"""Offline Python learning runner for Cyber Protocol Chapter 01."""
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
    allowed_calls = {
        "print", "len", "range", "str", "int", "float", "bool", "list", "dict",
        "tuple", "set", "enumerate", "sorted", "sum", "min", "max", "abs", "zip",
    }
    allowed_attributes = {"append", "strip", "lower", "upper"}
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
            if node.attr.startswith("_") or node.attr not in allowed_attributes:
                raise ValueError("This attribute is not available: " + node.attr)
        if isinstance(node, ast.Call) and isinstance(node.func, ast.Name):
            if node.func.id not in allowed_calls:
                raise ValueError("Function is not available: " + node.func.id)
        if isinstance(node, ast.Call) and isinstance(node.func, ast.Attribute):
            if node.func.attr not in allowed_attributes:
                raise ValueError("This method is not available.")


def run_code(source, variables=None):
    output = LimitedOutput()
    steps = [MAX_STEPS]
    previous_trace = sys.gettrace()
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
        namespace = {"__builtins__": safe_builtins}
        namespace.update(variables or {})
        with contextlib.redirect_stdout(output):
            sys.settrace(budget_trace)
            exec(compiled, namespace, namespace)
        return {"output": output.getvalue().strip(), "error": ""}
    except Exception as exc:
        return {"output": output.getvalue().strip(), "error": f"{type(exc).__name__}: {exc}"}
    finally:
        sys.settrace(previous_trace)


# Exact-output missions deliberately accept the requested output: this is how
# beginners learn print(), strings, variables, and arithmetic without fake
# anti-hardcoding rules. Later missions use changing fixtures to test behavior.
MISSIONS = {
    1: {
        "title": "First Contact",
        "concept": "print() dan output pertama",
        "cases": [({}, "Hello, Cyber Protocol!")],
        "feedback": "Output berhasil dikirim. Kamu baru saja menjalankan program Python pertamamu.",
    },
    2: {
        "title": "Signal Online",
        "concept": "Beberapa print() dan komentar",
        "cases": [({}, "SYSTEM ONLINE\nACCESS GRANTED")],
        "feedback": "Dua baris output berhasil dikirim. Komentar membantu manusia membaca kode dan tidak dieksekusi Python.",
    },
    3: {
        "title": "Analyst Identity",
        "concept": "Variabel dan string",
        "cases": [({}, "MARA\nONLINE")],
        "feedback": "Variabel menyimpan nilai yang bisa digunakan kembali. Identitas analis berhasil ditampilkan.",
    },
    4: {
        "title": "Power Calibration",
        "concept": "Angka dan operator aritmetika",
        "cases": [({}, "42")],
        "feedback": "Perhitungan selesai. Python dapat melakukan operasi matematika langsung.",
    },
    5: {
        "title": "Repair the Shield",
        "concept": "Variabel dan operasi matematika",
        "cases": [({}, "HP: 75")],
        "feedback": "Nilai HP berhasil dihitung dari variabel. Kamu sudah menggabungkan penyimpanan nilai dan matematika.",
    },
    6: {
        "title": "Power Decision",
        "concept": "if, else, dan perbandingan",
        "cases": [
            ({"energy": 10}, "SYSTEM ACTIVE"),
            ({"energy": 0}, "SYSTEM STANDBY"),
            ({"energy": -5}, "SYSTEM STANDBY"),
        ],
        "feedback": "Keputusan sistem benar untuk energi positif, nol, dan negatif. Kondisi mengubah perilaku program.",
    },
    7: {
        "title": "Signal Sweep",
        "concept": "for dan range()",
        "cases": [({}, "SCAN 1\nSCAN 2\nSCAN 3\nSCAN 4")],
        "feedback": "Empat tahap pemindaian dijalankan berurutan. Loop menghindarkan kita dari menulis perintah yang sama berulang kali.",
    },
    8: {
        "title": "Threat Triage",
        "concept": "List, for, if, dan counter",
        "cases": [
            ({"events": ["INFO: boot", "ERROR: token", "WARNING: retry", "ERROR: denied"]}, "2"),
            ({"events": ["ERROR: login", "INFO: audit"]}, "1"),
            ({"events": ["INFO: ready", "WARNING: disk"]}, "0"),
        ],
        "feedback": "Penyaring ancaman berhasil pada tiga kumpulan data berbeda. Kamu siap mempelajari file log pada chapter berikutnya.",
    },
}


def evaluate_mission(source, mission_id=1):
    """Run a mission against its expected output or varied behavior fixtures."""
    mission = MISSIONS.get(int(mission_id))
    if mission is None:
        return json.dumps({
            "output": "", "error": "Unknown mission. Select an available mission.",
            "passed": False, "feedback": "Mission not found.",
        })
    outputs = []
    for case_number, (variables, expected) in enumerate(mission["cases"], start=1):
        result = run_code(source, variables)
        if result["error"]:
            return json.dumps({
                "output": result["output"], "error": result["error"], "passed": False,
                "feedback": f"Python error in practice test {case_number}/{len(mission['cases'])}. Read the explanation and try again.",
            })
        actual = result["output"].strip()
        outputs.append(actual)
        if actual != expected:
            return json.dumps({
                "output": outputs[0], "error": "", "passed": False,
                "feedback": f"Test {case_number}/{len(mission['cases'])}: expected {expected!r}, got {actual or '(no output)'!r}. Compare your output with the demonstration.",
            })
    return json.dumps({
        "output": outputs[0], "error": "", "passed": True,
        "feedback": mission["feedback"],
    })
