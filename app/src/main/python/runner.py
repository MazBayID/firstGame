"""Constrained educational runner. This is NOT a hardened sandbox."""
import ast
import contextlib
import io
import json
import sys

MAX_SOURCE_LENGTH = 8000
MAX_STEPS = 20000

ALLOWED_NODES = (
    ast.Module, ast.Expr, ast.Assign, ast.AugAssign, ast.Name, ast.Load, ast.Store,
    ast.Constant, ast.List, ast.Tuple, ast.Dict, ast.Set, ast.For, ast.While, ast.If,
    ast.Compare, ast.Eq, ast.NotEq, ast.Lt, ast.LtE, ast.Gt, ast.GtE, ast.In,
    ast.NotIn, ast.Is, ast.IsNot, ast.BoolOp, ast.And, ast.Or, ast.UnaryOp,
    ast.Not, ast.USub, ast.UAdd, ast.BinOp, ast.Add, ast.Sub, ast.Mult, ast.Div,
    ast.FloorDiv, ast.Mod, ast.Pow, ast.Call, ast.keyword, ast.Attribute,
    ast.Subscript, ast.Slice, ast.IfExp, ast.With, ast.withitem, ast.Pass,
    ast.Break, ast.Continue,
)

class VirtualFile:
    def __init__(self, content):
        self._lines = content.splitlines(keepends=True)
        self._index = 0
    def __enter__(self):
        return self
    def __exit__(self, *_args):
        return False
    def __iter__(self):
        return self
    def __next__(self):
        if self._index >= len(self._lines):
            raise StopIteration
        line = self._lines[self._index]
        self._index += 1
        return line
    def read(self):
        value = "".join(self._lines[self._index:])
        self._index = len(self._lines)
        return value
    def readlines(self):
        value = self._lines[self._index:]
        self._index = len(self._lines)
        return list(value)

def run_code(source, access_log):
    if not isinstance(source, str) or len(source) > MAX_SOURCE_LENGTH:
        return json.dumps({"output": "", "error": "Code is empty or exceeds 8000 characters.", "passed": False})
    output = io.StringIO()
    steps = [MAX_STEPS]
    previous_trace = sys.gettrace()

    def virtual_open(filename, mode="r", *_args, **_kwargs):
        if filename != "access.log" or mode != "r":
            raise ValueError("Only read-only access to access.log is permitted")
        return VirtualFile(access_log)

    safe_builtins = {
        "print": print, "len": len, "range": range, "str": str, "int": int,
        "float": float, "bool": bool, "list": list, "dict": dict, "tuple": tuple,
        "set": set, "enumerate": enumerate, "sorted": sorted, "sum": sum,
        "min": min, "max": max, "abs": abs, "zip": zip,
    }

    def budget_trace(_frame, event, _arg):
        if event == "line":
            steps[0] -= 1
            if steps[0] < 0:
                raise RuntimeError("Execution budget exceeded")
        return budget_trace

    try:
        tree = ast.parse(source, mode="exec")
        for node in ast.walk(tree):
            if not isinstance(node, ALLOWED_NODES):
                raise ValueError("Unsupported syntax: " + type(node).__name__)
            if isinstance(node, ast.Name) and node.id.startswith("_"):
                raise ValueError("Private names are not allowed")
            if isinstance(node, ast.Attribute):
                if node.attr.startswith("_") or node.attr not in {"append", "strip", "lower", "upper"}:
                    raise ValueError("This attribute is not available")
            if isinstance(node, ast.Call) and isinstance(node.func, ast.Name):
                if node.func.id not in {
                    "print", "len", "range", "str", "int", "float", "bool",
                    "list", "dict", "tuple", "set", "enumerate", "sorted",
                    "sum", "min", "max", "abs", "zip", "open"
                }:
                    raise ValueError("Function is not available: " + node.func.id)
        compiled = compile(tree, "<mission>", "exec")
        namespace = {"__builtins__": safe_builtins, "open": virtual_open}
        with contextlib.redirect_stdout(output):
            sys.settrace(budget_trace)
            exec(compiled, namespace, namespace)
        result = output.getvalue().strip()
        return json.dumps({"output": result, "error": "", "passed": result == "2"})
    except Exception as exc:
        return json.dumps({"output": output.getvalue(), "error": f"{type(exc).__name__}: {exc}", "passed": False})
    finally:
        sys.settrace(previous_trace)
