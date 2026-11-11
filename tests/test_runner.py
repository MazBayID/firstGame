import json
import pathlib
import sys
import unittest

sys.path.insert(0, str(pathlib.Path(__file__).resolve().parents[1] / "app/src/main/python"))
import runner


class RunnerTests(unittest.TestCase):
    def run_code(self, source):
        return json.loads(runner.run_code_json(source))

    def submit(self, source, mission_id):
        return json.loads(runner.evaluate_mission(source, mission_id))

    def test_run_executes_without_completing_mission(self):
        result = self.run_code('print("hello")')
        self.assertEqual(result["output"].strip(), "hello")
        self.assertNotIn("passed", result)

    def test_mission_01(self):
        self.assertTrue(self.submit('print("Hello, Cyber Protocol!")', 1)["passed"])

    def test_mission_02(self):
        source = 'print("SYSTEM ONLINE")\nprint("ACCESS GRANTED")'
        self.assertTrue(self.submit(source, 2)["passed"])

    def test_mission_03_requires_variables(self):
        source = 'agent = "MARA"\nstatus = "ONLINE"\nprint(agent)\nprint(status)'
        self.assertTrue(self.submit(source, 3)["passed"])
        self.assertFalse(self.submit('print("MARA\\nONLINE")', 3)["passed"])

    def test_mission_04_arithmetic(self):
        self.assertTrue(self.submit("print(6 * 7)", 4)["passed"])

    def test_mission_05_requires_variables(self):
        source = "hp = 100\ndamage = 25\nhp = hp - damage\nprint(hp)"
        self.assertTrue(self.submit(source, 5)["passed"])
        self.assertFalse(self.submit("print(75)", 5)["passed"])

    def test_mission_06_conditional_fixtures(self):
        source = 'if energy > 0:\n    print("SYSTEM ACTIVE")\nelse:\n    print("SYSTEM STANDBY")'
        self.assertTrue(self.submit(source, 6)["passed"])
        self.assertFalse(self.submit('print("SYSTEM ACTIVE")', 6)["passed"])

    def test_mission_07_requires_loop(self):
        source = 'for step in range(1, 5):\n    print("SCAN", step)'
        self.assertTrue(self.submit(source, 7)["passed"])
        self.assertFalse(self.submit('print("SCAN 1\\nSCAN 2\\nSCAN 3\\nSCAN 4")', 7)["passed"])

    def test_mission_08_changing_fixtures(self):
        source = 'count = 0\nfor event in events:\n    if "ERROR" in event:\n        count += 1\nprint(count)'
        self.assertTrue(self.submit(source, 8)["passed"])
        self.assertFalse(self.submit("print(2)", 8)["passed"])

    def test_first_contact_stage_one_uses_structured_evaluator(self):
        source = 'signal = "ACCESS GRANTED"\nprint(signal)'
        result = self.submit(source, 9)
        self.assertTrue(result["passed"], result)
        self.assertFalse(self.submit('print("ACCESS GRANTED")', 9)["passed"])

    def test_first_contact_stage_two_uses_structured_evaluator(self):
        source = 'message = "IDENTITY VERIFIED"\nprint(message)'
        result = self.submit(source, 10)
        self.assertTrue(result["passed"], result)
        self.assertFalse(self.submit('print("IDENTITY VERIFIED")', 10)["passed"])

    def test_unknown_mission(self):
        self.assertFalse(self.submit("print(1)", 999)["passed"])

    def test_import_is_rejected(self):
        self.assertIn("Unsupported syntax", self.run_code("import os")["error"])

    def test_attribute_access_is_rejected(self):
        self.assertIn("Unsupported syntax", self.run_code('"x".__class__')["error"])

    def test_while_is_rejected(self):
        self.assertIn("Unsupported syntax", self.run_code("while True:\n    pass")["error"])

    def test_blank_source(self):
        self.assertIn("Write some Python code", self.run_code("   ")["error"])

    def test_range_budget_is_enforced(self):
        result = self.run_code("for item in range(5001):\n    print(item)")
        self.assertIn("Range is limited", result["error"])

    def test_output_is_bounded(self):
        result = self.run_code('print("x" * 12000)')
        self.assertLessEqual(len(result["output"]), runner.MAX_OUTPUT_LENGTH)

    def test_ast_complexity_is_bounded(self):
        source = "value = [" + ",".join("1" for _ in range(2600)) + "]"
        result = self.run_code(source)
        self.assertIn("too complex", result["error"])

    def test_large_integer_literal_is_rejected(self):
        result = self.run_code("value = 1000000001")
        self.assertIn("Integer constants are limited", result["error"])


if __name__ == "__main__":
    unittest.main()
