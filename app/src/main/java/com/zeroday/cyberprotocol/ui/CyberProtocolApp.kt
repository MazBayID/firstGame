package com.zeroday.cyberprotocol.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroday.cyberprotocol.core.model.Mission
import com.zeroday.cyberprotocol.data.ContentRepository
import com.zeroday.cyberprotocol.data.ProgressRepository
import com.zeroday.cyberprotocol.core.model.StoryEngine
import com.zeroday.cyberprotocol.execution.PythonExecutionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private enum class Screen { HOME, LEARN, PRACTICE, MISSIONS, MISSION }

@Composable
fun CyberProtocolApp() {
    val context = LocalContext.current
    val chapter = remember { ContentRepository(context).loadChapterOne() }
    val progressStore = remember { ProgressRepository(context) }
    val runner = remember { PythonExecutionService() }
    val story = remember { StoryEngine() }
    val scope = rememberCoroutineScope()
    var progress by remember { mutableStateOf(progressStore.load()) }
    var screen by remember { mutableStateOf(Screen.HOME) }
    var selectedMission by remember { mutableStateOf(chapter.missions.first()) }
    var source by remember { mutableStateOf(chapter.missions.first().starterCode) }
    var terminal by remember { mutableStateOf("TRAINING ENVIRONMENT READY\nWaiting for analyst input...") }
    var feedback by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var hintLevel by remember { mutableIntStateOf(0) }

    fun selectMission(mission: Mission) {
        selectedMission = mission
        source = mission.starterCode
        terminal = "MISSION ${mission.id.toString().padStart(2, '0')} READY"
        feedback = ""
        hintLevel = 0
        screen = Screen.MISSION
    }

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("CYBER PROTOCOL", color = MaterialTheme.colorScheme.primary, fontSize = 23.sp,
                fontWeight = FontWeight.Black, letterSpacing = 2.sp)
            Text("CYBER INTELLIGENCE RPG  /  PYTHON TRAINING",
                color = MaterialTheme.colorScheme.secondary, fontSize = 10.sp)

            when (screen) {
                Screen.HOME -> {
                    Text(chapter.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(chapter.opening, color = Color.LightGray)
                    Text("LEVEL ${progress.xp / 250 + 1}   •   ${progress.xp} XP   •   ${progress.credits} CR")
                    Text("CHAPTER PROGRESS  ${(story.chapterProgress(chapter, progress.completedMissions) * 100).toInt()}%")
                    Button(onClick = { screen = Screen.LEARN }, Modifier.fillMaxWidth()) { Text("LEARN") }
                    Button(onClick = { screen = Screen.PRACTICE }, Modifier.fillMaxWidth()) { Text("PRACTICE") }
                    Button(onClick = { screen = Screen.MISSIONS }, Modifier.fillMaxWidth()) { Text("MISSION BOARD") }
                }
                Screen.LEARN -> {
                    PageTitle("LEARN", "Understand the concepts before using them.")
                    chapter.lessons.forEach { lesson ->
                        Text(lesson.title, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Text(lesson.concept, color = MaterialTheme.colorScheme.secondary)
                        Text(lesson.explanation)
                        CodePanel(lesson.example)
                        Text("OUTPUT\n${lesson.expectedOutput}", color = MaterialTheme.colorScheme.secondary,
                            fontFamily = FontFamily.Monospace)
                    }
                    BackButton { screen = Screen.HOME }
                }
                Screen.PRACTICE -> {
                    PageTitle("PRACTICE", "Experiment, run, observe, and repair.")
                    Text("Practice execution does not award mission completion.")
                    OutlinedTextField(value = source, onValueChange = { source = it },
                        modifier = Modifier.fillMaxWidth().height(190.dp),
                        label = { Text("Python practice editor") },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace))
                    Button(enabled = !busy, onClick = {
                        busy = true
                        scope.launch {
                            val result = withContext(Dispatchers.IO) { runner.run(source) }
                            terminal = result.output.ifBlank { "(no output)" }
                            if (result.error.isNotBlank()) terminal += "\nERROR: ${result.error}"
                            feedback = "Executed only. No mission evaluation was performed."
                            busy = false
                        }
                    }, modifier = Modifier.fillMaxWidth()) { Text(if (busy) "RUNNING..." else "RUN PRACTICE") }
                    CodePanel(terminal)
                    Text(feedback, color = MaterialTheme.colorScheme.secondary)
                    BackButton { screen = Screen.HOME }
                }
                Screen.MISSIONS -> {
                    PageTitle("MISSION BOARD", "Choose an available assignment.")
                    chapter.missions.forEach { mission ->
                        val unlocked = mission.id == 1 || (mission.id - 1) in progress.completedMissions
                        Button(enabled = unlocked, onClick = { selectMission(mission) },
                            modifier = Modifier.fillMaxWidth()) {
                            val marker = if (mission.id in progress.completedMissions) "✓" else if (unlocked) "OPEN" else "LOCKED"
                            Text("$marker  M${mission.id.toString().padStart(2, '0')} — ${mission.title}")
                        }
                    }
                    BackButton { screen = Screen.HOME }
                }
                Screen.MISSION -> {
                    PageTitle("MISSION ${selectedMission.id.toString().padStart(2, '0')}", selectedMission.title)
                    Text(selectedMission.briefing, color = Color.LightGray)
                    Text("OBJECTIVE", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    Text(selectedMission.objective)
                    Text("CONCEPTS: ${selectedMission.concepts.joinToString()}",
                        color = MaterialTheme.colorScheme.secondary)
                    OutlinedTextField(value = source, onValueChange = { source = it },
                        modifier = Modifier.fillMaxWidth().height(220.dp),
                        label = { Text("Python code editor") },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace))
                    Button(enabled = !busy, onClick = {
                        busy = true
                        scope.launch {
                            val result = withContext(Dispatchers.IO) { runner.run(source) }
                            terminal = result.output.ifBlank { "(no output)" }
                            if (result.error.isNotBlank()) terminal += "\nERROR: ${result.error}"
                            feedback = "RUN completed. The mission has not been evaluated."
                            busy = false
                        }
                    }, modifier = Modifier.fillMaxWidth()) { Text("RUN  ·  EXECUTE ONLY") }
                    Button(enabled = !busy, onClick = {
                        busy = true
                        scope.launch {
                            val result = withContext(Dispatchers.IO) { runner.submit(selectedMission.id, source) }
                            terminal = result.output.ifBlank { "(no output)" }
                            feedback = result.error.ifBlank { result.feedback }
                            if (result.passed) {
                                progress = progressStore.completeMissionOnce(selectedMission.id)
                                feedback = "MISSION VERIFIED  ·  ${result.feedback}"
                            }
                            busy = false
                        }
                    }, modifier = Modifier.fillMaxWidth()) { Text("SUBMIT  ·  MISSION CHECK") }
                    CodePanel(terminal)
                    Text(feedback, color = if (selectedMission.id in progress.completedMissions)
                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                    TextButton(onClick = { hintLevel = (hintLevel + 1).coerceAtMost(3) }) {
                        Text(if (hintLevel == 0) "REQUEST HINT" else "NEXT HINT ($hintLevel/3)")
                    }
                    if (hintLevel > 0) Text(selectedMission.hints[hintLevel - 1],
                        color = MaterialTheme.colorScheme.secondary)
                    BackButton { screen = Screen.MISSIONS }
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
    Surface(color = Color(0xFF111B23), shape = MaterialTheme.shapes.medium) {
        Text(text, Modifier.fillMaxWidth().padding(12.dp), color = Color(0xFFB7C7D1),
            fontFamily = FontFamily.Monospace, fontSize = 12.sp)
    }
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    TextButton(onClick = onClick) { Text("BACK") }
}
