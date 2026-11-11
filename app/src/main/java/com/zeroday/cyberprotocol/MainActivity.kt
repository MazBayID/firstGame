package com.zeroday.cyberprotocol

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.chaquo.python.Python
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

private val Bg = Color(0xFF080D12)
private val Panel = Color(0xFF111B23)
private val PanelAlt = Color(0xFF17232D)
private val Neon = Color(0xFF52F2A5)
private val Cyan = Color(0xFF55C8E8)
private val Muted = Color(0xFF91A3B2)
private val Danger = Color(0xFFFF7777)
private const val PREFS = "cyber_protocol_progress"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CyberProtocolApp(
                    onRunCode = { missionId, code, callback ->
                        lifecycleScope.launch {
                            val result = withContext(Dispatchers.IO) { runPythonMission(missionId, code) }
                            callback(result.first, result.second)
                        }
                    }
                )
            }
        }
    }

    private fun runPythonMission(missionId: Int, code: String): Pair<String, Boolean> = try {
        val raw = Python.getInstance().getModule("runner").callAttr("evaluate_mission", code, missionId).toString()
        val result = JSONObject(raw)
        val error = result.optString("error")
        val feedback = result.optString("feedback")
        val output = result.optString("output")
        val message = when {
            error.isNotBlank() -> error
            feedback.isNotBlank() -> "$feedback\nOutput (original log): $output"
            else -> output.ifBlank { "(no output)" }
        }
        message to result.optBoolean("passed")
    } catch (error: Exception) {
        (error.message ?: "Python runtime error") to false
    }
}

@Composable
private fun CyberProtocolApp(onRunCode: (Int, String, (String, Boolean) -> Unit) -> Unit) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(PREFS, Context.MODE_PRIVATE) }
    var xp by remember { mutableIntStateOf(prefs.getInt("xp", 0)) }
    var credits by remember { mutableIntStateOf(prefs.getInt("credits", 0)) }
    var completedMissions by remember {
        mutableStateOf((1..3).filter { mission ->
            prefs.getBoolean("mission_$mission", mission == 1 && prefs.getBoolean("chapter_01_mission_01", false))
        }.toSet())
    }
    var selectedMission by remember { mutableIntStateOf(1) }
    val missionTitle = when (selectedMission) {
        1 -> "01 — THE CORRUPTED ACCESS LOG"
        2 -> "02 — THE WARNING SIGNAL"
        else -> "03 — THE FAILED LOGIN TRAIL"
    }
    val missionObjective = when (selectedMission) {
        1 -> "OBJECTIVE: count every ERROR entry using Python."
        2 -> "OBJECTIVE: count every WARNING entry using Python."
        else -> "OBJECTIVE: count every FAILED login entry using Python."
    }
    val missionLog = when (selectedMission) {
        1 -> "INFO: service started\nERROR: invalid token\nWARNING: retry scheduled\nERROR: permission denied"
        2 -> "INFO: service started\nWARNING: retry scheduled\nERROR: invalid token\nWARNING: timeout"
        else -> "FAILED login user=admin\nOK login user=guest\nFAILED login user=root"
    }
    val missionHint = when (selectedMission) {
        1 -> "HINT: use open(), with, for, if, in, append(), and len()."
        2 -> "HINT: reuse the file-reading pattern. Look for the word WARNING."
        else -> "HINT: inspect each line for FAILED, then count matching entries."
    }
    fun starterCode(mission: Int) = when (mission) {
        1 -> "errors = []\n\n# Count every line containing ERROR\n\nprint(len(errors))"
        2 -> "warnings = []\n\n# Count every line containing WARNING\n\nprint(len(warnings))"
        else -> "failed = []\n\n# Count every line containing FAILED\n\nprint(len(failed))"
    }
    var editor by remember { mutableStateOf(TextFieldValue(starterCode(1))) }
    var output by remember { mutableStateOf("SYSTEM READY\nAwaiting analyst input...") }
    var status by remember { mutableStateOf("TRAINING SIMULATION  /  CASE OPEN") }
    var isRunning by remember { mutableStateOf(false) }
    var showBriefing by remember { mutableStateOf(true) }

    Column(
        Modifier.fillMaxSize().background(Bg).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("CYBER PROTOCOL", color = Neon, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Text("ZERO DAY  /  CHAPTER 01", color = Muted, fontSize = 10.sp, letterSpacing = 1.5.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("LEVEL ${xp / 250 + 1}", color = Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("$xp XP  •  $credits CR", color = Neon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            (1..3).forEach { mission ->
                val unlocked = mission == 1 || (mission - 1) in completedMissions
                val active = selectedMission == mission
                Text(
                    text = if (!unlocked) "🔒  M0$mission" else "M0$mission",
                    color = if (active) Bg else if (unlocked) Cyan else Muted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(if (active) Neon else PanelAlt, RoundedCornerShape(8.dp))
                        .clickable(enabled = unlocked && !isRunning) {
                            selectedMission = mission
                            editor = TextFieldValue(starterCode(mission))
                            output = "MISSION 0$mission READY\nWrite your solution and run the evidence tests."
                            status = if (mission in completedMissions) "MISSION VERIFIED  /  REPLAY AVAILABLE" else "TRAINING SIMULATION  /  CASE OPEN"
                        }
                        .padding(horizontal = 14.dp, vertical = 9.dp)
                )
            }
        }

        Column(
            Modifier.fillMaxWidth().weight(0.88f).background(Panel, RoundedCornerShape(14.dp))
                .border(1.dp, Color(0xFF243844), RoundedCornerShape(14.dp)).padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("●", color = Neon, fontSize = 10.sp)
                Text("  INCOMING CASE  /  HELIX DYNAMICS", color = Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text("OFFLINE LAB", color = Muted, fontSize = 9.sp)
            }
            Text(missionTitle, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(
                when (selectedMission) {
                    1 -> "Mara, Senior Security Analyst: “The night audit found suspicious access attempts. Recover the evidence before the trail disappears.”"
                    2 -> "Mara: “A monitoring node raised repeated warnings before the outage. Separate warnings from routine log noise.”"
                    else -> "Mara: “Someone tried to enter the admin console. Count failed logins and preserve the evidence for incident response.”"
                },
                color = Muted, fontSize = 11.sp, lineHeight = 16.sp
            )
            Box(
                Modifier.fillMaxWidth().weight(1f).background(Color(0xFF070B0F), RoundedCornerShape(9.dp)).padding(9.dp)
            ) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    Text("root@helix-node:~$ cat access.log", color = Neon, fontFamily = FontFamily.Monospace, fontSize = 10.sp)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        missionLog,
                        color = Color(0xFFB7C7D1), fontFamily = FontFamily.Monospace, fontSize = 10.sp, lineHeight = 15.sp
                    )
                    Spacer(Modifier.height(7.dp))
                    Text(missionObjective, color = Neon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(status, color = if (selectedMission in completedMissions) Neon else Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("BRIEFING", color = Cyan, fontSize = 9.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { showBriefing = true }.padding(4.dp))
            }
        }

        Column(
            Modifier.fillMaxWidth().weight(1.18f).background(Panel, RoundedCornerShape(14.dp))
                .border(1.dp, Color(0xFF243844), RoundedCornerShape(14.dp)).padding(11.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("PYTHON 3.13 EDITOR", color = Neon, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(Modifier.weight(1f))
                Text("main.py", color = Muted, fontSize = 9.sp)
            }
            Row(Modifier.weight(1f).fillMaxWidth()) {
                Column(
                    Modifier.width(27.dp).fillMaxHeight()
                        .background(Color(0xFF0A1117), RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                        .padding(top = 9.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(maxOf(1, editor.text.count { it == '\n' } + 1)) { line ->
                        Text((line + 1).toString(), color = Color(0xFF536775), fontFamily = FontFamily.Monospace, fontSize = 10.sp, lineHeight = 17.sp)
                    }
                }
                BasicTextField(
                    value = editor,
                    onValueChange = { newValue -> editor = applyAutoIndent(editor, newValue) },
                    modifier = Modifier.weight(1f).fillMaxHeight()
                        .background(Color(0xFF070B0F), RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                        .padding(9.dp).verticalScroll(rememberScrollState()),
                    textStyle = TextStyle(color = Color(0xFFD6E3EA), fontFamily = FontFamily.Monospace, fontSize = 11.sp, lineHeight = 17.sp),
                    cursorBrush = SolidColor(Neon),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrect = false),
                    visualTransformation = PythonHighlightTransformation,
                    decorationBox = { inner ->
                        Box {
                            if (editor.text.isEmpty()) Text("# Write your Python solution...", color = Muted, fontFamily = FontFamily.Monospace)
                            inner()
                        }
                    }
                )
            }
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                val shortcuts = listOf("()" to "()", "[]" to "[]", "{}" to "{}", ":" to ":", "Tab" to "    ", "print()" to "print()")
                shortcuts.forEach { (label, insert) ->
                    Text(
                        label, color = Cyan, fontFamily = FontFamily.Monospace, fontSize = 10.sp,
                        modifier = Modifier.background(PanelAlt, RoundedCornerShape(6.dp)).clickable {
                            val start = editor.selection.start
                            val end = editor.selection.end
                            val updated = editor.text.replaceRange(start, end, insert)
                            val cursor = if (insert in listOf("()", "[]", "{}")) start + 1 else start + insert.length
                            editor = TextFieldValue(updated, TextRange(cursor))
                        }.padding(horizontal = 10.dp, vertical = 8.dp)
                    )
                }
            }
            Button(
                onClick = {
                    isRunning = true
                    status = "EXECUTING PYTHON..."
                    val runningMission = selectedMission
                    onRunCode(runningMission, editor.text) { result, passed ->
                        output = result
                        if (passed && runningMission !in completedMissions) {
                            completedMissions = completedMissions + runningMission
                            xp += 100
                            credits += 50
                            prefs.edit()
                                .putBoolean("mission_$runningMission", true)
                                .putInt("xp", xp)
                                .putInt("credits", credits)
                                .apply()
                            status = "MISSION COMPLETE  /  +100 XP"
                        } else if (passed) {
                            status = "MISSION VERIFIED  /  REWARD ALREADY CLAIMED"
                        } else {
                            status = "CODE EXECUTED  /  OBJECTIVE NOT MET"
                        }
                        isRunning = false
                    }
                },
                enabled = !isRunning,
                modifier = Modifier.fillMaxWidth().height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Neon, contentColor = Bg),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (isRunning) "RUNNING..." else "▶  RUN MISSION", fontWeight = FontWeight.Black, letterSpacing = 1.sp, fontSize = 12.sp)
            }
        }

        Column(Modifier.fillMaxWidth().background(Color(0xFF05090C), RoundedCornerShape(10.dp)).padding(9.dp)) {
            Text("MISSION OUTPUT  /  LOCAL PYTHON", color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(output, color = if (output.contains("Error") || output.contains("limit")) Danger else Neon,
                fontSize = 10.sp, fontFamily = FontFamily.Monospace, lineHeight = 14.sp)
        }
    }

    if (showBriefing) {
        AlertDialog(
            onDismissRequest = { showBriefing = false },
            containerColor = Panel,
            title = { Text("MISSION BRIEFING", color = Neon, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("MARA / SENIOR SECURITY ANALYST", color = Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(
                        when (selectedMission) {
                            1 -> "“Read access.log and count every line containing ERROR. The test system will swap the evidence file to verify your logic.”"
                            2 -> "“Read access.log and count every line containing WARNING. Do not count ERROR or INFO entries.”"
                            else -> "“Read access.log and count every line containing FAILED. The evidence will include successful logins too.”"
                        },
                        color = Color.White, fontSize = 13.sp, lineHeight = 19.sp
                    )
                    Text(missionHint + " Your code is tested against three evidence files.", color = Muted, fontSize = 11.sp)
                    Text(if (selectedMission in completedMissions) "REWARD CLAIMED  •  REPLAY FOR PRACTICE" else "REWARD: 100 XP  •  50 CREDITS", color = Neon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(onClick = { showBriefing = false }, colors = ButtonDefaults.buttonColors(containerColor = Neon, contentColor = Bg)) {
                    Text("ACCEPT MISSION")
                }
            }
        )
    }
}

private fun applyAutoIndent(old: TextFieldValue, new: TextFieldValue): TextFieldValue {
    if (new.text.length != old.text.length + 1 || new.selection.start <= 0) return new
    val cursor = new.selection.start
    if (new.text.getOrNull(cursor - 1) != '\n') return new
    val previousLine = new.text.substring(0, cursor - 1).substringAfterLast('\n')
    val indent = previousLine.takeWhile { it == ' ' || it == '\t' }
    val extra = if (previousLine.trimEnd().endsWith(":")) "    " else ""
    val insertion = indent + extra
    return TextFieldValue(
        new.text.substring(0, cursor) + insertion + new.text.substring(cursor),
        TextRange(cursor + insertion.length)
    )
}

private val PythonHighlightTransformation = VisualTransformation { input ->
    val text = input.text
    val builder = AnnotatedString.Builder(text)
    val keywordStyle = SpanStyle(color = Color(0xFF79B8FF), fontWeight = FontWeight.Bold)
    val builtinStyle = SpanStyle(color = Color(0xFFFFC66D))
    val stringStyle = SpanStyle(color = Color(0xFFB5E890))
    val numberStyle = SpanStyle(color = Color(0xFFD7A8FF))
    val commentStyle = SpanStyle(color = Color(0xFF718493), fontWeight = FontWeight.Normal)

    Regex("\"[^\"]*\"|'[^']*'").findAll(text).forEach {
        builder.addStyle(stringStyle, it.range.first, it.range.last + 1)
    }
    Regex("\\b(def|class|return|if|elif|else|for|while|in|not|and|or|is|with|as|try|except|finally|import|from|pass|break|continue|True|False|None)\\b")
        .findAll(text).forEach { builder.addStyle(keywordStyle, it.range.first, it.range.last + 1) }
    Regex("\\b(print|len|open|range|str|int|float|bool|list|dict|enumerate|sorted|sum|min|max)\\b")
        .findAll(text).forEach { builder.addStyle(builtinStyle, it.range.first, it.range.last + 1) }
    Regex("\\b\\d+(\\.\\d+)?\\b").findAll(text).forEach {
        builder.addStyle(numberStyle, it.range.first, it.range.last + 1)
    }
    Regex("#[^\n]*").findAll(text).forEach {
        builder.addStyle(commentStyle, it.range.first, it.range.last + 1)
    }
    TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
}
