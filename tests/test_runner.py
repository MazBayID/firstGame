import json
import pathlib
import sys
import unittest

sys.path.insert(0, str(pathlib.Path(__file__).resolve().parents[1] / "app/src/main/python"))
import runner


class Chapter01Tests(unittest.TestCase):
    def evaluate(self, source, mission_id):
        return json.loads(runner.evaluate_mission(source, mission_id))

    def test_mission_01_print_first_message(self):
        result = self.evaluate('print("Hello, Cyber Protocol!")', 1)
        self.assertTrue(result["passed"])

    def test_mission_02_multiple_lines_and_comment(self):
        source = '# Boot sequence\nprint("SYSTEM ONLINE")\nprint("ACCESS GRANTED")'
        self.assertTrue(self.evaluate(source, 2)["passed"])

    def test_mission_03_variables(self):
        source = 'agent = "MARA"\nstatus = "ONLINE"\nprint(agent)\nprint(status)'
        self.assertTrue(self.evaluate(source, 3)["passed"])

    def test_mission_04_arithmetic(self):
        self.assertTrue(self.evaluate("print(6 * 7)", 4)["passed"])

    def test_mission_05_variable_arithmetic(self):
        source = 'hp = 100\ndamage = 25\nhp = hp - damage\nprint("HP:", hp)'
        self.assertTrue(self.evaluate(source, 5)["passed"])

    def test_mission_06_if_else_handles_all_fixtures(self):
        source = 'if energy > 0:\n    print("SYSTEM ACTIVE")\nelse:\n    print("SYSTEM STANDBY")'
        self.assertTrue(self.evaluate(source, 6)["passed"])

    def test_mission_07_for_range(self):
        source = 'for step in range(1, 5):\n    print("SCAN", step)'
        self.assertTrue(self.evaluate(source, 7)["passed"])

    def test_mission_08_list_filter_and_counter(self):
        source = '''count = 0
for event in events:
    if "ERROR" in event:
        count += 1
print(count)'''
        self.assertTrue(self.evaluate(source, 8)["passed"])

    def test_beginner_exact_output_is_valid(self):
        self.assertTrue(self.evaluate('print("Hello, Cyber Protocol!")', 1)["passed"])

    def test_wrong_output_does_not_pass(self):
        self.assertFalse(self.evaluate('print("wrong")', 1)["passed"])

    def test_hardcoded_answer_fails_behavior_fixtures(self):
        result = self.evaluate("print(2)", 8)
        self.assertFalse(result["passed"])
        self.assertIn("Test 2/3", result["feedback"])

    def test_unknown_mission_is_rejected(self):
        result = self.evaluate("print(1)", 99)
        self.assertFalse(result["passed"])
        self.assertIn("Unknown mission", result["error"])

    def test_rejects_imports(self):
        result = self.evaluate("import os", 1)
        self.assertIn("Unsupported syntax", result["error"])

    def test_instruction_budget(self):
        result = self.evaluate("while True:\n    pass", 1)
        self.assertIn("Execution budget exceeded", result["error"])

    def test_range_budget(self):
        result = self.evaluate("print(len(range(1000000000)))", 1)
        self.assertIn("range is limited", result["error"])

    def test_blank_source(self):
        result = self.evaluate("   ", 1)
        self.assertIn("Write some Python code", result["error"])


if __name__ == "__main__":
    unittest.main()
