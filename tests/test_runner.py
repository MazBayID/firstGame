import json
import pathlib
import sys
import unittest

sys.path.insert(0, str(pathlib.Path(__file__).resolve().parents[1] / "app/src/main/python"))
import runner

LOG = "INFO: started\nERROR: one\nWARNING: wait\nERROR: two\n"

class RunnerTests(unittest.TestCase):
    def run_code(self, source):
        return json.loads(runner.run_code(source, LOG))

    def test_counts_error_lines(self):
        source = '''errors = []
with open("access.log", "r") as log_file:
    for line in log_file:
        if "ERROR" in line:
            errors.append(line.strip())
print(len(errors))'''
        result = self.run_code(source)
        self.assertEqual(result["output"], "2")
        self.assertTrue(result["passed"])

    def test_rejects_imports(self):
        result = self.run_code("import os")
        self.assertIn("Unsupported syntax", result["error"])

    def test_rejects_other_files(self):
        result = self.run_code('open("/etc/passwd")')
        self.assertIn("Only read-only access", result["error"])

    def test_instruction_budget(self):
        result = self.run_code("while True:\n    pass")
        self.assertIn("Execution budget exceeded", result["error"])

if __name__ == "__main__":
    unittest.main()
