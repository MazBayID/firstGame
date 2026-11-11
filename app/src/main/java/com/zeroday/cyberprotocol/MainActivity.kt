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
        mutableStateOf((1..8).filter { mission ->
            prefs.getBoolean("mission_$mission", mission == 1 && prefs.getBoolean("chapter_01_mission_01", false))
        }.toSet())
    }
    var selectedMission by remember { mutableIntStateOf(1) }
    var language by remember { mutableStateOf(prefs.getString("language", "id") ?: "id") }
    var screen by remember { mutableStateOf("home") }
    val missionTitles = listOf("FIRST CONTACT", "SIGNAL ONLINE", "ANALYST IDENTITY", "POWER CALIBRATION", "REPAIR THE SHIELD", "POWER DECISION", "SIGNAL SWEEP", "THREAT TRIAGE")
    val missionConcepts = listOf("print() dan output pertama", "Beberapa print() dan komentar", "Variabel dan string", "Angka dan operator matematika", "Variabel dan operasi matematika", "if, else, dan perbandingan", "for dan range()", "List, for, if, dan counter")
    val missionObjectives = listOf(
        "Kirim pesan pertamamu ke terminal.", "Tampilkan status sistem dalam dua baris.",
        "Simpan identitas analis ke variabel lalu tampilkan.", "Hitung hasil kalibrasi daya: 6 × 7.",
        "Hitung HP setelah menerima 25 damage dari 100 HP.", "Aktifkan sistem hanya jika energi lebih dari nol.",
        "Jalankan empat tahap pemindaian menggunakan loop.", "Hitung jumlah event ERROR dari daftar bukti."
    )
    val missionTitle = "M%02d — %s".format(selectedMission, missionTitles[selectedMission - 1])
    val missionObjective = "LEVEL $selectedMission · ${missionConcepts[selectedMission - 1]} — ${missionObjectives[selectedMission - 1]}"
    val missionLog = when (selectedMission) {
        1 -> "Expected output:\nHello, Cyber Protocol!"
        2 -> "Expected output:\nSYSTEM ONLINE\nACCESS GRANTED"
        3 -> "Expected output:\nMARA\nONLINE"
        4 -> "Expected output:\n42"
        5 -> "Expected output:\nHP: 75"
        6 -> "energy = 10 → SYSTEM ACTIVE\nenergy = 0 → SYSTEM STANDBY"
        7 -> "Expected output:\nSCAN 1\nSCAN 2\nSCAN 3\nSCAN 4"
        else -> "Evidence events:\nINFO: boot\nERROR: token\nWARNING: retry\nERROR: denied"
    }
    val missionHint = listOf(
        "Gunakan print() dan teks di dalam tanda kutip.",
        "Panggil print() dua kali. Baris yang diawali # adalah komentar.",
        "Buat variabel agent dan status, lalu cetak keduanya.",
        "Gunakan operator * untuk perkalian.",
        "Simpan 100 ke hp, 25 ke damage, lalu kurangi hp.",
        "Periksa energy > 0. Gunakan else untuk kondisi selain itu.",
        "Gunakan for step in range(1, 5), lalu cetak SCAN dan step.",
        "Mulai count dari 0. Telusuri events dan tambah count jika ERROR ditemukan."
    )[selectedMission - 1]
    fun starterCode(mission: Int) = when (mission) {
        1 -> "print(\"Hello, Cyber Protocol!\")"
        2 -> "# Boot sequence\nprint(\"SYSTEM ONLINE\")\nprint(\"ACCESS GRANTED\")"
        3 -> "agent = \"MARA\"\nstatus = \"ONLINE\"\n\nprint(agent)\nprint(status)"
        4 -> "print(6 * 7)"
        5 -> "hp = 100\ndamage = 25\nhp = hp - damage\n\nprint(\"HP:\", hp)"
        6 -> "if energy > 0:\n    print(\"SYSTEM ACTIVE\")\nelse:\n    print(\"SYSTEM STANDBY\")"
        7 -> "for step in range(1, 5):\n    print(\"SCAN\", step)"
        else -> "count = 0\n\nfor event in events:\n    if \"ERROR\" in event:\n        count += 1\n\nprint(count)"
    }
    var editor by remember { mutableStateOf(TextFieldValue(starterCode(1))) }
    var output by remember { mutableStateOf(localized(language, "SYSTEM READY\nAwaiting analyst input...", "SISTEM SIAP\nMenunggu input analis...")) }
    var status by remember { mutableStateOf(localized(language, "TRAINING SIMULATION  /  CASE OPEN", "SIMULASI LATIHAN  /  KASUS TERBUKA")) }
    var isRunning by remember { mutableStateOf(false) }
    var showBriefing by remember { mutableStateOf(true) }
    var showLesson by remember { mutableStateOf(false) }

    if (screen == "home") {
        Column(Modifier.fillMaxSize().background(Bg).padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("CYBER", color = Neon, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 7.sp)
            Text("PROTOCOL", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
            Spacer(Modifier.height(8.dp))
            Text(localized(language, "HACKER RPG  /  CODING SIMULATION", "HACKER RPG  /  SIMULASI PEMROGRAMAN"), color = Cyan, fontSize = 10.sp, letterSpacing = 1.sp)
            Spacer(Modifier.height(38.dp))
            Button(onClick = { screen = "game"; showBriefing = true }, modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = Neon, contentColor = Bg), shape = RoundedCornerShape(10.dp)) {
                Text(localized(language, "▶  START CAMPAIGN", "▶  MULAI KAMPANYE"), fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.height(10.dp))
            Button(onClick = { screen = "settings" }, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = PanelAlt, contentColor = Cyan), shape = RoundedCornerShape(10.dp)) {
                Text(localized(language, "SETTINGS", "PENGATURAN"), fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(24.dp))
            Text("CHAPTER 01  •  OFFLINE TRAINING LAB", color = Muted, fontSize = 9.sp)
            Text("v0.4.0  •  PYTHON 3.13", color = Muted, fontSize = 9.sp)
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
            (1..8).forEach { mission ->
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
                            output = localized(language, "MISSION 0$mission READY\nWrite your solution and run the evidence tests.", "MISI 0$mission SIAP\nTulis solusi dan jalankan pengujian bukti.")
                            status = if (mission in completedMissions) localized(language, "MISSION VERIFIED  /  REPLAY AVAILABLE", "MISI TERVERIFIKASI  /  BISA DIULANGI") else localized(language, "TRAINING SIMULATION  /  CASE OPEN", "SIMULASI LATIHAN  /  KASUS TERBUKA")
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
                    Text("root@helix-node:~$ python lesson.py", color = Neon, fontFamily = FontFamily.Monospace, fontSize = 10.sp)
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
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(localized(language, "LEARN", "BELAJAR"), color = Neon, fontSize = 9.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { showLesson = true }.padding(4.dp))
                    Text(localized(language, "BRIEFING", "BRIEFING"), color = Cyan, fontSize = 9.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { showBriefing = true }.padding(4.dp))
                }
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
                    status = localized(language, "EXECUTING PYTHON...", "MENJALANKAN PYTHON...")
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
                            status = localized(language, "MISSION COMPLETE  /  +100 XP", "MISI SELESAI  /  +100 XP")
                        } else if (passed) {
                            status = localized(language, "MISSION VERIFIED  /  REWARD ALREADY CLAIMED", "MISI TERVERIFIKASI  /  HADIAH SUDAH DIAMBIL")
                        } else {
                            status = localized(language, "CODE EXECUTED  /  OBJECTIVE NOT MET", "KODE DIJALANKAN  /  TUJUAN BELUM TERCAPAI")
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
                        localized(language,
                            "“We will build your Python skills one step at a time. Learn the concept, inspect the example, experiment with it, then solve the case. No previous coding experience is required.”",
                            "“Kita akan membangun kemampuan Python-mu tahap demi tahap. Pelajari konsepnya, amati contoh, bereksperimen, lalu selesaikan kasus. Kamu tidak perlu pengalaman coding sebelumnya.”"),
                        color = Color.White, fontSize = 13.sp, lineHeight = 19.sp
                    )
                    Text(missionHint, color = Muted, fontSize = 11.sp, lineHeight = 16.sp)
                    Text(if (selectedMission in completedMissions) localized(language, "REWARD CLAIMED  •  REPLAY FOR PRACTICE", "HADIAH SUDAH DIAMBIL  •  ULANGI UNTUK LATIHAN") else localized(language, "REWARD: 100 XP  •  50 CREDITS", "HADIAH: 100 XP  •  50 KREDIT"), color = Neon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(onClick = { showBriefing = false; showLesson = true }, colors = ButtonDefaults.buttonColors(containerColor = Neon, contentColor = Bg)) {
                    Text(localized(language, "CONTINUE TO LESSON", "LANJUT KE MATERI"))
                }
            }
        )
    }
    if (showLesson && screen == "game") {
        val lesson = when (selectedMission) {
            1 -> localized(language, "LEARN · print()\n\nprint() sends information to the output terminal. Text must be wrapped in quotes.\n\nDEMONSTRATION\nprint(\"Hello, Cyber Protocol!\")\n\nOUTPUT\nHello, Cyber Protocol!\n\nEXPERIMENT\nChange the message inside the quotes, run it, and observe the output. Then restore the mission message.", "BELAJAR · print()\n\nprint() mengirim informasi ke terminal output. Teks harus berada di dalam tanda kutip.\n\nDEMONSTRASI\nprint(\"Hello, Cyber Protocol!\")\n\nHASIL\nHello, Cyber Protocol!\n\nEKSPERIMEN\nUbah pesan di dalam tanda kutip, jalankan, dan amati hasilnya. Setelah itu kembalikan pesan sesuai misi.")
            2 -> localized(language, "LEARN · Multiple output and comments\n\nEach print() creates a new output line. A comment starts with # and is ignored by Python.\n\nDEMONSTRATION\n# Start the system\nprint(\"SYSTEM ONLINE\")\nprint(\"ACCESS GRANTED\")\n\nEXPERIMENT\nAdd another print() line, then add a comment.", "BELAJAR · Beberapa output dan komentar\n\nSetiap print() membuat baris output baru. Komentar diawali # dan diabaikan Python.\n\nDEMONSTRASI\n# Menyalakan sistem\nprint(\"SYSTEM ONLINE\")\nprint(\"ACCESS GRANTED\")\n\nEKSPERIMEN\nTambahkan satu print() lagi, lalu tambahkan komentar.")
            3 -> localized(language, "LEARN · Variables\n\nA variable is a name that refers to a value. Use = to assign a value, then use its name to read it. Text values use quotes.\n\nDEMONSTRATION\nagent = \"MARA\"\nstatus = \"ONLINE\"\nprint(agent)\nprint(status)\n\nEXPERIMENT\nChange ONLINE to OFFLINE and run again.", "BELAJAR · Variabel\n\nVariabel adalah nama yang merujuk pada nilai. Gunakan = untuk menyimpan nilai, lalu gunakan nama variabel untuk membacanya. Teks memakai tanda kutip.\n\nDEMONSTRASI\nagent = \"MARA\"\nstatus = \"ONLINE\"\nprint(agent)\nprint(status)\n\nEKSPERIMEN\nUbah ONLINE menjadi OFFLINE lalu jalankan.")
            4 -> localized(language, "LEARN · Numbers and operators\n\n+ addition, - subtraction, * multiplication, / division.\n\nDEMONSTRATION\nprint(6 * 7)\n\nOUTPUT\n42\n\nEXPERIMENT\nTry 6 + 7, 6 - 7, and 6 / 7. Restore 6 * 7.", "BELAJAR · Angka dan operator\n\n+ penjumlahan, - pengurangan, * perkalian, / pembagian.\n\nDEMONSTRASI\nprint(6 * 7)\n\nHASIL\n42\n\nEKSPERIMEN\nCoba 6 + 7, 6 - 7, dan 6 / 7. Kembalikan 6 * 7.")
            5 -> localized(language, "LEARN · Variables plus arithmetic\n\nVariables can hold numbers. Calculate a new value and assign it back. print(\"HP:\", hp) prints text and a value.\n\nDEMONSTRATION\nhp = 100\ndamage = 25\nhp = hp - damage\nprint(\"HP:\", hp)\n\nEXPERIMENT\nChange damage to 40 and predict the result.", "BELAJAR · Variabel dan matematika\n\nVariabel dapat menyimpan angka. Hitung nilai baru lalu simpan kembali. print(\"HP:\", hp) mencetak teks dan nilai.\n\nDEMONSTRASI\nhp = 100\ndamage = 25\nhp = hp - damage\nprint(\"HP:\", hp)\n\nEKSPERIMEN\nUbah damage menjadi 40 dan prediksi hasilnya.")
            6 -> localized(language, "LEARN · if and else\n\nA condition chooses which code runs. > means greater than. The indented block after if runs when true; else handles the other case.\n\nDEMONSTRATION\nif energy > 0:\n    print(\"SYSTEM ACTIVE\")\nelse:\n    print(\"SYSTEM STANDBY\")\n\nEXPERIMENT\nThe lab tests energy as 10, 0, and -5.", "BELAJAR · if dan else\n\nKondisi menentukan kode yang dijalankan. > berarti lebih besar dari. Blok setelah if berjalan saat benar; else menangani kondisi lainnya.\n\nDEMONSTRASI\nif energy > 0:\n    print(\"SYSTEM ACTIVE\")\nelse:\n    print(\"SYSTEM STANDBY\")\n\nEKSPERIMEN\nLab menguji energy dengan nilai 10, 0, dan -5.")
            7 -> localized(language, "LEARN · for and range()\n\nA for loop repeats a block. range(1, 5) produces 1, 2, 3, 4; the ending number is not included.\n\nDEMONSTRATION\nfor step in range(1, 5):\n    print(\"SCAN\", step)\n\nEXPERIMENT\nChange the range to range(1, 4), then restore it.", "BELAJAR · for dan range()\n\nLoop for mengulang blok kode. range(1, 5) menghasilkan 1, 2, 3, 4; angka akhir tidak ikut.\n\nDEMONSTRASI\nfor step in range(1, 5):\n    print(\"SCAN\", step)\n\nEKSPERIMEN\nUbah range menjadi range(1, 4), lalu kembalikan.")
            else -> localized(language, "LEARN · Lists, loops, conditions, and counters\n\nA list stores multiple values. for visits each item. if filters matching items. count += 1 adds one to the counter. The lab supplies different events in every test.\n\nDEMONSTRATION\ncount = 0\nfor event in events:\n    if \"ERROR\" in event:\n        count += 1\nprint(count)\n\nEXPERIMENT\nChange ERROR to WARNING and observe the count.", "BELAJAR · List, loop, kondisi, dan counter\n\nList menyimpan banyak nilai. for mengunjungi setiap item. if menyaring item yang cocok. count += 1 menambah counter satu angka. Lab memberikan daftar event berbeda pada setiap pengujian.\n\nDEMONSTRASI\ncount = 0\nfor event in events:\n    if \"ERROR\" in event:\n        count += 1\nprint(count)\n\nEKSPERIMEN\nUbah ERROR menjadi WARNING dan amati jumlahnya.")
        }
        AlertDialog(
            onDismissRequest = { showLesson = false },
            containerColor = Panel,
            title = { Text(localized(language, "HYBRID LEARNING  /  LESSON $selectedMission", "HYBRID LEARNING  /  MATERI $selectedMission"), color = Neon, fontWeight = FontWeight.Bold) },
            text = { Text(lesson, color = Color.White, fontSize = 12.sp, lineHeight = 18.sp) },
            confirmButton = {
                Button(onClick = { showLesson = false }, colors = ButtonDefaults.buttonColors(containerColor = Neon, contentColor = Bg)) {
                    Text(localized(language, "START PRACTICE", "MULAI LATIHAN"))
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
