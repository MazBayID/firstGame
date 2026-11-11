import json
import pathlib
import sys
import unittest

sys.path.insert(0, str(pathlib.Path(__file__).resolve().parents[1] / "app/src/main/python"))
import runner

ERROR_SOLUTION = '''count = 0
for line in logs:
    if "ERROR" in line:
        count += 1
print(count)'''

WARNING_SOLUTION = '''warnings = []
with open("access.log", "r") as log_file:
    for line in log_file:
        if "WARNING" in line:
            warnings.append(line.strip())
print(len(warnings))'''

FAILED_SOLUTION = '''failed = []
with open("access.log", "r") as log_file:
    for line in log_file:
        if "FAILED" in line:
            failed.append(line.strip())
print(len(failed))'''


class RunnerTests(unittest.TestCase):
    def evaluate(self, source, mission_id=1):
        return json.loads(runner.evaluate_mission(source, mission_id))

    def test_mission_01_error_counter_passes(self):
        result = self.evaluate(ERROR_SOLUTION, 1)
        self.assertEqual(result["output"], "2")
        self.assertTrue(result["passed"])

    def test_mission_01_logs_are_available(self):
        result = self.evaluate(ERROR_SOLUTION, 1)
        self.assertTrue(result["passed"])
        self.assertEqual(result["output"], "2")

    def test_mission_02_warning_counter_passes(self):
        result = self.evaluate(WARNING_SOLUTION, 2)
        self.assertEqual(result["output"], "2")
        self.assertTrue(result["passed"])

    def test_mission_03_failed_login_counter_passes(self):
        result = self.evaluate(FAILED_SOLUTION, 3)
        self.assertEqual(result["output"], "2")
        self.assertTrue(result["passed"])

    def test_hardcoded_answer_does_not_pass(self):
        result = self.evaluate("print(2)", 1)
        self.assertFalse(result["passed"])
        self.assertIn("Evidence test 2/3", result["feedback"])

    def test_mission_specific_solution_does_not_pass_other_mission(self):
        result = self.evaluate(ERROR_SOLUTION, 2)
        self.assertFalse(result["passed"])

    def test_unknown_mission_is_rejected(self):
        result = self.evaluate(ERROR_SOLUTION, 99)
        self.assertFalse(result["passed"])
        self.assertIn("Unknown mission", result["error"])

    def test_rejects_imports(self):
        result = self.evaluate("import os")
        self.assertIn("Unsupported syntax", result["error"])

    def test_rejects_other_files(self):
        result = self.evaluate('print(open("/etc/passwd").read())')
        self.assertIn("only contains access.log", result["error"])

    def test_rejects_write_mode(self):
        result = self.evaluate('open("access.log", "w")')
        self.assertIn("read-only", result["error"])

    def test_instruction_budget(self):
        result = self.evaluate("while True:\n    pass")
        self.assertIn("Execution budget exceeded", result["error"])

    def test_range_budget(self):
        result = self.evaluate("print(len(range(1000000000)))")
        self.assertIn("range is limited", result["error"])

    def test_blank_source(self):
        result = self.evaluate("   ")
        self.assertIn("Write some Python code", result["error"])


if __name__ == "__main__":
    unittest.main()
