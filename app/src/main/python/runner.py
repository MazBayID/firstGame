"""Restricted Python teaching runner. Prototype guardrails, not a hardened sandbox."""
import ast
import contextlib
import io
import json
import sys

MAX_SOURCE_LENGTH = 12000
MAX_OUTPUT_LENGTH = 8000
MAX_TRACE_EVENTS = 50000
MAX_AST_NODES = 2500
MAX_RANGE_ITEMS = 5000
MAX_INTEGER_ABS = 1_000_000_000


class LimitedTextWriter:
    """Capture stdout without allowing unbounded output accumulation."""
    def __init__(self, limit):
        self.limit = limit
        self.parts = []
        self.length = 0

    def write(self, value):
        text = str(value)
        remaining = self.limit - self.length
        if remaining > 0:
            accepted = text[:remaining]
            self.parts.append(accepted)
            self.length += len(accepted)
        return len(text)

    def flush(self):
        return None

    def getvalue(self):
        return "".join(self.parts)


def _bounded_range(*args):
    value = range(*args)
    if len(value) > MAX_RANGE_ITEMS:
        raise RunnerError(
            f"Range is limited to {MAX_RANGE_ITEMS} steps in this training environment."
        )
    return value


SAFE_BUILTINS = {
    "print": print, "range": _bounded_range, "len": len, "str": str, "int": int,
    "float": float, "bool": bool, "enumerate": enumerate, "min": min,
    "max": max, "sum": sum, "abs": abs,
}
ALLOWED_NODES = (
    ast.Module, ast.Expr, ast.Call, ast.Name, ast.Load, ast.Store, ast.Constant,
    ast.Assign, ast.BinOp, ast.Add, ast.Sub, ast.Mult, ast.Div, ast.FloorDiv,
    ast.Mod, ast.UnaryOp, ast.USub, ast.UAdd, ast.Compare, ast.Gt, ast.GtE,
    ast.Lt, ast.LtE, ast.Eq, ast.NotEq, ast.In, ast.NotIn, ast.If, ast.For,
    ast.AugAssign, ast.List, ast.Tuple, ast.BoolOp, ast.And, ast.Or, ast.Not,
    ast.Subscript,
)


class RunnerError(Exception):
    pass


def _validate(tree, extra_call_names=()):
    nodes = list(ast.walk(tree))
    if len(nodes) > MAX_AST_NODES:
        raise RunnerError("This program is too complex for the current training environment.")
    for node in nodes:
        if not isinstance(node, ALLOWED_NODES):
            raise RunnerError(f"Unsupported syntax: {type(node).__name__}")
        if isinstance(node, ast.Name) and (node.id.startswith("_") or "__" in node.id):
            raise RunnerError("Names beginning with underscore are not available.")
        if isinstance(node, ast.Call):
            if not isinstance(node.func, ast.Name) or node.func.id not in SAFE_BUILTINS and node.func.id not in extra_call_names:
                raise RunnerError("Only approved built-in functions may be called.")
        if isinstance(node, ast.Constant) and isinstance(node.value, (bytes, complex)):
            raise RunnerError("This value type is not available in the training runner.")
        if isinstance(node, ast.Constant) and isinstance(node.value, int):
            if abs(node.value) > MAX_INTEGER_ABS:
                raise RunnerError("Integer constants are limited in this training environment.")
        if isinstance(node, (ast.List, ast.Tuple)) and len(node.elts) > 1000:
            raise RunnerError("Collection literals are limited to 1000 items.")


def _execute(source, variables=None, workspace_files=None, writable_files=None):
    if not isinstance(source, str) or not source.strip():
        raise RunnerError("Write some Python code before running.")
    if len(source) > MAX_SOURCE_LENGTH:
        raise RunnerError("Source is too long for this training exercise.")
    try:
        tree = ast.parse(source, mode="exec")
    except SyntaxError as error:
        return {"output": "", "error": f"SyntaxError: {error.msg} (line {error.lineno})"}
    workspace = dict(workspace_files or {})
    writable = set(writable_files or ())

    def read_text(name):
        if not isinstance(name, str) or name not in workspace:
            raise RunnerError("File is not available in this mission workspace.")
        return workspace[name]

    def write_text(name, content):
        if not isinstance(name, str) or name not in writable:
            raise RunnerError("This file is read-only or outside the mission workspace.")
        if not isinstance(content, str) or len(content) > 50000:
            raise RunnerError("Mission files must contain text up to 50000 characters.")
        workspace[name] = content

    safe_builtins = dict(SAFE_BUILTINS)
    safe_builtins.update({"read_text": read_text, "write_text": write_text})
    try:
        _validate(tree, {"read_text", "write_text"})
    except RunnerError as error:
        return {"output": "", "error": str(error)}

    namespace = dict(variables or {})
    output = LimitedTextWriter(MAX_OUTPUT_LENGTH)
    counter = {"events": 0}

    def trace(frame, event, arg):
        if event == "line":
            counter["events"] += 1
            if counter["events"] > MAX_TRACE_EVENTS:
                raise RunnerError("Execution budget exceeded. Check for an unbounded loop.")
        return trace

    previous_trace = sys.gettrace()
    try:
        sys.settrace(trace)
        with contextlib.redirect_stdout(output):
            exec(compile(tree, "<training-mission>", "exec"),
                 {"__builtins__": safe_builtins}, namespace)
    except RunnerError as error:
        return {"output": output.getvalue(), "error": str(error), "files": workspace}
    except Exception as error:
        return {"output": output.getvalue(),
                "error": f"{type(error).__name__}: {error}", "files": workspace}
    finally:
        sys.settrace(previous_trace)
    return {"output": output.getvalue(), "error": "", "files": workspace}


def run_code_json(source, workspace_json="{}", writable_json="[]"):
    try:
        workspace_files = json.loads(workspace_json)
        writable_files = json.loads(writable_json)
        if not isinstance(workspace_files, dict) or not isinstance(writable_files, list):
            raise RunnerError("Invalid mission workspace contract.")
        if any(not isinstance(k, str) or not isinstance(v, str) for k, v in workspace_files.items()):
            raise RunnerError("Mission workspace only supports named text files.")
        return json.dumps(_execute(source, workspace_files=workspace_files,
                                   writable_files=writable_files))
    except RunnerError as error:
        return json.dumps({"output": "", "error": str(error), "files": {}})
    except (TypeError, ValueError) as error:
        return json.dumps({"output": "", "error": f"Invalid workspace data: {error}", "files": {}})


def _has_node(tree, node_type):
    return any(isinstance(node, node_type) for node in ast.walk(tree))


def _has_assigned_name(tree, name):
    return any(isinstance(node, ast.Name) and node.id == name
               and isinstance(node.ctx, ast.Store) for node in ast.walk(tree))


def _concept_check(source, mission_id):
    try:
        tree = ast.parse(source, mode="exec")
    except SyntaxError:
        return "Fix the syntax error before submitting."
    if mission_id == 3 and not (_has_assigned_name(tree, "agent")
                                and _has_assigned_name(tree, "status")):
        return "This mission teaches variables. Assign values to agent and status."
    if mission_id == 5 and not (_has_assigned_name(tree, "hp")
                                and _has_assigned_name(tree, "damage")):
        return "Use the hp and damage variables required by this exercise."
    if mission_id == 6 and not _has_node(tree, ast.If):
        return "The objective requires a conditional. Use if/else."
    if mission_id == 7 and not _has_node(tree, ast.For):
        return "The objective requires a for loop. Repeating print statements is not enough."
    if mission_id == 8 and not (_has_node(tree, ast.For) and _has_node(tree, ast.If)):
        return "Use both a loop and a condition to inspect the changing event list."
    if mission_id == 9 and not (_has_assigned_name(tree, "signal") and _has_node(tree, ast.Call)):
        return "Assign the first signal to a variable named signal, then display it with print()."
    if mission_id == 10 and not (_has_assigned_name(tree, "message") and _has_node(tree, ast.Call)):
        return "Assign the verified message to a variable named message, then display it with print()."
    return ""


MISSIONS = {
    1: {"cases": [({}, "Hello, Cyber Protocol!")]},
    2: {"cases": [({}, "SYSTEM ONLINE\nACCESS GRANTED")]},
    3: {"cases": [({}, "MARA\nONLINE")]},
    4: {"cases": [({}, "42")]},
    5: {"cases": [({}, "75")]},
    6: {"cases": [({"energy": 10}, "SYSTEM ACTIVE"), ({"energy": 0}, "SYSTEM STANDBY"), ({"energy": -5}, "SYSTEM STANDBY")]},
    7: {"cases": [({}, "SCAN 1\nSCAN 2\nSCAN 3\nSCAN 4")]},
    8: {"cases": [
        ({"events": ["INFO: boot", "ERROR: token", "WARNING: retry", "ERROR: denied"]}, "2"),
        ({"events": ["ERROR: login", "INFO: audit"]}, "1"),
        ({"events": ["INFO: ready", "WARNING: disk"]}, "0"),
    ]},
    9: {"cases": [({}, "ACCESS GRANTED")]},
    10: {"cases": [({}, "IDENTITY VERIFIED")]},
}


def evaluate_mission(source, mission_id=1):
    try:
        mission_id = int(mission_id)
    except (TypeError, ValueError):
        mission_id = -1
    mission = MISSIONS.get(mission_id)
    if mission is None:
        return json.dumps({"output": "", "error": "Unknown mission.",
                           "passed": False, "feedback": "Mission not found."})
    concept_feedback = _concept_check(source, mission_id)
    if concept_feedback:
        return json.dumps({"output": "", "error": "", "passed": False,
                           "feedback": concept_feedback})
    outputs = []
    cases = mission["cases"]
    for index, (variables, expected) in enumerate(cases, start=1):
        result = _execute(source, variables)
        if result["error"]:
            return json.dumps({"output": result["output"], "error": result["error"],
                               "passed": False,
                               "feedback": f"Execution failed in test {index}/{len(cases)}."})
        actual = result["output"].strip()
        outputs.append(actual)
        if actual != expected:
            return json.dumps({"output": actual, "error": "", "passed": False,
                               "feedback": f"Test {index}/{len(cases)}: expected {expected!r}, got {actual or '(no output)'!r}."})
    return json.dumps({"output": outputs[0] if outputs else "", "error": "",
                       "passed": True,
                       "feedback": "All mission checks passed. Required concepts and behavior were verified."})
