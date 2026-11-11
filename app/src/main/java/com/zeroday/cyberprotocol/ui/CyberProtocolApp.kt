package com.zeroday.cyberprotocol.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.zeroday.cyberprotocol.data.VirtualFileSystem
import com.zeroday.cyberprotocol.execution.PythonExecutionService
import com.zeroday.cyberprotocol.ui.theme.CyberPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private enum class GameScreen {
    MAIN_MENU, CAMPAIGN, STORY, APP_HUB, MESSENGER, TERMINAL,
    DATABASE, DOSSIER, ANALYSIS, OPERATIONS
}

private data class StoryBeat(val title: String, val eyebrow: String, val body: String, val button: String = "CONTINUE")

private val firstContactBeats = listOf(
    StoryBeat("UNKNOWN SENDER", "CHAPTER 001  /  FIRST CONTACT",
        "Late in the evening, a device you rarely use wakes with a single encrypted notification.\n\nNo familiar name. No number. Only a secure channel requesting your attention.", "OPEN MESSAGE"),
    StoryBeat("THE INVITATION", "INCOMING TRANSMISSION",
        "“We know you are not a trained operative. That is precisely why this message may surprise you. Someone has identified a possibility in you that you have not yet had a reason to recognize.”\n\n“An organization called A.E.G.I.S. requests a confidential conversation.”", "ACCEPT CONTACT"),
    StoryBeat("THE ORGANIZATION", "A.E.G.I.S.  /  MANILA",
        "A.E.G.I.S. is an independent international intelligence organization. Its work spans intelligence analysis, investigation, cyber operations, technical research, and field operations.\n\nIts headquarters are not listed on public maps. Access is restricted. Its cases are classified. The organization does not recruit casually.", "REVIEW CLASSIFIED FILE"),
    StoryBeat("CLASSIFIED FILE 001", "RECRUITMENT RECORD  /  RESTRICTED",
        "The file contains a short assessment prepared before this contact. It does not explain why you were selected. Several sections are withheld.\n\nOne line remains visible:\n\n“Potential is not the same as experience. The candidate has the former. The latter can be taught.”", "BEGIN FIRST ASSIGNMENT"),
    StoryBeat("FIRST ASSIGNMENT", "GUIDED DECRYPTION  /  LAYER 01",
        "A short message is hidden behind a simple instruction. NEXUS, the A.E.G.I.S. communication-intelligence system, will guide your first step.\n\nStore the message in a variable, then use print() to reveal it. This is a fictional training cipher, not real cryptographic security.", "OPEN CODE EDITOR"),
    StoryBeat("SYSTEM VERIFICATION", "GUIDED DECRYPTION  /  LAYER 02",
        "A second message arrives. This time, the structure is familiar. Apply the same Python ideas without copying the first answer blindly.\n\nA correct result will verify that you can follow a basic instruction and inspect its output.", "VERIFY MESSAGE"),
    StoryBeat("OFFICIAL RECRUITMENT", "A.E.G.I.S.  /  PERSONNEL",
        "The secure channel confirms your recruitment as a trainee. Your access is limited, your record is new, and your training has only begun.\n\nSomewhere inside the headquarters, your assigned mentor is waiting to introduce herself.",
        "ENTER A.E.G.I.S.")
)

private val storyCode = listOf(
    "signal = \"ACCESS GRANTED\"\nprint(signal)",
    "message = \"IDENTITY VERIFIED\"\nprint(message)"
)
private val storyExpected = listOf("ACCESS GRANTED", "IDENTITY VERIFIED")

@Composable
fun CyberProtocolApp() {
    val context = LocalContext.current
    val preferences = remember { context.getSharedPreferences("cyber_protocol_campaign_v2", 0) }
    val fileSystem = remember { VirtualFileSystem(context) }
    val python = remember { PythonExecutionService() }
    val scope = rememberCoroutineScope()

    var screenName by rememberSaveable { mutableStateOf(GameScreen.MAIN_MENU.name) }
    val screen = runCatching { GameScreen.valueOf(screenName) }.getOrDefault(GameScreen.MAIN_MENU)
    var returnScreen by rememberSaveable { mutableStateOf(GameScreen.CAMPAIGN.name) }
    var storyStage by rememberSaveable { mutableIntStateOf(preferences.getInt("story_stage", 0).coerceIn(0, 7)) }
    var code by remember { mutableStateOf(TextFieldValue(preferences.getString("saved_code_$storyStage", storyCode.getOrElse((storyStage - 4).coerceIn(0, 1)) { "" }) ?: "")) }
    var codeOutput by rememberSaveable { mutableStateOf("OUTPUT WILL APPEAR HERE") }
    var codeFeedback by rememberSaveable { mutableStateOf("") }
    var command by rememberSaveable { mutableStateOf("") }
    var currentDirectory by rememberSaveable { mutableStateOf(fileSystem.initialDirectory()) }
    var terminalOutput by rememberSaveable {
        mutableStateOf("A.E.G.I.S. VIRTUAL TERMINAL  ·  BUILD 0.1\nGame-managed environment ready.\nType 'help' to list supported commands.\n")
    }
    var terminalBusy by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var messengerReply by rememberSaveable { mutableStateOf("") }
    var savedCodeStatus by rememberSaveable { mutableStateOf("UNSAVED CHANGES") }

    fun saveStage(value: Int) {
        storyStage = value.coerceIn(0, 7)
        preferences.edit().putInt("story_stage", storyStage).apply()
        code = TextFieldValue(
            preferences.getString("saved_code_$storyStage",
                storyCode.getOrElse((storyStage - 4).coerceIn(0, 1)) { "" }) ?: ""
        )
        codeOutput = "OUTPUT WILL APPEAR HERE"
        codeFeedback = ""
        savedCodeStatus = "SAVED DRAFT"
    }

    fun openHub(from: GameScreen) {
        returnScreen = from.name
        screenName = GameScreen.APP_HUB.name
    }

    fun executeTerminalCommand() {
        val input = command.trim()
        if (input.isEmpty() || terminalBusy) return
        command = ""
        val response = fileSystem.execute(input, currentDirectory)
        currentDirectory = response.currentDirectory
        if (response.clearScreen) {
            terminalOutput = "A.E.G.I.S. VIRTUAL TERMINAL  ·  OUTPUT CLEARED\n"
            return
        }
        terminalOutput += "\n$ $input\n"
        val output = response.output
        if (output.startsWith("PYTHON_LAUNCH:")) {
            val fileName = output.removePrefix("PYTHON_LAUNCH:")
            val source = fileSystem.readFile(fileName, currentDirectory)
            if (source == null) {
                terminalOutput += "python: cannot open '$fileName': file not found\n"
            } else {
                terminalBusy = true
                scope.launch {
                    val result = withContext(Dispatchers.IO) { python.run(source) }
                    terminalOutput += (result.output.ifBlank { "(no output)" }) + "\n"
                    if (result.error.isNotBlank()) terminalOutput += "ERROR: " + result.error + "\n"
                    terminalBusy = false
                }
            }
        } else if (output.isNotBlank()) {
            terminalOutput += output + "\n"
        }
    }

    BackHandler(enabled = screen != GameScreen.MAIN_MENU) {
        screenName = when (screen) {
            GameScreen.CAMPAIGN -> GameScreen.MAIN_MENU.name
            GameScreen.STORY -> GameScreen.CAMPAIGN.name
            GameScreen.APP_HUB -> returnScreen
            GameScreen.MESSENGER, GameScreen.TERMINAL, GameScreen.DATABASE,
            GameScreen.DOSSIER, GameScreen.ANALYSIS, GameScreen.OPERATIONS -> GameScreen.APP_HUB.name
            GameScreen.MAIN_MENU -> GameScreen.MAIN_MENU.name
        }
    }

    when (screen) {
        GameScreen.MAIN_MENU -> MainMenuScreen(
            onContinue = { screenName = GameScreen.CAMPAIGN.name },
            onNewCampaign = { showResetDialog = true },
            onHub = { openHub(GameScreen.MAIN_MENU) }
        )
        GameScreen.CAMPAIGN -> CampaignScreen(
            storyStage = storyStage,
            onBack = { screenName = GameScreen.MAIN_MENU.name },
            onContinue = { screenName = if (storyStage < 7) GameScreen.STORY.name else GameScreen.APP_HUB.name },
            onHub = { openHub(GameScreen.CAMPAIGN) }
        )
        GameScreen.STORY -> {
            val beatIndex = storyStage.coerceIn(0, 6)
            StoryScreen(
                beat = firstContactBeats[beatIndex],
                stage = beatIndex,
                code = code,
                onCodeChange = { code = it; savedCodeStatus = "UNSAVED CHANGES" },
                output = codeOutput,
                feedback = codeFeedback,
                saveStatus = savedCodeStatus,
                onSaveCode = {
                    preferences.edit().putString("saved_code_$storyStage", code.text).apply()
                    savedCodeStatus = "SAVED"
                },
                onRun = {
                    scope.launch {
                        codeFeedback = "RUNNING — execution only; no mission evaluation."
                        val result = withContext(Dispatchers.IO) { python.run(code.text) }
                        codeOutput = result.output.ifBlank { "(no output)" }
                        if (result.error.isNotBlank()) codeOutput += "\n" + result.error
                        codeFeedback = "RUN COMPLETE — campaign progress has not changed."
                    }
                },
                onSubmit = {
                    val expected = storyExpected.getOrNull(beatIndex - 4).orEmpty()
                    scope.launch {
                        codeFeedback = "VERIFYING..."
                        val result = withContext(Dispatchers.IO) { python.run(code.text) }
                        codeOutput = result.output.ifBlank { "(no output)" }
                        if (result.error.isNotBlank()) codeOutput += "\n" + result.error
                        val hasVariable = Regex("""(?m)^\s*[A-Za-z][A-Za-z0-9_]*\s*=""").containsMatchIn(code.text)
                        val hasPrint = Regex("""\bprint\s*\(""").containsMatchIn(code.text)
                        if (result.error.isBlank() && result.output.trim() == expected && hasVariable && hasPrint) {
                            preferences.edit().putString("saved_code_$storyStage", code.text).apply()
                            savedCodeStatus = "SAVED AT CHECKPOINT"
                            codeFeedback = "VERIFIED — required output and variable/print concepts detected."
                            saveStage(storyStage + 1)
                            screenName = if (storyStage >= 7) GameScreen.CAMPAIGN.name else GameScreen.STORY.name
                        } else {
                            codeFeedback = "NOT VERIFIED — check the output, variable assignment, and print() call. You can edit and retry."
                        }
                    }
                },
                onContinue = {
                    if (storyStage == 6) {
                        saveStage(7)
                        screenName = GameScreen.CAMPAIGN.name
                    } else {
                        saveStage(storyStage + 1)
                    }
                },
                onBack = { screenName = GameScreen.CAMPAIGN.name },
                onHub = { openHub(GameScreen.STORY) }
            )
        }
        GameScreen.APP_HUB -> AppHubScreen(
            storyStage = storyStage,
            onClose = { screenName = returnScreen },
            onOpen = { target -> screenName = target.name }
        )
        GameScreen.MESSENGER -> MessengerScreen(
            onBack = { screenName = GameScreen.APP_HUB.name },
            reply = messengerReply,
            onReply = { messengerReply = it }
        )
        GameScreen.TERMINAL -> TerminalScreen(
            currentDirectory = currentDirectory,
            command = command,
            onCommandChange = { command = it },
            output = terminalOutput,
            busy = terminalBusy,
            onExecute = { executeTerminalCommand() },
            onBack = { screenName = GameScreen.APP_HUB.name }
        )
        GameScreen.DATABASE -> InfoAppScreen(
            "INTELLIGENCE DATABASE", "A.E.G.I.S. / RESTRICTED INDEX",
            "No operational records are currently assigned to your clearance.\n\nTraining records and fictional case data will appear here as campaign permissions expand.",
            CyberAssets.database, { screenName = GameScreen.APP_HUB.name }
        )
        GameScreen.DOSSIER -> InfoAppScreen(
            "DOSSIER", "PERSONNEL RECORD / RECRUIT",
            "IDENTITY: TRAINEE OPERATIVE\nORGANIZATION: A.E.G.I.S.\nCLEARANCE: PROVISIONAL\nMENTOR: FREYA KNOX\nSTATUS: RECRUITMENT COMPLETE\n\nAdditional personal and operational records unlock through the campaign.",
            CyberAssets.document, { screenName = GameScreen.APP_HUB.name }
        )
        GameScreen.ANALYSIS -> InfoAppScreen(
            "ANALYSIS", "INTELLIGENCE WORKSPACE",
            "The analysis workspace will collect verified findings, working notes, and evidence assessments.\n\nEvidence status will distinguish CONFIRMED, INFERRED, DISPUTED, and UNVERIFIED information.",
            CyberAssets.dataAnalysis, { screenName = GameScreen.APP_HUB.name }
        )
        GameScreen.OPERATIONS -> InfoAppScreen(
            "OPERATIONS", "MISSION ACCESS / CLEARANCE CHECK",
            if (storyStage >= 7) "FIRST CONTACT — RECRUITMENT\nSTATUS: COMPLETED\n\nYour next operational assignment is being prepared."
            else "No operation is currently authorized.\nComplete the recruitment sequence to receive your first assignment.",
            CyberAssets.investigation, { screenName = GameScreen.APP_HUB.name }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("START NEW CAMPAIGN?") },
            text = { Text("Campaign story progress will return to the beginning. Your Personal Workspace files will be preserved.") },
            confirmButton = {
                TextButton(onClick = {
                    showResetDialog = false
                    saveStage(0)
                    screenName = GameScreen.CAMPAIGN.name
                }) { Text("START") }
            },
            dismissButton = { TextButton(onClick = { showResetDialog = false }) { Text("CANCEL") } }
        )
    }
}

@Composable
private fun MainMenuScreen(onContinue: () -> Unit, onNewCampaign: () -> Unit, onHub: () -> Unit) {
    Box(
        Modifier.fillMaxSize().background(
            androidx.compose.ui.graphics.Brush.verticalGradient(
                listOf(Color(0xFF062B33), Color(0xFF071923), Color(0xFF090A0E))
            )
        )
    ) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 26.dp, vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(20.dp))
            androidx.compose.foundation.Image(painterResource(CyberAssets.emblem), "A.E.G.I.S. emblem", Modifier.size(94.dp))
            Spacer(Modifier.height(22.dp))
            Text("A.E.G.I.S.", color = Color(0xFFBCE8EA), fontSize = 15.sp, letterSpacing = 5.sp, fontWeight = FontWeight.Bold)
            Text("CYBER PROTOCOL", color = Color.White, fontSize = 29.sp, letterSpacing = 2.2.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(10.dp))
            Text("KNOWLEDGE IS POWER.\nINTELLIGENCE IS RESPONSIBILITY.",
                color = Color(0xFF8AA8AE), fontSize = 10.sp, lineHeight = 17.sp,
                letterSpacing = 1.2.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(Modifier.height(46.dp))
            Surface(color = Color(0xFF0B2028).copy(alpha = 0.82f), shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, Color(0xFF28515B)), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    MenuButton("CONTINUE CAMPAIGN", "Resume your recruitment file", onContinue, true)
                    MenuButton("NEW CAMPAIGN", "Begin Chapter 001 — First Contact", onNewCampaign)
                    MenuButton("APPLICATION HUB", "Open your A.E.G.I.S. device", onHub)
                }
            }
            Spacer(Modifier.height(30.dp))
            Text("MANILA  /  CENTRAL COMMAND  /  SECURE ACCESS",
                color = Color(0xFF55747A), fontSize = 9.sp, letterSpacing = 1.sp)
        }
    }
}

@Composable
private fun MenuButton(title: String, subtitle: String, onClick: () -> Unit, primary: Boolean = false) {
    Surface(onClick = onClick, color = if (primary) Color(0xFF12404A) else Color(0xFF0C2931),
        shape = RoundedCornerShape(3.dp),
        border = BorderStroke(1.dp, if (primary) Color(0xFF5A9BA3) else Color(0xFF25434A)),
        modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(horizontal = 14.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                Text(subtitle, color = Color(0xFF9AB4B8), fontSize = 11.sp)
            }
            Text("›", color = Color(0xFFBCE8EA), fontSize = 26.sp)
        }
    }
}

@Composable
private fun CampaignScreen(storyStage: Int, onBack: () -> Unit, onContinue: () -> Unit, onHub: () -> Unit) {
    GamePage("CAMPAIGN", "A.E.G.I.S. / STORY OPERATIONS", onBack, onHub) {
        GamePanel {
            Text("CAMPAIGN 01", color = CyberPalette.SteelGray, fontSize = 10.sp, letterSpacing = 1.5.sp)
            Text("THE RECRUITMENT", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Black)
            Text("An invitation. An unknown selection. A first step into intelligence work.",
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            HorizontalDivider(color = CyberPalette.Divider)
            Text("CHAPTER 001", color = CyberPalette.Crimson, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("FIRST CONTACT", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Progress: " + if (storyStage >= 7) "RECRUITMENT COMPLETE" else "$storyStage/7 story checkpoints",
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            LinearProgressIndicator(progress = { storyStage.coerceAtMost(7) / 7f },
                modifier = Modifier.fillMaxWidth(), color = CyberPalette.Crimson, trackColor = CyberPalette.Divider)
            CPPrimaryButton(if (storyStage >= 7) "OPEN A.E.G.I.S. HUB" else "CONTINUE STORY", onContinue)
        }
        GamePanel {
            Text("CAMPAIGN STRUCTURE", color = CyberPalette.SteelGray, fontSize = 10.sp, letterSpacing = 1.3.sp)
            Text("Story scenes and discoveries lead into guided operations. New chapters remain hidden until the campaign is ready to reveal them.",
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun StoryScreen(
    beat: StoryBeat, stage: Int, code: TextFieldValue, onCodeChange: (TextFieldValue) -> Unit,
    output: String, feedback: String, saveStatus: String, onSaveCode: () -> Unit,
    onRun: () -> Unit, onSubmit: () -> Unit, onContinue: () -> Unit,
    onBack: () -> Unit, onHub: () -> Unit
) {
    GamePage("FIRST CONTACT", "CHAPTER 001  /  THE RECRUITMENT", onBack, onHub) {
        GamePanel {
            Text(beat.eyebrow, color = CyberPalette.Crimson, fontSize = 10.sp,
                letterSpacing = 1.4.sp, fontWeight = FontWeight.Bold)
            Text(beat.title, color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Black)
            Text(beat.body, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 22.sp)
        }
        if (stage == 4 || stage == 5) {
            GamePanel {
                Text("NEXUS  /  TRAINING ASSISTANCE", color = Color(0xFF83C7D0),
                    fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(if (stage == 4) "Store the message in a variable. Then display that variable with print()."
                    else "Use the same two concepts to reveal this second message. Change the code if you need to.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                CodeEditor(code, onCodeChange, "PYTHON 3 CODE EDITOR", 180)
                Text(saveStatus, color = CyberPalette.SteelGray, fontSize = 10.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onSaveCode, modifier = Modifier.weight(1f)) { Text("SAVE CODE") }
                    Button(onClick = onRun, modifier = Modifier.weight(1f)) { Text("RUN") }
                }
                Surface(color = Color(0xFF080D12), shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, CyberPalette.Divider)) {
                    Column(Modifier.fillMaxWidth().padding(12.dp)) {
                        Text("TERMINAL OUTPUT", color = CyberPalette.SteelGray, fontSize = 9.sp)
                        Text(output, color = Color(0xFFB7E4C7), fontFamily = FontFamily.Monospace)
                    }
                }
                if (feedback.isNotBlank()) Text(feedback, color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                CPPrimaryButton("SUBMIT FOR VERIFICATION", onSubmit)
                Text("RUN only executes. SUBMIT checks the objective and advances the story only when verified.",
                    color = CyberPalette.SteelGray, fontSize = 10.sp)
            }
        } else {
            CPPrimaryButton(beat.button, onContinue)
        }
        Text("CHECKPOINT " + (stage + 1) + " / 7", color = CyberPalette.SteelGray, fontSize = 10.sp, letterSpacing = 1.2.sp)
    }
}

@Composable
private fun AppHubScreen(storyStage: Int, onClose: () -> Unit, onOpen: (GameScreen) -> Unit) {
    GamePage("APPLICATION HUB", "A.E.G.I.S. SECURE DEVICE", onClose, null) {
        GamePanel {
            Text("AUTHORIZED APPLICATIONS", color = CyberPalette.SteelGray, fontSize = 10.sp, letterSpacing = 1.2.sp)
            Text("Select an application. Closing it returns you to the previous operation.",
                color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
        data class HubApp(val title: String, val icon: Int, val target: GameScreen, val unlockAt: Int)
        val apps = listOf(
            HubApp("SECURE MESSENGER", CyberAssets.connection, GameScreen.MESSENGER, 7),
            HubApp("TERMINAL", CyberAssets.terminal, GameScreen.TERMINAL, 4),
            HubApp("INTELLIGENCE DATABASE", CyberAssets.database, GameScreen.DATABASE, 3),
            HubApp("DOSSIER", CyberAssets.document, GameScreen.DOSSIER, 3),
            HubApp("ANALYSIS", CyberAssets.dataAnalysis, GameScreen.ANALYSIS, 5),
            HubApp("OPERATIONS", CyberAssets.investigation, GameScreen.OPERATIONS, 7)
        )
        apps.chunked(2).forEach { rowApps ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowApps.forEach { app ->
                    val unlocked = storyStage >= app.unlockAt
                    Surface(onClick = { if (unlocked) onOpen(app.target) }, modifier = Modifier.weight(1f),
                        color = if (unlocked) MaterialTheme.colorScheme.surface else Color(0xFF101216),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (unlocked) CyberPalette.Divider else Color(0xFF22252B))) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            androidx.compose.foundation.Image(painterResource(app.icon), null,
                                Modifier.size(42.dp), alpha = if (unlocked) 1f else 0.3f)
                            Text(app.title, color = if (unlocked) Color.White else CyberPalette.SteelGray,
                                fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text(if (unlocked) "OPEN  ›" else "LOCKED  ·  CLEARANCE REQUIRED",
                                color = CyberPalette.SteelGray, fontSize = 9.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MessengerScreen(onBack: () -> Unit, reply: String, onReply: (String) -> Unit) {
    GamePage("SECURE MESSENGER", "A.E.G.I.S. / ENCRYPTED CHANNEL", onBack, null) {
        GamePanel {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(color = Color(0xFF24333A), shape = RoundedCornerShape(50)) {
                    Text("FK", Modifier.padding(12.dp), color = Color.White, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("FREYA KNOX", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("PRIMARY MENTOR  ·  SECURE", color = CyberPalette.Success, fontSize = 9.sp)
                }
            }
            ChatBubble("Freya Knox", "Good. Your recruitment is official. I will supervise your initial training and make sure you understand every system you are authorized to use.", false)
            ChatBubble("Freya Knox", "I have reviewed your file. I see potential. You, on the other hand, probably see a very long list of things you do not know yet. That is a perfectly reasonable assessment.", false)
            if (reply.isNotBlank()) {
                ChatBubble("YOU", reply, true)
                ChatBubble("Freya Knox", "Acknowledged. We will take this one operation at a time. Questions are welcome; pretending to understand is not required.", false)
                ChatBubble("Freya Knox", "Excellent. You have chosen a mentor of extraordinary brilliance. I recommend writing that down before I become modest again.", false)
            }
            listOf("Understood. Where do we begin?", "I have a lot to learn.", "Why did you choose me?").forEach { choice ->
                OutlinedButton(onClick = { onReply(choice) }, modifier = Modifier.fillMaxWidth()) { Text(choice) }
            }
            Text("Initial dialogue choices affect the immediate reply only. They do not alter campaign branches or relationship values.",
                color = CyberPalette.SteelGray, fontSize = 10.sp)
        }
    }
}

@Composable
private fun ChatBubble(sender: String, message: String, outgoing: Boolean) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = if (outgoing) Alignment.End else Alignment.Start) {
        Text(sender, color = CyberPalette.SteelGray, fontSize = 9.sp)
        Surface(color = if (outgoing) Color(0xFF253C45) else Color(0xFF1A1D24),
            shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, CyberPalette.Divider),
            modifier = Modifier.fillMaxWidth(0.92f)) {
            Text(message, Modifier.padding(12.dp), color = Color(0xFFE4E7EA), lineHeight = 20.sp)
        }
    }
}

@Composable
private fun TerminalScreen(
    currentDirectory: String, command: String, onCommandChange: (String) -> Unit,
    output: String, busy: Boolean, onExecute: () -> Unit, onBack: () -> Unit
) {
    GamePage("TERMINAL", "A.E.G.I.S. VIRTUAL ENVIRONMENT", onBack, null) {
        GamePanel {
            Text(currentDirectory, color = Color(0xFF8CC8A2), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            Surface(color = Color(0xFF070B0E), shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFF263B35))) {
                Text(output, Modifier.fillMaxWidth().heightIn(min = 240.dp, max = 430.dp)
                    .verticalScroll(rememberScrollState()).padding(12.dp),
                    color = Color(0xFFB7D9C2), fontFamily = FontFamily.Monospace, fontSize = 11.sp, lineHeight = 17.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = command, onValueChange = onCommandChange,
                    modifier = Modifier.weight(1f), singleLine = true, placeholder = { Text("Enter command…") },
                    textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { onExecute() }))
                Button(onClick = onExecute, enabled = !busy) { Text(if (busy) "…" else "RUN") }
            }
            val availableCommands = listOf("help", "pwd", "ls", "cd", "cat", "mkdir", "touch", "rm", "echo", "whoami", "python", "clear")
            val query = command.trim().lowercase()
            val suggestions = if (query.isBlank()) listOf("help", "pwd", "ls", "whoami")
                else availableCommands.filter { it.startsWith(query) }.take(5)
            if (suggestions.isNotEmpty()) {
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    suggestions.forEach { suggestion ->
                        OutlinedButton(onClick = { onCommandChange(suggestion) },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)) {
                            Text(suggestion, fontFamily = FontFamily.Monospace, fontSize = 10.sp)
                        }
                    }
                }
            }
            Text("Linux-like commands • isolated virtual filesystem • no Android shell access",
                color = CyberPalette.SteelGray, fontSize = 10.sp)
            TextButton(onClick = { onCommandChange("help") }) { Text("FILL COMMAND REFERENCE") }
        }
    }
}

@Composable
private fun InfoAppScreen(title: String, subtitle: String, body: String, icon: Int, onBack: () -> Unit) {
    GamePage(title, subtitle, onBack, null) {
        GamePanel {
            androidx.compose.foundation.Image(painterResource(icon), null, Modifier.size(54.dp))
            Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 22.sp)
        }
    }
}

@Composable
private fun GamePage(title: String, subtitle: String, onBack: () -> Unit,
    onHub: (() -> Unit)?, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().background(CyberPalette.Obsidian)
        .verticalScroll(rememberScrollState()).padding(horizontal = 18.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Text(subtitle, color = CyberPalette.SteelGray, fontSize = 9.sp, letterSpacing = 0.8.sp)
            }
            if (onHub != null) TextButton(onClick = onHub) { Text("APPS") }
            TextButton(onClick = onBack) { Text("BACK") }
        }
        HorizontalDivider(color = CyberPalette.Divider)
        content()
        Spacer(Modifier.height(18.dp))
    }
}

@Composable
private fun GamePanel(content: @Composable ColumnScope.() -> Unit) {
    Surface(color = Color(0xFF15171D), shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, CyberPalette.Divider), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
    }
}
