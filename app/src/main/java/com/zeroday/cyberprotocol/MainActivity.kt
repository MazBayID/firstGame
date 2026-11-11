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

private fun localized(language: String, english: String, indonesian: String): String =
    if (language == "id") indonesian else english

private fun moveCursor(value: TextFieldValue, direction: String): TextFieldValue {
    val text = value.text
    val cursor = value.selection.start.coerceIn(0, text.length)
    val next = when (direction) {
        "left" -> (cursor - 1).coerceAtLeast(0)
        "right" -> (cursor + 1).coerceAtMost(text.length)
        "home" -> text.lastIndexOf('\n', (cursor - 1).coerceAtLeast(0)).let { it + 1 }
        "end" -> text.indexOf('\n', cursor).let { if (it == -1) text.length else it }
        "up" -> {
            val start = text.lastIndexOf('\n', (cursor - 1).coerceAtLeast(0)) + 1
            val column = cursor - start
            if (start == 0) cursor else {
                val end = start - 1
                val previousStart = text.lastIndexOf('\n', (end - 1).coerceAtLeast(0)) + 1
                (previousStart + column).coerceAtMost(end)
            }
        }
        "down" -> {
            val end = text.indexOf('\n', cursor).let { if (it == -1) text.length else it }
            val column = cursor - (text.lastIndexOf('\n', (cursor - 1).coerceAtLeast(0)) + 1)
            if (end == text.length) cursor else {
                val start = end + 1
                val nextEnd = text.indexOf('\n', start).let { if (it == -1) text.length else it }
                (start + column).coerceAtMost(nextEnd)
            }
        }
        else -> cursor
    }
    return value.copy(selection = TextRange(next))
}

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
    var language by remember { mutableStateOf(prefs.getString("language", "id") ?: "id") }
    var screen by remember { mutableStateOf("home") }
    val missionTitle = when (selectedMission) {
        1 -> localized(language, "01 — THE CORRUPTED ACCESS LOG", "01 — LOG AKSES TERCEMAR")
        2 -> localized(language, "02 — THE WARNING SIGNAL", "02 — SINYAL PERINGATAN")
        else -> localized(language, "03 — THE FAILED LOGIN TRAIL", "03 — JEJAK LOGIN GAGAL")
    }
    val missionObjective = when (selectedMission) {
        1 -> localized(language, "OBJECTIVE: count every ERROR entry using Python.", "TUJUAN: hitung setiap entri ERROR menggunakan Python.")
        2 -> localized(language, "OBJECTIVE: count every WARNING entry using Python.", "TUJUAN: hitung setiap entri WARNING menggunakan Python.")
        else -> localized(language, "OBJECTIVE: count every FAILED login entry using Python.", "TUJUAN: hitung setiap entri login FAILED menggunakan Python.")
    }
    val missionLog = when (selectedMission) {
        1 -> "INFO: service started\nERROR: invalid token\nWARNING: retry scheduled\nERROR: permission denied"
        2 -> "INFO: service started\nWARNING: retry scheduled\nERROR: invalid token\nWARNING: timeout"
        else -> "FAILED login user=admin\nOK login user=guest\nFAILED login user=root"
    }
    val missionHint = when (selectedMission) {
        1 -> localized(language, "HINT: use open(), with, for, if, in, append(), and len().", "PETUNJUK: gunakan open(), with, for, if, in, append(), dan len().")
        2 -> localized(language, "HINT: reuse the file-reading pattern. Look for the word WARNING.", "PETUNJUK: gunakan kembali pola membaca file. Cari kata WARNING.")
        else -> localized(language, "HINT: inspect each line for FAILED, then count matching entries.", "PETUNJUK: periksa setiap baris untuk FAILED, lalu hitung yang cocok.")
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

    if (screen == "home") {
        Column(Modifier.fillMaxSize().background(Bg).padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("CYBER", color = Neon, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 7.sp)
            Text("PROTOCOL", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
            Spacer(Modifier.height(8.dp))
            Text(localized(language, "HACKER RPG  /  CODING SIMULATION", "HACKER RPG  /  SIMULASI PEMROGRAMAN"), color = Cyan, fontSize = 10.sp, letterSpacing = 1.sp)
            Spacer(Modifier.height(38.dp))
            Button(onClick = { screen = "game"; showBriefing = true }, modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = Neon, contentColor = Bg), shape = RoundedCornerShape(10.dp)) {
                Text(localized(language, "▶  CONTINUE / NEW GAME", "▶  LANJUTKAN / MULAI GAME"), fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.height(10.dp))
            Button(onClick = { screen = "settings" }, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = PanelAlt, contentColor = Cyan), shape = RoundedCornerShape(10.dp)) {
                Text(localized(language, "SETTINGS", "PENGATURAN"), fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(24.dp))
            Text("CHAPTER 01  •  OFFLINE TRAINING LAB", color = Muted, fontSize = 9.sp)
            Text("v0.2.0  •  PYTHON 3.13", color = Muted, fontSize = 9.sp)
        }
    } else if (screen == "settings") {
        Column(Modifier.fillMaxSize().background(Bg).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(localized(language, "SETTINGS", "PENGATURAN"), color = Neon, fontSize = 22.sp, fontWeight = FontWeight.Black)
            Text(localized(language, "GAME LANGUAGE", "BAHASA GAME"), color = Cyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { language = "id"; prefs.edit().putString("language", "id").apply() }, colors = ButtonDefaults.buttonColors(containerColor = if (language == "id") Neon else PanelAlt, contentColor = if (language == "id") Bg else Color.White)) { Text("🇮🇩  Indonesia") }
                Button(onClick = { language = "en"; prefs.edit().putString("language", "en").apply() }, colors = ButtonDefaults.buttonColors(containerColor = if (language == "en") Neon else PanelAlt, contentColor = if (language == "en") Bg else Color.White)) { Text("🇬🇧  English") }
            }
            Text(localized(language, "Language preference is saved on this device.", "Pilihan bahasa disimpan di perangkat ini."), color = Muted, fontSize = 11.sp)
            Spacer(Modifier.weight(1f))
            Button(onClick = { screen = "home" }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Neon, contentColor = Bg)) {
                Text(localized(language, "BACK TO HOME", "KEMBALI KE MENU UTAMA"))
            }
        }
    } else {
    Column(
        Modifier.fillMaxSize().background(Bg).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("‹", color = Cyan, fontSize = 28.sp, modifier = Modifier.clickable { screen = "home" }.padding(end = 10.dp))
            Column(Modifier.weight(1f)) {
                Text("CYBER PROTOCOL", color = Neon, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Text(localized(language, "ZERO DAY  /  CHAPTER 01", "ZERO DAY  /  BAB 01"), color = Muted, fontSize = 10.sp, letterSpacing = 1.5.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(localized(language, "LEVEL ${xp / 250 + 1}", "LEVEL ${xp / 250 + 1}"), color = Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                Text(localized(language, "  INCOMING CASE  /  HELIX DYNAMICS", "  KASUS MASUK  /  HELIX DYNAMICS"), color = Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text(localized(language, "OFFLINE LAB", "LAB OFFLINE"), color = Muted, fontSize = 9.sp)
            }
            Text(missionTitle, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(
                when (selectedMission) {
                    1 -> localized(language, "Mara, Senior Security Analyst: “The night audit found suspicious access attempts. Recover the evidence before the trail disappears.”", "Mara, Analis Keamanan Senior: “Audit malam menemukan upaya akses mencurigakan. Amankan buktinya sebelum jejaknya hilang.”")
                    2 -> localized(language, "Mara: “A monitoring node raised repeated warnings before the outage. Separate warnings from routine log noise.”", "Mara: “Node pemantau mengirim peringatan berulang sebelum gangguan terjadi. Pisahkan peringatan dari log rutin.”")
                    else -> localized(language, "Mara: “Someone tried to enter the admin console. Count failed logins and preserve the evidence for incident response.”", "Mara: “Seseorang mencoba masuk ke konsol admin. Hitung login yang gagal dan simpan bukti untuk penanganan insiden.”")
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
                Text(localized(language, "BRIEFING", "BRIEFING"), color = Cyan, fontSize = 9.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { showBriefing = true }.padding(4.dp))
            }
        }

        Column(
            Modifier.fillMaxWidth().weight(1.18f).background(Panel, RoundedCornerShape(14.dp))
                .border(1.dp, Color(0xFF243844), RoundedCornerShape(14.dp)).padding(11.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(localized(language, "PYTHON 3.13 EDITOR", "EDITOR PYTHON 3.13"), color = Neon, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
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
                            if (editor.text.isEmpty()) Text(localized(language, "# Write your Python solution...", "# Tulis solusi Python-mu..."), color = Muted, fontFamily = FontFamily.Monospace)
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

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
                listOf("left", "up", "down", "right", "home", "end").forEach { direction ->
                    val label = when (direction) { "left" -> "←"; "right" -> "→"; "up" -> "↑"; "down" -> "↓"; "home" -> "HOME"; else -> "END" }
                    Text(label, color = Neon, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(if (direction == "home" || direction == "end") 1.4f else 1f)
                            .background(PanelAlt, RoundedCornerShape(6.dp))
                            .clickable { editor = moveCursor(editor, direction) }
                            .padding(vertical = 9.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center)
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
                Text(if (isRunning) localized(language, "RUNNING...", "MENJALANKAN...") else localized(language, "▶  RUN MISSION", "▶  JALANKAN MISI"), fontWeight = FontWeight.Black, letterSpacing = 1.sp, fontSize = 12.sp)
            }
        }

        Column(Modifier.fillMaxWidth().background(Color(0xFF05090C), RoundedCornerShape(10.dp)).padding(9.dp)) {
            Text(localized(language, "MISSION OUTPUT  /  LOCAL PYTHON", "HASIL MISI  /  PYTHON LOKAL"), color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(output, color = if (output.contains("Error") || output.contains("limit")) Danger else Neon,
                fontSize = 10.sp, fontFamily = FontFamily.Monospace, lineHeight = 14.sp)
        }
    }

    }
    if (showBriefing && screen == "game") {
        AlertDialog(
            onDismissRequest = { showBriefing = false },
            containerColor = Panel,
            title = { Text(localized(language, "MISSION BRIEFING", "BRIEFING MISI"), color = Neon, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(localized(language, "MARA / SENIOR SECURITY ANALYST", "MARA / ANALIS KEAMANAN SENIOR"), color = Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(
                        when (selectedMission) {
                            1 -> localized(language, "“Read access.log and count every line containing ERROR. The test system will swap the evidence file to verify your logic.”", "“Baca access.log dan hitung setiap baris yang berisi ERROR. Sistem pengujian akan mengganti file bukti untuk memverifikasi logikamu.”")
                            2 -> localized(language, "“Read access.log and count every line containing WARNING. Do not count ERROR or INFO entries.”", "“Baca access.log dan hitung setiap baris yang berisi WARNING. Jangan hitung entri ERROR atau INFO.”")
                            else -> localized(language, "“Read access.log and count every line containing FAILED. The evidence will include successful logins too.”", "“Baca access.log dan hitung setiap baris yang berisi FAILED. Bukti juga akan memuat login yang berhasil.”")
                        },
                        color = Color.White, fontSize = 13.sp, lineHeight = 19.sp
                    )
                    Text(missionHint + localized(language, " Your code is tested against three evidence files.", " Kode diuji pada tiga file bukti yang berbeda."), color = Muted, fontSize = 11.sp)
                    Text(if (selectedMission in completedMissions) localized(language, "REWARD CLAIMED  •  REPLAY FOR PRACTICE", "HADIAH SUDAH DIAMBIL  •  ULANGI UNTUK LATIHAN") else localized(language, "REWARD: 100 XP  •  50 CREDITS", "HADIAH: 100 XP  •  50 KREDIT"), color = Neon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(onClick = { showBriefing = false }, colors = ButtonDefaults.buttonColors(containerColor = Neon, contentColor = Bg)) {
                    Text(localized(language, "ACCEPT MISSION", "MULAI MISI"))
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
