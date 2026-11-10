import json
import pathlib
import sys
import unittest

sys.path.insert(0, str(pathlib.Path(__file__).resolve().parents[1] / "app/src/main/python"))
import runner

SOLUTION = '''errors = []
with open("access.log", "r") as log_file:
    for line in log_file:
        if "ERROR" in line:
            errors.append(line.strip())
print(len(errors))'''


class RunnerTests(unittest.TestCase):
    def evaluate(self, source):
        return json.loads(runner.evaluate_mission(source))

    def test_real_solution_passes_multiple_evidence_files(self):
        result = self.evaluate(SOLUTION)
        self.assertEqual(result["output"], "2")
        self.assertTrue(result["passed"])

    def test_hardcoded_answer_does_not_pass(self):
        result = self.evaluate("print(2)")
        self.assertFalse(result["passed"])
        self.assertIn("hardcode", result["feedback"])

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
