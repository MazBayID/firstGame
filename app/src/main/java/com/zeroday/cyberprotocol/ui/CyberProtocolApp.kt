package com.zeroday.cyberprotocol.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroday.cyberprotocol.core.model.Mission
import com.zeroday.cyberprotocol.data.ContentRepository
import com.zeroday.cyberprotocol.data.ProgressRepository
import com.zeroday.cyberprotocol.core.model.StoryEngine
import com.zeroday.cyberprotocol.execution.PythonExecutionService
import com.zeroday.cyberprotocol.ui.theme.CyberPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private enum class Screen { HOME, LEARN, PRACTICE, MISSIONS, MISSION, SETTINGS }
private enum class UiLanguage(val code: String, val label: String) {
    EN("en", "English"), ID("id", "Bahasa Indonesia")
}

private fun tr(key: String, language: UiLanguage): String {
    if (language == UiLanguage.EN) return when (key) {
        "learn" -> "LEARN"; "practice" -> "PRACTICE"; "missions" -> "MISSION BOARD"
        "settings" -> "SETTINGS"; "back" -> "BACK"; "home_sub" -> "CYBER INTELLIGENCE RPG  /  PYTHON TRAINING"
        "learn_sub" -> "Understand the concepts before using them."
        "practice_sub" -> "Experiment, run, observe, and repair."
        "mission_sub" -> "Choose an available assignment."
        "language" -> "INTERFACE LANGUAGE"; "language_note" -> "Story and lesson text currently follows the chapter's authored language."
        "run_practice" -> "RUN PRACTICE"; "run" -> "RUN  ·  EXECUTE ONLY"
        "submit" -> "SUBMIT  ·  MISSION CHECK"; "editor" -> "Python code editor"
        "practice_editor" -> "Python practice editor"; "hint" -> "REQUEST HINT"
        "executed" -> "Executed only. No mission evaluation was performed."
        "run_done" -> "RUN completed. The mission has not been evaluated."
        "objective" -> "OBJECTIVE"; "loading" -> "RUNNING..."
        else -> key
    }
    return when (key) {
        "learn" -> "BELAJAR"; "practice" -> "LATIHAN"; "missions" -> "PAPAN MISI"
        "settings" -> "PENGATURAN"; "back" -> "KEMBALI"
        "home_sub" -> "RPG INTELIJEN SIBER  /  PELATIHAN PYTHON"
        "learn_sub" -> "Pahami konsep sebelum menggunakannya."
        "practice_sub" -> "Bereksperimen, jalankan, amati, dan perbaiki."
        "mission_sub" -> "Pilih tugas yang tersedia."
        "language" -> "BAHASA ANTARMUKA"
        "language_note" -> "Teks cerita dan materi mengikuti bahasa yang ditulis pada chapter."
        "run_practice" -> "JALANKAN LATIHAN"; "run" -> "JALANKAN  ·  EKSEKUSI SAJA"
        "submit" -> "KIRIM  ·  PERIKSA MISI"; "editor" -> "Editor kode Python"
        "practice_editor" -> "Editor latihan Python"; "hint" -> "MINTA PETUNJUK"
        "executed" -> "Kode dijalankan. Evaluasi misi tidak dilakukan."
        "run_done" -> "RUN selesai. Misi belum dievaluasi."
        "objective" -> "TUJUAN"; "loading" -> "MENJALANKAN..."
        else -> key
    }
}

@Composable
fun CyberProtocolApp() {
    val context = LocalContext.current
    val chapter = remember { ContentRepository(context).loadChapterOne() }
    val progressStore = remember { ProgressRepository(context) }
    val runner = remember { PythonExecutionService() }
    val story = remember { StoryEngine() }
    val scope = rememberCoroutineScope()
    val preferences = remember { context.getSharedPreferences("cyber_protocol_settings", 0) }
    var language by remember {
        mutableStateOf(
            if (preferences.getString("language", "en") == "id") UiLanguage.ID else UiLanguage.EN
        )
    }
    var languageMenu by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(progressStore.load()) }
    var screen by remember { mutableStateOf(Screen.HOME) }
    var selectedMission by remember { mutableStateOf(chapter.missions.first()) }
    var source by remember { mutableStateOf(TextFieldValue(chapter.missions.first().starterCode)) }
    var terminal by remember { mutableStateOf("TRAINING ENVIRONMENT READY\nWaiting for analyst input...") }
    var feedback by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var hintLevel by remember { mutableIntStateOf(0) }

    fun selectMission(mission: Mission) {
        selectedMission = mission
        source = TextFieldValue(mission.starterCode, TextRange(mission.starterCode.length))
        terminal = "MISSION ${mission.id.toString().padStart(2, '0')} READY"
        feedback = ""
        hintLevel = 0
        screen = Screen.MISSION
    }

    BackHandler(enabled = screen != Screen.HOME) {
        screen = when (screen) {
            Screen.MISSION -> Screen.MISSIONS
            Screen.LEARN, Screen.PRACTICE, Screen.MISSIONS, Screen.SETTINGS -> Screen.HOME
            Screen.HOME -> Screen.HOME
        }
    }

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(CyberAssets.emblem),
                    contentDescription = "Cyber Protocol emblem",
                    modifier = Modifier.size(44.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("CYBER PROTOCOL", color = MaterialTheme.colorScheme.primary,
                        fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 1.8.sp)
                    Text(tr("home_sub", language), color = MaterialTheme.colorScheme.secondary,
                        fontSize = 10.sp, letterSpacing = 0.5.sp)
                }
            }

            when (screen) {
                Screen.HOME -> {
                    CPPanel {
                        CPSectionHeader(chapter.title, eyebrow = "CURRENT CHAPTER")
                        Text(chapter.opening, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    CPPanel {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CPMetric("OPERATIVE LEVEL", (progress.xp / 250 + 1).toString())
                            CPMetric("EXPERIENCE", "${progress.xp} XP")
                            CPMetric("CREDITS", "${progress.credits} CR")
                        }
                        CPDivider()
                        Text(
                            "CHAPTER PROGRESS  ${(story.chapterProgress(chapter, progress.completedMissions) * 100).toInt()}%",
                            color = CyberPalette.Crimson,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                    Button(onClick = { screen = Screen.LEARN }, Modifier.fillMaxWidth()) { Text(tr("learn", language)) }
                    Button(onClick = { screen = Screen.PRACTICE }, Modifier.fillMaxWidth()) { Text(tr("practice", language)) }
                    Button(onClick = { screen = Screen.MISSIONS }, Modifier.fillMaxWidth()) { Text(tr("missions", language)) }
                    OutlinedButton(onClick = { screen = Screen.SETTINGS }, Modifier.fillMaxWidth()) { Text(tr("settings", language)) }
                }
                Screen.LEARN -> {
                    PageTitle(tr("learn", language), tr("learn_sub", language))
                    chapter.lessons.forEach { lesson ->
                        Text(lesson.title, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Text(lesson.concept, color = MaterialTheme.colorScheme.secondary)
                        Text(lesson.explanation)
                        CodePanel(lesson.example)
                        Text("OUTPUT\n${lesson.expectedOutput}", color = MaterialTheme.colorScheme.secondary,
                            fontFamily = FontFamily.Monospace)
                    }
                    BackButton(language) { screen = Screen.HOME; }
                }
                Screen.PRACTICE -> {
                    PageTitle(tr("practice", language), tr("practice_sub", language))
                    Text(if (language == UiLanguage.ID) "Eksekusi latihan tidak meluluskan misi." else "Practice execution does not award mission completion.")
                    CodeEditor(source, { source = it }, tr("practice_editor", language), 190)
                    Button(enabled = !busy, onClick = {
                        busy = true
                        scope.launch {
                            val result = withContext(Dispatchers.IO) { runner.run(source.text) }
                            terminal = result.output.ifBlank { "(no output)" }
                            if (result.error.isNotBlank()) terminal += "\nERROR: ${result.error}"
                            feedback = tr("executed", language)
                            busy = false
                        }
                    }, modifier = Modifier.fillMaxWidth()) { Text(if (busy) tr("loading", language) else tr("run_practice", language)) }
                    CodePanel(terminal)
                    Text(feedback, color = MaterialTheme.colorScheme.secondary)
                    BackButton(language) { screen = Screen.HOME }
                }
                Screen.MISSIONS -> {
                    PageTitle(tr("missions", language), tr("mission_sub", language))
                    chapter.missions.forEach { mission ->
                        val unlocked = mission.id == 1 || (mission.id - 1) in progress.completedMissions
                        Button(enabled = unlocked, onClick = { selectMission(mission) },
                            modifier = Modifier.fillMaxWidth()) {
                            val marker = if (mission.id in progress.completedMissions) "✓" else if (unlocked) "OPEN" else "LOCKED"
                            Text("$marker  M${mission.id.toString().padStart(2, '0')} — ${mission.title}")
                        }
                    }
                    BackButton(language) { screen = Screen.HOME }
                }
                Screen.MISSION -> {
                    PageTitle("MISSION ${selectedMission.id.toString().padStart(2, '0')}", selectedMission.title)
                    Text(selectedMission.briefing, color = Color.LightGray)
                    Text(tr("objective", language), color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    Text(selectedMission.objective)
                    Text("CONCEPTS: ${selectedMission.concepts.joinToString()}",
                        color = MaterialTheme.colorScheme.secondary)
                    CodeEditor(source, { source = it }, tr("editor", language), 220)
                    Button(enabled = !busy, onClick = {
                        busy = true
                        scope.launch {
                            val result = withContext(Dispatchers.IO) { runner.run(source.text) }
                            terminal = result.output.ifBlank { "(no output)" }
                            if (result.error.isNotBlank()) terminal += "\nERROR: ${result.error}"
                            feedback = tr("run_done", language)
                            busy = false
                        }
                    }, modifier = Modifier.fillMaxWidth()) { Text(if (busy) tr("loading", language) else tr("run", language)) }
                    Button(enabled = !busy, onClick = {
                        busy = true
                        scope.launch {
                            val result = withContext(Dispatchers.IO) { runner.submit(selectedMission.id, source.text) }
                            terminal = result.output.ifBlank { "(no output)" }
                            feedback = result.error.ifBlank { result.feedback }
                            if (result.passed) {
                                progress = progressStore.completeMissionOnce(selectedMission.id)
                                feedback = "MISSION VERIFIED  ·  ${result.feedback}"
                            }
                            busy = false
                        }
                    }, modifier = Modifier.fillMaxWidth()) { Text(tr("submit", language)) }
                    CodePanel(terminal)
                    Text(feedback, color = if (selectedMission.id in progress.completedMissions)
                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                    TextButton(onClick = { hintLevel = (hintLevel + 1).coerceAtMost(3) }) {
                        Text(if (hintLevel == 0) tr("hint", language) else if (language == UiLanguage.ID) "PETUNJUK BERIKUTNYA ($hintLevel/3)" else "NEXT HINT ($hintLevel/3)")
                    }
                    if (hintLevel > 0) Text(selectedMission.hints[hintLevel - 1],
                        color = MaterialTheme.colorScheme.secondary)
                    BackButton(language) { screen = Screen.MISSIONS }
                }
                Screen.SETTINGS -> {
                    PageTitle(tr("settings", language), if (language == UiLanguage.ID) "Atur preferensi antarmuka." else "Configure interface preferences.")
                    Text(tr("language", language), color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    Box {
                        OutlinedButton(onClick = { languageMenu = true }) { Text(language.label + "  ▾") }
                        DropdownMenu(expanded = languageMenu, onDismissRequest = { languageMenu = false }) {
                            UiLanguage.entries.forEach { option ->
                                DropdownMenuItem(text = { Text(option.label) }, onClick = {
                                    language = option
                                    preferences.edit().putString("language", option.code).apply()
                                    languageMenu = false
                                })
                            }
                        }
                    }
                    Text(tr("language_note", language), color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                    BackButton(language) { screen = Screen.HOME }
                }
            }
        }
    }
}

@Composable
private fun PageTitle(title: String, subtitle: String) {
    Text(title, color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, fontWeight = FontWeight.Black)
    Text(subtitle, color = MaterialTheme.colorScheme.secondary)
}

@Composable
private fun CodePanel(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Text(
            text,
            Modifier.fillMaxWidth().padding(12.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun BackButton(language: UiLanguage, onClick: () -> Unit) {
    TextButton(onClick = onClick) { Text("←  ${tr("back", language)}") }
}
