"""Offline mission runner with a small Python teaching surface.
This is a local educational runner, NOT a security sandbox for hostile code.
"""
import ast
import contextlib
import io
import json
import sys

MAX_SOURCE_LENGTH = 8000
MAX_AST_NODES = 2000
MAX_STEPS = 20000
MAX_RANGE_ITEMS = 10000
MAX_OUTPUT_LENGTH = 16000

ALLOWED_NODES = (
    ast.Module, ast.Expr, ast.Assign, ast.AugAssign, ast.Name, ast.Load, ast.Store,
    ast.Constant, ast.List, ast.Tuple, ast.Dict, ast.Set, ast.For, ast.While, ast.If,
    ast.Compare, ast.Eq, ast.NotEq, ast.Lt, ast.LtE, ast.Gt, ast.GtE, ast.In,
    ast.NotIn, ast.Is, ast.IsNot, ast.BoolOp, ast.And, ast.Or, ast.UnaryOp,
    ast.Not, ast.USub, ast.UAdd, ast.BinOp, ast.Add, ast.Sub, ast.Mult, ast.Div,
    ast.FloorDiv, ast.Mod, ast.Call, ast.Attribute, ast.Subscript, ast.Slice,
    ast.IfExp, ast.With, ast.withitem, ast.Pass, ast.Break, ast.Continue,
)
ALLOWED_CALLS = {
    "print", "len", "range", "str", "int", "float", "bool", "list", "dict",
    "tuple", "set", "enumerate", "sorted", "sum", "min", "max", "abs", "zip", "open",
}
ALLOWED_ATTRIBUTES = {"append", "strip", "lower", "upper", "read", "readlines", "close"}


class VirtualFile:
    def __init__(self, content):
        self._lines = content.splitlines(keepends=True)
        self._index = 0
        self.closed = False

    def __enter__(self):
        self._check_open()
        return self

    def __exit__(self, *_args):
        self.close()
        return False

    def __iter__(self):
        return self

    def __next__(self):
        if self.closed or self._index >= len(self._lines):
            raise StopIteration
        line = self._lines[self._index]
        self._index += 1
        return line

    def read(self):
        self._check_open()
        value = "".join(self._lines[self._index:])
        self._index = len(self._lines)
        return value

    def readlines(self):
        self._check_open()
        value = self._lines[self._index:]
        self._index = len(self._lines)
        return list(value)

    def close(self):
        self.closed = True

    def _check_open(self):
        if self.closed:
            raise ValueError("I/O operation on closed file")


class LimitedOutput(io.StringIO):
    def write(self, value):
        remaining = MAX_OUTPUT_LENGTH - len(self.getvalue())
        if len(value) > remaining:
            if remaining > 0:
                super().write(value[:remaining])
            raise RuntimeError("Output limit exceeded")
        return super().write(value)


def _bounded_range(*args):
    result = range(*args)
    if len(result) > MAX_RANGE_ITEMS:
        raise ValueError(f"range is limited to {MAX_RANGE_ITEMS} items")
    return result


def _validate(source):
    if not isinstance(source, str) or not source.strip():
        raise ValueError("Write some Python code before running the mission.")
    if len(source) > MAX_SOURCE_LENGTH:
        raise ValueError("Code exceeds the 8000 character limit.")
    tree = ast.parse(source, mode="exec")
    nodes = list(ast.walk(tree))
    if len(nodes) > MAX_AST_NODES:
        raise ValueError("Code is too complex for this training mission.")
    for node in nodes:
        if not isinstance(node, ALLOWED_NODES):
            raise ValueError("Unsupported syntax: " + type(node).__name__)
        if isinstance(node, ast.Name) and node.id.startswith("_"):
            raise ValueError("Private names are not available in this lab.")
        if isinstance(node, ast.Constant):
            if isinstance(node.value, int) and node.value.bit_length() > 1024:
                raise ValueError("Integer literal is too large.")
            if isinstance(node.value, (str, bytes)) and len(node.value) > 4096:
                raise ValueError("Text literal is too long.")
        if isinstance(node, ast.Attribute):
            if node.attr.startswith("_") or node.attr not in ALLOWED_ATTRIBUTES:
                raise ValueError("This attribute is not available: " + node.attr)
        if isinstance(node, ast.Call) and isinstance(node.func, ast.Name):
            if node.func.id not in ALLOWED_CALLS:
                raise ValueError("Function is not available: " + node.func.id)
        if isinstance(node, ast.Call) and isinstance(node.func, ast.Attribute):
            if node.func.attr not in ALLOWED_ATTRIBUTES:
                raise ValueError("This method is not available.")


def run_code(source, access_log):
    output = LimitedOutput()
    steps = [MAX_STEPS]
    previous_trace = sys.gettrace()

    def virtual_open(filename, mode="r", *_args, **_kwargs):
        if filename != "access.log":
            raise ValueError("This mission only contains access.log")
        if mode not in ("r", "rt"):
            raise ValueError("The mission file is read-only.")
        return VirtualFile(access_log)

    safe_builtins = {
        "print": print, "len": len, "range": _bounded_range, "str": str,
        "int": int, "float": float, "bool": bool, "list": list, "dict": dict,
        "tuple": tuple, "set": set, "enumerate": enumerate, "sorted": sorted,
        "sum": sum, "min": min, "max": max, "abs": abs, "zip": zip,
    }

    def budget_trace(_frame, event, _arg):
        if event == "line":
            steps[0] -= 1
            if steps[0] < 0:
                raise RuntimeError("Execution budget exceeded")
        return budget_trace

    try:
        _validate(source)
        compiled = compile(source, "<mission>", "exec")
        namespace = {"__builtins__": safe_builtins, "open": virtual_open}
        with contextlib.redirect_stdout(output):
            sys.settrace(budget_trace)
            exec(compiled, namespace, namespace)
        return {"output": output.getvalue().strip(), "error": ""}
    except Exception as exc:
        return {"output": output.getvalue().strip(), "error": f"{type(exc).__name__}: {exc}"}
    finally:
        sys.settrace(previous_trace)


def evaluate_mission(source):
    """Run the player's program against three different evidence files."""
    cases = (
        ("INFO: booted\nERROR: invalid token\nWARNING: retry\nERROR: denied\n", "2"),
        ("ERROR: suspicious login\nINFO: audit complete\n", "1"),
        ("INFO: service started\nWARNING: retry scheduled\n", "0"),
    )
    outputs = []
    for log_text, expected in cases:
        result = run_code(source, log_text)
        if result["error"]:
            return json.dumps({
                "output": result["output"], "error": result["error"], "passed": False,
                "feedback": "Fix the Python error, then run the mission again.",
            })
        actual = result["output"].strip()
        outputs.append(actual)
        if actual != expected:
            return json.dumps({
                "output": outputs[0], "error": "", "passed": False,
                "feedback": "Your code ran, but it did not solve every evidence test. Do not hardcode the answer.",
            })
    return json.dumps({
        "output": outputs[0], "error": "", "passed": True,
        "feedback": "All evidence tests passed. The ERROR counter works on different log files.",
    })
