package com.zeroday.cyberprotocol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.chaquo.python.Python
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

private val Bg = Color(0xFF080D12)
private val Panel = Color(0xFF111B23)
private val PanelAlt = Color(0xFF17232D)
private val Neon = Color(0xFF52F2A5)
private val Cyan = Color(0xFF55C8E8)
private val Muted = Color(0xFF91A3B2)
private val Danger = Color(0xFFFF7777)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CyberProtocolApp { code, callback ->
                    lifecycleScope.launch {
                        val result = withContext(Dispatchers.IO) { runPythonMission(code) }
                        callback(result.first, result.second)
                    }
                }
            }
        }
    }

    private fun runPythonMission(code: String): Pair<String, Boolean> = try {
        val raw = Python.getInstance().getModule("runner")
            .callAttr("run_code", code,
                "INFO: service started\nERROR: invalid token\nWARNING: retry scheduled\nERROR: permission denied\n")
            .toString()
        val result = JSONObject(raw)
        val output = result.optString("output")
        val error = result.optString("error")
        (if (error.isNotBlank()) error else output.ifBlank { "(no output)" }) to result.optBoolean("passed")
    } catch (error: Exception) {
        (error.message ?: "Python runtime error") to false
    }
}

@Composable
private fun CyberProtocolApp(onRunCode: (String, (String, Boolean) -> Unit) -> Unit) {
    var editor by remember {
        mutableStateOf(TextFieldValue("errors = []\n\n# TODO: read access.log and collect ERROR lines\n\nprint(len(errors))"))
    }
    var output by remember { mutableStateOf("SYSTEM READY\nAwaiting analyst input...") }
    var status by remember { mutableStateOf("TRAINING SIMULATION") }
    var isRunning by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().background(Bg).padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("CYBER PROTOCOL", color = Neon, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Text("ZERO DAY  /  BUILD 0.1.0", color = Muted, fontSize = 10.sp, letterSpacing = 1.5.sp)
            }
            Text("LVL 01  •  0 XP", color = Cyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        Column(
            Modifier.fillMaxWidth().weight(0.88f).background(Panel, RoundedCornerShape(14.dp))
                .border(1.dp, Color(0xFF243844), RoundedCornerShape(14.dp)).padding(13.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("●", color = Neon, fontSize = 10.sp)
                Text("  INCOMING CASE  /  CHAPTER 01", color = Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text("OFFLINE LAB", color = Muted, fontSize = 9.sp)
            }
            Text("THE CORRUPTED ACCESS LOG", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text(
                "A routine audit at Helix Dynamics uncovered a damaged access log. Two entries contain unauthorized activity. Recover the evidence before the trail disappears.",
                color = Muted, fontSize = 12.sp, lineHeight = 18.sp
            )
            Box(Modifier.fillMaxWidth().weight(1f).background(Color(0xFF070B0F), RoundedCornerShape(9.dp)).padding(10.dp)) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    Text("root@helix-node:~$ tail -n 4 access.log", color = Neon, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                    Spacer(Modifier.height(7.dp))
                    Text(
                        "INFO: service started\nERROR: invalid token\nWARNING: retry scheduled\nERROR: permission denied",
                        color = Color(0xFFB7C7D1), fontFamily = FontFamily.Monospace, fontSize = 11.sp, lineHeight = 17.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("> OBJECTIVE: count every ERROR entry", color = Neon, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                }
            }
            Text(status, color = if (status.contains("SUCCESS")) Neon else Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }

        Column(
            Modifier.fillMaxWidth().weight(1.15f).background(Panel, RoundedCornerShape(14.dp))
                .border(1.dp, Color(0xFF243844), RoundedCornerShape(14.dp)).padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("PYTHON EDITOR", color = Neon, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                Spacer(Modifier.weight(1f))
                Text("main.py", color = Muted, fontSize = 10.sp)
            }
            BasicTextField(
                value = editor, onValueChange = { editor = it },
                modifier = Modifier.fillMaxWidth().weight(1f)
                    .background(Color(0xFF070B0F), RoundedCornerShape(8.dp)).padding(10.dp)
                    .verticalScroll(rememberScrollState()),
                textStyle = TextStyle(color = Color(0xFFD6E3EA), fontFamily = FontFamily.Monospace, fontSize = 12.sp, lineHeight = 18.sp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrect = false),
                decorationBox = { inner ->
                    Box {
                        if (editor.text.isEmpty()) Text("# Write your Python solution...", color = Muted, fontFamily = FontFamily.Monospace)
                        inner()
                    }
                }
            )
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(":", "()", "[]", "{}", "=", "Tab").forEach { token ->
                    Text(token, color = Cyan, fontFamily = FontFamily.Monospace, fontSize = 12.sp,
                        modifier = Modifier.background(PanelAlt, RoundedCornerShape(6.dp)).clickable {
                            val insertion = if (token == "Tab") "    " else token
                            val start = editor.selection.start
                            val end = editor.selection.end
                            val updated = editor.text.replaceRange(start, end, insertion)
                            editor = TextFieldValue(updated, TextRange(start + insertion.length))
                        }.padding(horizontal = 12.dp, vertical = 8.dp))
                }
            }
            Button(
                onClick = {
                    isRunning = true
                    status = "EXECUTING PYTHON..."
                    onRunCode(editor.text) { text, passed ->
                        output = text
                        status = if (passed) "MISSION SUCCESS  /  +100 XP" else "CODE EXECUTED  /  OBJECTIVE NOT MET"
                        isRunning = false
                    }
                },
                enabled = !isRunning, modifier = Modifier.fillMaxWidth().height(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Neon, contentColor = Bg),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (isRunning) "RUNNING..." else "▶  RUN CODE", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            }
        }

        Column(Modifier.fillMaxWidth().background(Color(0xFF05090C), RoundedCornerShape(10.dp)).padding(10.dp)) {
            Text("OUTPUT  /  PYTHON 3.13", color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(output, color = if (output.contains("Error")) Danger else Neon,
                fontSize = 11.sp, fontFamily = FontFamily.Monospace, lineHeight = 15.sp)
        }
    }
}
