import json
import pathlib
import sys
import unittest
from unittest.mock import patch

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
        self.assertFalse(self.submit('print("MARA\nONLINE")', 3)["passed"])

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
        self.assertFalse(self.submit('print("SCAN 1\nSCAN 2\nSCAN 3\nSCAN 4")', 7)["passed"])

    def test_mission_08_changing_fixtures(self):
        source = 'count = 0\nfor event in events:\n    if "ERROR" in event:\n        count += 1\nprint(count)'
        self.assertTrue(self.submit(source, 8)["passed"])
        self.assertFalse(self.submit("print(2)", 8)["passed"])

    def test_first_contact_stage_one_uses_structured_evaluator(self):
        source = 'signal = "ACCESS GRANTED"\nprint(signal)'
        result = self.submit(source, 9)
        self.assertTrue(result["passed"], result)
        self.assertFalse(self.submit('print("ACCESS GRANTED")', 9)["passed"])
        bypass = self.submit('signal = "WRONG"\nprint("ACCESS GRANTED")', 9)
        self.assertFalse(bypass["passed"])
        self.assertIn("print(signal)", bypass["feedback"])

    def test_first_contact_stage_two_uses_structured_evaluator(self):
        source = 'message = "IDENTITY VERIFIED"\nprint(message)'
        result = self.submit(source, 10)
        self.assertTrue(result["passed"], result)
        self.assertFalse(self.submit('print("IDENTITY VERIFIED")', 10)["passed"])
        bypass = self.submit('message = "WRONG"\nprint("IDENTITY VERIFIED")', 10)
        self.assertFalse(bypass["passed"])
        self.assertIn("print(message)", bypass["feedback"])

    def test_first_contact_audit_uses_loop_and_condition(self):
        source = 'count = 0\nfor event in events:\n    if "ERROR" in event:\n        count += 1\nprint(count)'
        self.assertTrue(self.submit(source, 8)["passed"])
        self.assertFalse(self.submit("print(2)", 8)["passed"])

    def test_first_contact_inventory_requires_len_and_named_result(self):
        source = "record_count = len(records)\nprint(record_count)"
        result = self.submit(source, 11)
        self.assertTrue(result["passed"], result)
        self.assertFalse(self.submit("print(len(records))", 11)["passed"])

    def test_first_contact_trace_uses_enumerate_and_filtering(self):
        source = 'for index, receipt in enumerate(receipts):\n    if "RX-14" in receipt:\n        print(index, receipt)'
        result = self.submit(source, 12)
        self.assertTrue(result["passed"], result)
        self.assertFalse(self.submit('for receipt in receipts:\n    if "RX-14" in receipt:\n        print(receipt)', 12)["passed"])

    def test_first_contact_compare_uses_list_indexing(self):
        source = 'primary = receipts[0]\narchive = receipts[1]\nprint(primary)\nprint(archive)'
        result = self.submit(source, 13)
        self.assertTrue(result["passed"], result)
        self.assertFalse(self.submit('print(receipts[0])\nprint(receipts[1])', 13)["passed"])

    def test_first_contact_normalize_uses_integer_conversion_and_signed_delta(self):
        source = 'first_time = int(receipt_seconds[0])\nsecond_time = int(receipt_seconds[1])\ndelta = second_time - first_time\nprint(delta)'
        result = self.submit(source, 14)
        self.assertTrue(result["passed"], result)
        self.assertFalse(self.submit("print(4)", 14)["passed"])

    def test_first_contact_validates_absolute_delta_across_reversed_sources(self):
        source = 'difference = abs(int(receipt_seconds[1]) - int(receipt_seconds[0]))\nprint(difference)'
        result = self.submit(source, 15)
        self.assertTrue(result["passed"], result)
        self.assertFalse(self.submit("print(4)", 15)["passed"])

    def test_mission_run_receives_read_only_input_variables(self):
        source = 'count = 0\nfor event in events:\n    if "ERROR" in event:\n        count += 1\nprint(count)'
        result = json.loads(runner.run_code_json(
            source, "{}", "[]", json.dumps({"events": ["INFO: ready", "ERROR: denied"]})
        ))
        self.assertEqual("1", result["output"].strip())
        self.assertEqual("", result["error"])

    def test_mission_run_rejects_invalid_input_variables(self):
        result = json.loads(runner.run_code_json(
            "print(1)", "{}", "[]", json.dumps({"__class__": ["blocked"]})
        ))
        self.assertIn("Invalid mission input variable", result["error"])

    def test_mission_input_lists_are_read_only(self):
        result = json.loads(runner.run_code_json(
            'events[0] = "CHANGED"\nprint(events[0])',
            "{}", "[]", json.dumps({"events": ["ERROR: original"]})
        ))
        self.assertIn("does not support item assignment", result["error"])

    def test_mission_workspace_reads_only_allowlisted_files(self):
        source = 'print(read_text("briefing.txt"))'
        result = json.loads(runner.run_code_json(
            source, json.dumps({"briefing.txt": "AUTHORIZED BRIEF"}),
            json.dumps(["script.py"])
        ))
        self.assertEqual(result["output"].strip(), "AUTHORIZED BRIEF")
        denied = json.loads(runner.run_code_json(
            'print(read_text("private.txt"))',
            json.dumps({"briefing.txt": "AUTHORIZED BRIEF"}),
            json.dumps(["script.py"])
        ))
        self.assertIn("not available", denied["error"])

    def test_first_contact_signal_asset_is_readable_in_mission_workspace(self):
        source = 'signal = read_text("message_fragment.txt")\nprint(signal)'
        result = json.loads(runner.run_code_json(
            source,
            json.dumps({"message_fragment.txt": "ACCESS GRANTED\n"}),
            json.dumps(["signal.py"])
        ))
        self.assertEqual("", result["error"], result)
        self.assertEqual("ACCESS GRANTED", result["output"].strip())

    def test_mission_workspace_writes_only_writable_files(self):
        result = json.loads(runner.run_code_json(
            'write_text("script.py", "print(42)")',
            json.dumps({"briefing.txt": "READ ONLY"}),
            json.dumps(["script.py"])
        ))
        self.assertEqual(result["files"]["script.py"], "print(42)")
        self.assertEqual(result["files"]["briefing.txt"], "READ ONLY")

        denied = json.loads(runner.run_code_json(
            'write_text("briefing.txt", "tampered")',
            json.dumps({"briefing.txt": "READ ONLY"}),
            json.dumps(["script.py"])
        ))
        self.assertIn("read-only", denied["error"])

    def test_mission_workspace_rejects_oversized_file_content(self):
        large_content = " + ".join(['"x" * 1000'] * 51)
        source = 'write_text("script.py", ' + large_content + ')'
        result = json.loads(runner.run_code_json(
            source, json.dumps({}), json.dumps(["script.py"])
        ))
        self.assertIn("50000", result["error"])

    def test_evaluator_feedback_is_localized_without_changing_default_english(self):
        english = json.loads(runner.evaluate_mission('print("wrong")', 1))
        indonesian = json.loads(runner.evaluate_mission('print("wrong")', 1, "id"))
        self.assertIn("Test 1/1:", english["feedback"])
        self.assertIn("Pengujian 1/1:", indonesian["feedback"])

        concept = json.loads(runner.evaluate_mission('print("ACCESS GRANTED")', 9, "id"))
        self.assertIn("variabel signal", concept["feedback"])

        passed = json.loads(runner.evaluate_mission(
            'signal = "ACCESS GRANTED"\nprint(signal)', 9, "id"
        ))
        self.assertTrue(passed["passed"])
        self.assertIn("Semua pemeriksaan misi berhasil", passed["feedback"])

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

    def test_wall_clock_execution_budget_is_enforced(self):
        with patch("runner.time.monotonic", side_effect=[0.0, 4.0]):
            result = runner._execute("print(1)")
        self.assertIn("Execution time limit exceeded", result["error"])

    def test_output_is_bounded(self):
        result = self.run_code('print("x" * 12000)')
        self.assertLessEqual(len(result["output"]), runner.MAX_OUTPUT_LENGTH)

    def test_ast_complexity_is_bounded(self):
        source = "value = [" + ",".join("1" for _ in range(2600)) + "]"
        result = self.run_code(source)
        self.assertIn("too complex", result["error"])

    def test_large_string_repetition_is_rejected(self):
        result = self.run_code('text = "x"\nprint(text * 1001)')
        self.assertIn("repetition is limited", result["error"])

    def test_large_list_repetition_is_rejected(self):
        result = self.run_code("items = [1]\nexpanded = items * 1001")
        self.assertIn("repetition is limited", result["error"])

    def test_oversized_text_literal_is_rejected(self):
        source = 'text = "' + ("x" * 10001) + '"'
        result = self.run_code(source)
        self.assertIn("Text literals are limited", result["error"])

    def test_large_integer_literal_is_rejected(self):
        result = self.run_code("value = 1000000001")
        self.assertIn("Integer constants are limited", result["error"])


if __name__ == "__main__":
    unittest.main()
