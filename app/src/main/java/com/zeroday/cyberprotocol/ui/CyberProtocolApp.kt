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
import android.content.res.Configuration
import java.util.Locale
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.zeroday.cyberprotocol.R
import com.zeroday.cyberprotocol.core.model.GameLanguage
import com.zeroday.cyberprotocol.data.GameLanguagePreferences
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zeroday.cyberprotocol.core.model.CompetencyTrack
import com.zeroday.cyberprotocol.core.model.MissionArchetype
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.core.model.CaseProgress
import com.zeroday.cyberprotocol.core.model.CasePhase
import com.zeroday.cyberprotocol.core.model.CasePhaseEvaluator
import com.zeroday.cyberprotocol.data.Campaign01Catalog
import com.zeroday.cyberprotocol.data.Campaign01CaseCatalog
import com.zeroday.cyberprotocol.data.Campaign01Story
import com.zeroday.cyberprotocol.data.Campaign01StoryNavigationPolicy
import com.zeroday.cyberprotocol.data.Campaign01StoryLocalization
import com.zeroday.cyberprotocol.data.StoryBeat
import com.zeroday.cyberprotocol.data.LocalGameStateRepository
import com.zeroday.cyberprotocol.data.VirtualFileSystem
import com.zeroday.cyberprotocol.data.MissionWorkspacePolicy
import com.zeroday.cyberprotocol.execution.PythonExecutionService
import com.zeroday.cyberprotocol.domain.usecase.MissionSubmissionPolicy
import com.zeroday.cyberprotocol.ui.theme.CyberPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CyberProtocolApp() {
    val context = LocalContext.current
    var startupReady by remember { mutableStateOf(false) }
    var startupError by remember { mutableStateOf<String?>(null) }
    var startupAttempt by remember { mutableIntStateOf(0) }
    val settingsPreferences = remember { context.getSharedPreferences("cyber_protocol_settings_v1", 0) }
    var reducedMotion by remember { mutableStateOf(settingsPreferences.getBoolean("reduced_motion", false)) }

    LaunchedEffect(startupAttempt) {
        runCatching {
            withContext(Dispatchers.IO) {
                LocalGameStateRepository(context).load()
                VirtualFileSystem(context).initialDirectory()
                com.chaquo.python.Python.getInstance()
            }
        }.onSuccess {
            startupReady = true
        }.onFailure {
            startupError = it.message ?: "Inisialisasi game gagal."
        }
    }

    if (!startupReady) {
        EntryLoadingScreen(
            ready = startupReady,
            error = startupError,
            onRetry = {
                startupReady = false
                startupError = null
                startupAttempt += 1
            },
            reducedMotion = reducedMotion
        )
        return
    }
    CyberProtocolGameContent(
        reducedMotion = reducedMotion,
        onReducedMotionChange = { enabled ->
            reducedMotion = enabled
            settingsPreferences.edit().putBoolean("reduced_motion", enabled).apply()
        }
    )
}

@Composable
private fun CyberProtocolGameContent(reducedMotion: Boolean, onReducedMotionChange: (Boolean) -> Unit) {
    val baseContext = LocalContext.current
    var selectedLanguage by remember {
        mutableStateOf(GameLanguagePreferences.load(baseContext))
    }
    val localizedContext = remember(baseContext, selectedLanguage) {
        val configuration = Configuration(baseContext.resources.configuration)
        configuration.setLocale(Locale.forLanguageTag(selectedLanguage.localeTag))
        baseContext.createConfigurationContext(configuration)
    }
    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides localizedContext.resources.configuration
    ) {
        CyberProtocolGameContentBody(
            reducedMotion = reducedMotion,
            onReducedMotionChange = onReducedMotionChange,
            selectedLanguage = selectedLanguage,
            onLanguageChange = { language ->
                GameLanguagePreferences.save(baseContext, language)
                selectedLanguage = language
            }
        )
    }
}

@Composable
private fun CyberProtocolGameContentBody(
    reducedMotion: Boolean,
    onReducedMotionChange: (Boolean) -> Unit,
    selectedLanguage: GameLanguage,
    onLanguageChange: (GameLanguage) -> Unit
) {
    val context = LocalContext.current
    val preferences = remember { context.getSharedPreferences("cyber_protocol_campaign_v2", 0) }
    val sessionViewModel: GameSessionViewModel = viewModel(
        factory = GameSessionViewModel.Factory(context)
    )
    val initialGameState = sessionViewModel.gameState
    val currentGameState = sessionViewModel.gameState
    val rx14Progress = currentGameState.caseProgress[Campaign01CaseCatalog.RX14_CASE_ID]
        ?: CaseProgress(Campaign01CaseCatalog.RX14_CASE_ID)
    val rx14CasePhase = CasePhaseEvaluator.phase(Campaign01CaseCatalog.rx14, rx14Progress)
    val rx14CaseComplete = rx14CasePhase == CasePhase.COMPLETE
    val fileSystem = remember { VirtualFileSystem(context) }
    val activeMission = currentGameState.activeMissionId?.let(Campaign01Catalog::find)
    val activeWorkspace = remember(activeMission?.id) {
        activeMission?.let(fileSystem::openMissionWorkspace)
    }
    val python = remember { PythonExecutionService() }
    val scope = rememberCoroutineScope()

    var navigation by rememberSaveable(stateSaver = GameNavigationState.Saver) {
        mutableStateOf(GameNavigationState())
    }
    val screen = navigation.currentScreen

    fun navigate(destination: GameScreen) {
        navigation = navigation.navigateTo(destination)
    }
    var storyStage by rememberSaveable {
        mutableIntStateOf(initialGameState.storyBeat.coerceIn(0, 16))
    }
    var code by remember {
        mutableStateOf(TextFieldValue(
            Campaign01Story.draftForStage(
                storyStage,
                initialGameState.savedCode["story_$storyStage"]
                    ?: preferences.getString("saved_code_$storyStage", null)
            )
        ))
    }
    // A submission is accepted only for the exact source that completed RUN successfully.
    var lastSuccessfulRunSource by rememberSaveable { mutableStateOf<String?>(null) }
    var codeOutput by rememberSaveable { mutableStateOf("KELUARAN AKAN MUNCUL DI SINI") }
    var codeFeedback by rememberSaveable { mutableStateOf("") }
    var terminalSession by rememberSaveable(stateSaver = TerminalSessionState.Saver) {
        mutableStateOf(TerminalSessionState(currentDirectory = fileSystem.initialDirectory()))
    }
    var showResetDialog by remember { mutableStateOf(false) }
    val messengerPreferences = remember { context.getSharedPreferences("cp_messenger_v1", 0) }
    val browserPreferences = remember { context.getSharedPreferences("cp_browser_session_v1", 0) }
    var browserSession by rememberSaveable(stateSaver = BrowserSessionState.Saver) {
        mutableStateOf(
            BrowserSessionState(
                authenticated = browserPreferences.getBoolean("authenticated", false)
            )
        )
    }
    var terminalReturnScreen by rememberSaveable { mutableStateOf(GameScreen.APP_HUB.name) }
    var messengerSession by rememberSaveable(stateSaver = MessengerSessionState.Saver) {
        mutableStateOf(
            MessengerSessionState(
                freyaReplies = messengerPreferences.getString("freya_replies", "").orEmpty()
                    .split("\u001F").filter { it.isNotBlank() },
                nexusReplies = messengerPreferences.getString("nexus_replies", "").orEmpty()
                    .split("\u001F").filter { it.isNotBlank() }
            )
        )
    }
    var savedCodeStatus by rememberSaveable { mutableStateOf("PERUBAHAN BELUM DISIMPAN") }
    var debriefTitle by rememberSaveable { mutableStateOf("") }
    var debriefFeedback by rememberSaveable { mutableStateOf("") }
    var debriefXp by rememberSaveable { mutableIntStateOf(0) }
    var debriefClue by rememberSaveable { mutableStateOf("") }
    var hintLevel by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(screen, storyStage) {
        sessionViewModel.refresh()
    }

    LaunchedEffect(currentGameState.storyBeat) {
        if (currentGameState.storyBeat >= 10) {
            fileSystem.installEvidenceAssets()
            sessionViewModel.ensureRecruitmentEvidence()
        }
    }
    LaunchedEffect(currentGameState.activeMissionId) {
        sessionViewModel.retireLegacyStoryMission()
    }
    LaunchedEffect(rx14CaseComplete) {
        if (rx14CaseComplete) sessionViewModel.ensureCaseCompletionReward(Campaign01CaseCatalog.rx14)
    }

    fun canRecordRx14Evidence(evidenceId: String): Boolean {
        val latest = sessionViewModel.gameState
        val definition = Campaign01CaseCatalog.rx14
        val progress = latest.caseProgress[definition.id] ?: CaseProgress(definition.id)
        return CasePhaseEvaluator.canDiscoverEvidence(definition, progress, evidenceId)
    }

    fun recordRx14Evidence(evidenceId: String) {
        if (canRecordRx14Evidence(evidenceId)) {
            sessionViewModel.discoverCaseEvidence(Campaign01CaseCatalog.rx14, evidenceId)
        }
    }

    fun saveStage(value: Int) {
        storyStage = value.coerceIn(0, 16)
        preferences.edit().putInt("story_stage", storyStage).apply()
        sessionViewModel.saveStoryBeat(storyStage)
        val savedDraft = sessionViewModel.gameState.savedCode["story_$storyStage"]
            ?: preferences.getString("saved_code_$storyStage", null)
        code = TextFieldValue(Campaign01Story.draftForStage(storyStage, savedDraft))
        lastSuccessfulRunSource = null
        codeOutput = "KELUARAN AKAN MUNCUL DI SINI"
        codeFeedback = ""
        hintLevel = 0
        savedCodeStatus = "DRAF TERSIMPAN"
    }

    fun openHub(from: GameScreen) {
        navigation = navigation.openHub(from)
        val activeMission = currentGameState.activeMissionId?.let { Campaign01Catalog.find(it) }
        terminalSession = terminalSession.copy(
            currentDirectory = activeMission?.workspaceDirectory ?: fileSystem.initialDirectory()
        )
    }

    fun appendTerminalOutput(value: String) {
        terminalSession = terminalSession.appendOutput(value)
    }

    fun downloadMissionFile(fileName: String): Boolean =
        activeWorkspace?.downloadAsset(fileName) == true

    fun submitMission(mission: com.zeroday.cyberprotocol.core.model.MissionDefinition?) {
        val sourceToVerify = code.text
        if (mission == null) {
            codeFeedback = "TIDAK ADA MISI AKTIF UNTUK DIKIRIM."
        } else if (sourceToVerify.isBlank()) {
            codeFeedback = "BELUM ADA KODE — tulis solusi terlebih dahulu."
        } else if (!MissionSubmissionPolicy.canSubmit(sourceToVerify, lastSuccessfulRunSource)) {
            codeFeedback = "JALANKAN KODE TERLEBIH DAHULU. Verifikasi hanya menerima kode yang sama dengan eksekusi terakhir yang berhasil."
        } else {
            scope.launch {
                codeFeedback = "MEMVERIFIKASI..."
                val result = withContext(Dispatchers.IO) {
                    python.submit(mission.evaluatorId ?: -1, sourceToVerify)
                }
                if (code.text != sourceToVerify) {
                    lastSuccessfulRunSource = null
                    codeFeedback = "KODE BERUBAH SELAMA VERIFIKASI — jalankan ulang versi terbaru."
                } else {
                    codeOutput = result.output.ifBlank { "(tidak ada keluaran)" }
                    if (result.error.isNotBlank()) codeOutput += "\n" + result.error
                    if (result.passed) {
                        val firstCompletion = sessionViewModel.completeMission(
                            mission.id,
                            mission.rewardXp,
                            mission.requiredClearance,
                            competencyTrack = if (mission.primaryArchetype == MissionArchetype.TECHNICAL) {
                                CompetencyTrack.TECHNICAL
                            } else {
                                CompetencyTrack.INTELLIGENCE
                            }
                        )
                        sessionViewModel.recordEvidence(
                            com.zeroday.cyberprotocol.core.model.EvidenceRecord(
                                id = "evidence_" + mission.id,
                                title = mission.title,
                                summary = "Hasil tugas latihan berhasil diverifikasi oleh evaluator A.E.G.I.S.",
                                source = "A.E.G.I.S. / Python evaluator " + mission.evaluatorId,
                                status = com.zeroday.cyberprotocol.core.model.EvidenceStatus.CONFIRMED,
                                missionId = mission.id
                            )
                        )
                        mission.writableFiles.firstOrNull()?.let { fileName ->
                            fileSystem.openMissionWorkspace(mission).writeFile(fileName, sourceToVerify)
                        }
                        preferences.edit().putString("saved_code_$storyStage", sourceToVerify).apply()
                        sessionViewModel.saveCode(
                            "story_$storyStage", sourceToVerify, "code_checkpoint_story_$storyStage"
                        )
                        savedCodeStatus = "TERSIMPAN PADA CHECKPOINT"
                        saveStage(storyStage + 1)
                        debriefTitle = mission.title
                        debriefFeedback = result.feedback
                        debriefXp = if (firstCompletion) mission.rewardXp else 0
                        debriefClue = mission.nextClue.orEmpty()
                        navigate(GameScreen.DEBRIEF)
                    } else {
                        codeFeedback = result.feedback.ifBlank {
                            "BELUM TERVERIFIKASI — periksa kembali kode dan tujuan misi."
                        }
                    }
                }
            }
        }
    }

    fun executeTerminalCommand(
        missionOverride: com.zeroday.cyberprotocol.core.model.MissionDefinition? = null,
        onPythonResult: ((String, String) -> Unit)? = null
    ) {
        val input = terminalSession.command.trim()
        if (input.isEmpty() || terminalSession.busy) return
        terminalSession = terminalSession.copy(command = "")
        val activeMission = missionOverride
            ?: currentGameState.activeMissionId?.let { Campaign01Catalog.find(it) }
        val response = if (activeMission == null) {
            fileSystem.execute(input, terminalSession.currentDirectory)
        } else {
            fileSystem.execute(
                input,
                terminalSession.currentDirectory,
                activeMission.workspaceDirectory,
                activeMission.allowedFiles,
                activeMission.writableFiles
            )
        }
        terminalSession = terminalSession.copy(currentDirectory = response.currentDirectory)
        if (response.clearScreen) {
            terminalSession = terminalSession.clearOutput()
            return
        }
        appendTerminalOutput("\n$ $input\n")
        val output = response.output
        if (activeMission == null) {
            com.zeroday.cyberprotocol.data.EvidenceAssetCatalog
                .sourceAssetReadByTerminal(input, output)
                ?.let { recordRx14Evidence(it.evidenceId) }
        }
        if (output.startsWith("PYTHON_LAUNCH:")) {
            val fileName = output.removePrefix("PYTHON_LAUNCH:")
            val source = if (activeMission == null) {
                fileSystem.readFile(fileName, terminalSession.currentDirectory)
            } else {
                fileSystem.readFile(fileName, terminalSession.currentDirectory, activeMission.workspaceDirectory, activeMission.allowedFiles)
            }
            if (source == null) {
                val error = "python: tidak dapat membuka '$fileName': file tidak ditemukan"
                appendTerminalOutput(error + "\n")
                onPythonResult?.invoke("", error)
            } else {
                if (activeMission != null && fileName in activeMission.writableFiles) {
                    lastSuccessfulRunSource = null
                }
                terminalSession = terminalSession.copy(busy = true)
                scope.launch {
                    val missionWorkspace = activeMission?.let { fileSystem.openMissionWorkspace(it) }
                    val result = withContext(Dispatchers.IO) {
                        python.run(source, missionWorkspace, activeMission?.runtimeVariables.orEmpty())
                    }
                    val visibleOutput = result.output.ifBlank { "(tidak ada keluaran)" }
                    appendTerminalOutput(visibleOutput + "\n")
                    if (result.error.isNotBlank()) appendTerminalOutput("KESALAHAN: " + result.error + "\n")
                    terminalSession = terminalSession.copy(busy = false)
                    if (activeMission != null && fileName in activeMission.writableFiles &&
                        result.error.isBlank() && source == code.text
                    ) {
                        lastSuccessfulRunSource = source
                        codeOutput = result.output.ifBlank { "(tidak ada keluaran)" }
                        codeFeedback = "EKSEKUSI TERMINAL BERHASIL — kode ini siap dikirim melalui Browser Mission Control."
                    }
                    onPythonResult?.invoke(result.output, result.error)
                }
            }
        } else if (output.isNotBlank()) {
            appendTerminalOutput(output + "\n")
            onPythonResult?.invoke("", output)
        }
    }

    fun runMissionCode(
        mission: com.zeroday.cyberprotocol.core.model.MissionDefinition,
        source: String
    ) {
        if (terminalSession.busy) {
            codeFeedback = "TERMINAL MASIH MENJALANKAN PERINTAH LAIN. Coba lagi setelah selesai."
            return
        }
        val fileName = mission.writableFiles.firstOrNull()
        if (fileName == null) {
            codeFeedback = "MISI INI BELUM MENYEDIAKAN FILE KODE YANG DAPAT DITULIS."
            return
        }
        val invocation = MissionWorkspacePolicy.pythonCommand(fileName)
        if (invocation == null) {
            codeFeedback = "NAMA FILE MISI TIDAK VALID UNTUK DIJALANKAN."
            return
        }
        val workspace = fileSystem.openMissionWorkspace(mission)
        if (!workspace.writeFile(fileName, source)) {
            codeFeedback = "KODE TIDAK DAPAT DISIMPAN KE RUANG KERJA MISI."
            return
        }
        terminalSession = terminalSession.copy(
            currentDirectory = mission.workspaceDirectory,
            command = invocation
        )
        lastSuccessfulRunSource = null
        codeFeedback = "MENJALANKAN MELALUI A.E.G.I.S. TERMINAL..."
        terminalReturnScreen = GameScreen.STORY.name
        navigate(GameScreen.TERMINAL)
        executeTerminalCommand(mission) { output, error ->
            codeOutput = output.ifBlank { "(tidak ada keluaran)" }
            if (error.isNotBlank()) codeOutput += "\n" + error
            if (error.isBlank() && code.text == source) {
                lastSuccessfulRunSource = source
                codeFeedback = "EKSEKUSI TERMINAL BERHASIL — kode ini siap dikirim melalui Browser Mission Control."
            } else {
                lastSuccessfulRunSource = null
                codeFeedback = if (error.isNotBlank()) {
                    "EKSEKUSI TERMINAL GAGAL — perbaiki kesalahan sebelum verifikasi."
                } else {
                    "KODE BERUBAH SAAT DIJALANKAN — jalankan ulang versi terbaru."
                }
            }
        }
    }

    BackHandler(enabled = screen != GameScreen.MAIN_MENU) {
        if (screen == GameScreen.BROWSER) {
            navigate(runCatching { GameScreen.valueOf(browserSession.returnScreen) }.getOrDefault(GameScreen.APP_HUB))
        } else if (screen == GameScreen.TERMINAL) {
            navigate(runCatching { GameScreen.valueOf(terminalReturnScreen) }.getOrDefault(GameScreen.APP_HUB))
        } else if (screen == GameScreen.MESSENGER) {
            if (messengerSession.selectedContact.isNotBlank()) {
                messengerSession = messengerSession.copy(selectedContact = "")
            } else {
                navigate(runCatching { GameScreen.valueOf(messengerSession.returnScreen) }.getOrDefault(GameScreen.APP_HUB))
            }
        } else {
            navigation = navigation.back(storyStage, messengerHasSelectedContact = false)
        }
    }

    when (screen) {
        GameScreen.MAIN_MENU -> MainMenuScreen(
            onContinue = { navigate(GameScreen.CAMPAIGN) },
            onNewCampaign = { showResetDialog = true },
            onHub = { openHub(GameScreen.MAIN_MENU) },
            onProfile = { navigate(GameScreen.PROFILE) },
            onArchives = { navigate(GameScreen.ARCHIVES) },
            onAchievements = { navigate(GameScreen.ACHIEVEMENTS) },
            onSettings = { navigate(GameScreen.SETTINGS) },
            onLanguage = { navigate(GameScreen.LANGUAGE) }
        )
        GameScreen.CAMPAIGN -> {
            if (!currentGameState.recruitmentNotificationOpened && storyStage < 2) {
                RecruitmentNotificationScreen(
                    onOpenMessage = {
                        sessionViewModel.openRecruitmentInvitation()
                        if (storyStage < 2) saveStage(2)
                        messengerSession = messengerSession.copy(selectedContact = "AEGIS")
                        messengerSession = messengerSession.copy(returnScreen = GameScreen.CAMPAIGN.name)
                        navigate(GameScreen.MESSENGER)
                    }
                )
            } else {
                CampaignScreen(
                    storyStage = storyStage,
                    onBack = { navigate(GameScreen.MAIN_MENU) },
                    onContinue = { navigate(if (storyStage < 16) GameScreen.STORY else GameScreen.APP_HUB) },
                    onHub = { openHub(GameScreen.CAMPAIGN) }
                )
            }
        }
        GameScreen.STORY -> {
            val beatIndex = storyStage.coerceIn(2, 15)
            val authoredMission = when (beatIndex) {
                4 -> Campaign01Catalog.find("first_contact.signal")
                5 -> Campaign01Catalog.find("first_contact.identity")
                else -> null
            }
            LaunchedEffect(authoredMission?.id) {
                authoredMission?.let { mission ->
                    val latest = sessionViewModel.refresh()
                    if (mission.id !in latest.completedMissionIds && latest.activeMissionId != mission.id) {
                        sessionViewModel.startMission(mission.id)
                    }
                }
            }
            StoryScreen(
                beat = Campaign01StoryLocalization.localizedBeat(beatIndex - 2, selectedLanguage),
                stage = beatIndex,
                mission = authoredMission,
                freyaContactUnlocked = currentGameState.freyaContactOpened,
                caseComplete = rx14CaseComplete,
                casePhase = rx14CasePhase,
                onOpenOperations = {
                    navigation = navigation.copy(hubReturnScreen = GameScreen.STORY)
                    navigate(GameScreen.OPERATIONS)
                },
                onOpenAnalysis = {
                    navigation = navigation.copy(hubReturnScreen = GameScreen.STORY)
                    navigate(GameScreen.ANALYSIS)
                },
                hintLevel = hintLevel,
                onRequestHint = { hintLevel = (hintLevel + 1).coerceAtMost(3) },
                code = code,
                onCodeChange = {
                    if (it.text.length <= 12_000) {
                        code = it
                        lastSuccessfulRunSource = null
                        savedCodeStatus = "PERUBAHAN BELUM DISIMPAN"
                    }
                },
                output = codeOutput,
                feedback = codeFeedback,
                saveStatus = savedCodeStatus,
                canSubmit = authoredMission != null &&
                    MissionSubmissionPolicy.canSubmit(code.text, lastSuccessfulRunSource) &&
                    codeFeedback != "MEMVERIFIKASI...",
                onSubmit = { submitMission(authoredMission) },
                onSaveCode = {
                    authoredMission?.let { mission ->
                        fileSystem.openMissionWorkspace(mission).writeFile(mission.writableFiles.first(), code.text)
                    }
                    preferences.edit().putString("saved_code_$storyStage", code.text).apply()
                    sessionViewModel.saveCode(
                        "story_$storyStage", code.text, "code_saved_story_$storyStage"
                    )
                    savedCodeStatus = "TERSIMPAN"
                },
                onRun = {
                    val sourceToRun = code.text
                    val mission = authoredMission
                    if (sourceToRun.isBlank()) {
                        lastSuccessfulRunSource = null
                        codeFeedback = "EDITOR KOSONG — tulis kode terlebih dahulu."
                    } else if (mission == null) {
                        codeFeedback = "TIDAK ADA RUANG KERJA MISI AKTIF."
                    } else {
                        runMissionCode(mission, sourceToRun)
                    }
                },
                onContinue = {
                    if (storyStage >= 15) {
                        saveStage(16)
                        navigate(GameScreen.CAMPAIGN)
                    } else {
                        saveStage(storyStage + 1)
                    }
                },
                onBack = { navigate(GameScreen.CAMPAIGN) },
                onHub = { openHub(GameScreen.STORY) }
            )
            if (storyStage == 4 &&
                currentGameState.activeMissionId == "first_contact.signal" &&
                !currentGameState.freyaContactOpened
            ) {
                FreyaNotificationDialog(
                    onOpenMessage = {
                        sessionViewModel.openFreyaIntroduction()
                        messengerSession = messengerSession.copy(selectedContact = "FREYA")
                        messengerSession = messengerSession.copy(returnScreen = GameScreen.STORY.name)
                        navigate(GameScreen.MESSENGER)
                    }
                )
            }
        }
        GameScreen.DEBRIEF -> MissionDebriefScreen(
            title = debriefTitle,
            feedback = debriefFeedback,
            xpAwarded = debriefXp,
            rank = currentGameState.rank.replace('_', ' '),
            totalExperience = currentGameState.experience,
            nextClue = debriefClue,
            onContinue = {
                navigate(GameScreen.STORY)
            }
        )
        GameScreen.APP_HUB -> AppHubScreen(
            gameState = currentGameState,
            onClose = { navigate(navigation.hubReturnScreen) },
            onOpen = { target ->
                if (target == GameScreen.MESSENGER) messengerSession = messengerSession.copy(returnScreen = GameScreen.APP_HUB.name)
                if (target == GameScreen.BROWSER) browserSession = browserSession.copy(returnScreen = GameScreen.APP_HUB.name)
                if (target == GameScreen.TERMINAL) terminalReturnScreen = GameScreen.APP_HUB.name
                navigate(target)
            }
        )
        GameScreen.MESSENGER -> MessengerScreen(
            selectedContact = messengerSession.selectedContact,
            onSelectContact = { contact -> messengerSession = messengerSession.copy(selectedContact = contact) },
            onClose = {
                navigate(runCatching { GameScreen.valueOf(messengerSession.returnScreen) }.getOrDefault(GameScreen.APP_HUB))
            },
            recruitmentInvitation = currentGameState.recruitmentNotificationOpened,
            freyaContactUnlocked = currentGameState.freyaContactOpened,
            nexusUnlocked = currentGameState.nexusContactUnlocked,
            activeMissionTitle = currentGameState.activeMissionId?.let { Campaign01Catalog.find(it)?.title },
            completedMissionCount = currentGameState.completedMissionIds.size,
            evidenceAssetsAvailable = currentGameState.storyBeat >= 10,
            onReadEvidenceAsset = fileSystem::readEvidenceAsset,
            canRecordEvidence = ::canRecordRx14Evidence,
            onRecordEvidence = ::recordRx14Evidence,
            onOpenMissionLink = {
                if (storyStage < 2) saveStage(2)
                messengerSession = messengerSession.copy(selectedContact = "")
                navigate(GameScreen.STORY)
            },
            onOpenNexusLink = {
                sessionViewModel.unlockNexusContact()
                messengerSession = messengerSession.copy(selectedContact = "NEXUS")
            },
            onOpenIntranetLink = {
                browserSession = browserSession.copy(returnScreen = GameScreen.MESSENGER.name)
                browserSession = browserSession.copy(page = browserSession.initialPage())
                navigate(GameScreen.BROWSER)
            },
            onDownloadMissionFile = ::downloadMissionFile,
            replies = messengerSession.repliesFor(messengerSession.selectedContact),
            onReply = { reply ->
                val contact = messengerSession.selectedContact
                val updatedSession = messengerSession.appendReply(contact, reply)
                messengerSession = updatedSession
                when (contact) {
                    "FREYA" -> messengerPreferences.edit()
                        .putString("freya_replies", updatedSession.freyaReplies.joinToString("\u001F"))
                        .apply()
                    "NEXUS" -> messengerPreferences.edit()
                        .putString("nexus_replies", updatedSession.nexusReplies.joinToString("\u001F"))
                        .apply()
                }
            }
        )
        GameScreen.BROWSER -> AegisBrowserScreen(
            authenticated = browserSession.authenticated,
            page = browserSession.page,
            mission = activeMission,
            gameState = currentGameState,
            evidence = currentGameState.evidence,
            caseDefinition = Campaign01CaseCatalog.rx14,
            caseProgress = rx14Progress,
            downloadedFiles = activeWorkspace?.listFiles()?.toSet().orEmpty(),
            onReadEvidenceAsset = fileSystem::readEvidenceAsset,
            canRecordEvidence = ::canRecordRx14Evidence,
            onRecordEvidence = ::recordRx14Evidence,
            onPageChange = { browserSession = browserSession.copy(page = it) },
            onAuthenticate = { agentId, accessCode ->
                val accepted = AegisIntranetAccess.authenticate(agentId, accessCode)
                if (accepted) {
                    browserSession = browserSession.copy(authenticated = true, page = "home")
                    browserPreferences.edit().putBoolean("authenticated", true).apply()
                }
                accepted
            },
            onDownloadFile = ::downloadMissionFile,
            onOpenEditor = {
                activeMission?.id?.let(Campaign01Catalog::storyStageForMission)?.let(::saveStage)
                navigate(GameScreen.STORY)
            },
            onSubmitMission = { submitMission(activeMission) },
            canSubmitMission = activeMission != null &&
                MissionSubmissionPolicy.canSubmit(code.text, lastSuccessfulRunSource),
            submissionStatus = codeFeedback,
            onClose = {
                navigate(runCatching { GameScreen.valueOf(browserSession.returnScreen) }.getOrDefault(GameScreen.APP_HUB))
            }
        )
        GameScreen.TERMINAL -> TerminalScreen(
            currentDirectory = terminalSession.currentDirectory,
            workspaceLabel = activeMission?.title
                ?.let { "MISSION WORKSPACE  /  $it" } ?: "PERSONAL WORKSPACE  /  A.E.G.I.S.",
            command = terminalSession.command,
            onCommandChange = { terminalSession = terminalSession.copy(command = it) },
            output = terminalSession.output,
            busy = terminalSession.busy,
            onExecute = { executeTerminalCommand() },
            onBack = {
                navigate(runCatching { GameScreen.valueOf(terminalReturnScreen) }.getOrDefault(GameScreen.APP_HUB))
            }
        )
        GameScreen.DATABASE -> IntelligenceDatabaseScreen(
            gameState = currentGameState,
            caseDefinition = Campaign01CaseCatalog.rx14,
            caseProgress = currentGameState.caseProgress[Campaign01CaseCatalog.RX14_CASE_ID]
                ?: CaseProgress(Campaign01CaseCatalog.RX14_CASE_ID),
            onBack = { navigate(GameScreen.APP_HUB) }
        )
        GameScreen.DOSSIER -> PersonnelDossierScreen(
            gameState = currentGameState,
            caseDefinition = Campaign01CaseCatalog.rx14,
            caseProgress = currentGameState.caseProgress[Campaign01CaseCatalog.RX14_CASE_ID]
                ?: CaseProgress(Campaign01CaseCatalog.RX14_CASE_ID),
            onBack = { navigate(GameScreen.APP_HUB) }
        )
        GameScreen.ANALYSIS -> AnalysisScreen(
            gameState = currentGameState,
            caseDefinition = Campaign01CaseCatalog.rx14,
            caseProgress = currentGameState.caseProgress[Campaign01CaseCatalog.RX14_CASE_ID]
                ?: CaseProgress(Campaign01CaseCatalog.RX14_CASE_ID),
            onBack = { navigate(GameScreen.APP_HUB) },
            onAnalyzeCaseEvidence = { evidenceId ->
                sessionViewModel.analyzeCaseEvidence(Campaign01CaseCatalog.rx14, evidenceId)
            },
            onSelectAnalysisConclusion = { conclusionId ->
                sessionViewModel.selectCaseAnalysisConclusion(Campaign01CaseCatalog.rx14, conclusionId)
            },
            onReviewEvidence = { evidenceId, status, reason ->
                sessionViewModel.reviewEvidence(evidenceId, status, reason)
            }
        )
        GameScreen.OPERATIONS -> OperationsScreen(
            gameState = currentGameState,
            caseDefinition = Campaign01CaseCatalog.rx14,
            caseProgress = currentGameState.caseProgress[Campaign01CaseCatalog.RX14_CASE_ID]
                ?: com.zeroday.cyberprotocol.core.model.CaseProgress(Campaign01CaseCatalog.RX14_CASE_ID),
            evidenceRecords = currentGameState.evidence,
            onBack = { navigate(GameScreen.APP_HUB) },
            onReadSource = { evidenceId ->
                val asset = com.zeroday.cyberprotocol.data.EvidenceAssetCatalog.assets
                    .firstOrNull { it.evidenceId == evidenceId }
                asset?.let { fileSystem.readEvidenceAsset(it.id) }
            },
            onAcknowledgeBriefing = {
                sessionViewModel.acknowledgeCaseBriefing(Campaign01CaseCatalog.rx14)
            },
            onDiscoverEvidence = { evidenceId ->
                sessionViewModel.discoverCaseEvidence(Campaign01CaseCatalog.rx14, evidenceId)
            },
            onOpenAnalysis = {
                navigation = navigation.copy(hubReturnScreen = GameScreen.OPERATIONS)
                navigate(GameScreen.ANALYSIS)
            },
            onDecisionRationaleChange = { rationale ->
                sessionViewModel.saveCaseDecisionRationale(Campaign01CaseCatalog.rx14, rationale.take(600))
            },
            onSelectDecision = { decisionId, rationale ->
                sessionViewModel.selectCaseDecision(Campaign01CaseCatalog.rx14, decisionId, rationale)
            },
            onAcknowledgeDebrief = {
                sessionViewModel.acknowledgeCaseDebrief(Campaign01CaseCatalog.rx14)
            }
        )
        GameScreen.PROFILE -> InfoAppScreen(
            "PROFIL AGEN", "A.E.G.I.S. / IDENTITAS PERSONEL",
            buildString {
                append("IDENTITAS: AGEN MAGANG\n")
                append("RANK: ").append(currentGameState.rank.replace('_', ' ')).append("\n")
                append("PENGALAMAN: ").append(currentGameState.experience).append(" XP\n")
                append("CLEARANCE: ").append(currentGameState.clearance).append("\n")
                append("KAMPANYE: ").append(currentGameState.campaignId).append("\n")
                append("MISI SELESAI: ").append(currentGameState.completedMissionIds.size).append("\n")
                append("BUKTI TERCATAT: ").append(currentGameState.evidence.size).append("\n")
                append("CHECKPOINT: ").append(currentGameState.lastCheckpoint).append("\n")
                val nextRankXp = com.zeroday.cyberprotocol.domain.usecase.ProgressionRules.nextRankThreshold(currentGameState.experience)
                if (nextRankXp == null) append("RANK MAKSIMUM TERCAPAI")
                else append("MENUJU RANK BERIKUTNYA: ").append(nextRankXp - currentGameState.experience).append(" XP")
            },
            CyberAssets.document, { navigate(GameScreen.MAIN_MENU) }
        )
        GameScreen.ARCHIVES -> InfoAppScreen(
            "ARSIP", "A.E.G.I.S. / RIWAYAT OPERASI",
            buildString {
                append("MISI TERVERIFIKASI\n")
                Campaign01Catalog.missions.filter { it.id in currentGameState.completedMissionIds }
                    .forEach { append("• ").append(it.title).append("\n") }
                if (currentGameState.completedMissionIds.isEmpty()) append("Belum ada misi selesai.\n")
                append("\nBUKTI TERSIMPAN\n")
                currentGameState.evidence.forEach {
                    append("• ").append(it.title).append(" / ").append(it.status.name).append("\n")
                    append("  SUMBER: ").append(it.source).append("\n")
                }
                if (currentGameState.evidence.isEmpty()) append("Belum ada bukti tersimpan.\n")
            },
            CyberAssets.document, { navigate(GameScreen.MAIN_MENU) }
        )
        GameScreen.ACHIEVEMENTS -> InfoAppScreen(
            "PENCAPAIAN", "A.E.G.I.S. / PENGHARGAAN",
            buildString {
                val awards = listOf(
                    "FIRST_SIGNAL" to ("Sinyal Pertama" to "first_contact.signal"),
                    "IDENTITY_VERIFIED" to ("Identitas Terverifikasi" to "first_contact.identity"),
                    "AUDIT_COMPLETE" to ("Audit Jejak Selesai" to "first_contact.audit"),
                    "INVENTORY_COMPLETE" to ("Inventaris Rekaman Selesai" to "first_contact.inventory"),
                    "TRACE_COMPLETE" to ("Penelusuran Referensi Selesai" to "first_contact.trace"),
                    "COMPARE_COMPLETE" to ("Perbandingan Dua Sumber Selesai" to "first_contact.compare"),
                    "NORMALIZE_COMPLETE" to ("Normalisasi Waktu Selesai" to "first_contact.normalize"),
                    "DELTA_COMPLETE" to ("Validasi Selisih Selesai" to "first_contact.validate_delta")
                )
                awards.forEach { (_, award) ->
                    val earned = award.second in currentGameState.completedMissionIds
                    append(if (earned) "[DIPEROLEH] " else "[TERKUNCI] ")
                    append(award.first).append("\n")
                }
                append(if (currentGameState.storyBeat >= 16) "[DIPEROLEH] First Contact Selesai" else "[TERKUNCI] First Contact Selesai")
            },
            CyberAssets.investigation, { navigate(GameScreen.MAIN_MENU) }
        )
        GameScreen.SETTINGS -> SettingsScreen(
            reducedMotion = reducedMotion,
            onReducedMotionChange = onReducedMotionChange,
            onBack = { navigate(GameScreen.MAIN_MENU) }
        )
        GameScreen.LANGUAGE -> LanguageScreen(
            selectedLanguage = selectedLanguage,
            onLanguageChange = onLanguageChange,
            onBack = { navigate(GameScreen.MAIN_MENU) }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("MULAI KAMPANYE BARU?") },
            text = { Text("Progres cerita kampanye akan kembali ke awal. File di Ruang Kerja Pribadi akan tetap tersimpan.") },
            confirmButton = {
                TextButton(onClick = {
                    showResetDialog = false
                    sessionViewModel.resetCampaign()
                    preferences.edit().putInt("story_stage", 0).apply()
                    saveStage(0)
                    navigate(GameScreen.CAMPAIGN)
                }) { Text("MULAI") }
            },
            dismissButton = { TextButton(onClick = { showResetDialog = false }) { Text("BATAL") } }
        )
    }
}

@Composable
private fun MainMenuScreen(onContinue: () -> Unit, onNewCampaign: () -> Unit, onHub: () -> Unit, onProfile: () -> Unit, onArchives: () -> Unit, onAchievements: () -> Unit, onSettings: () -> Unit, onLanguage: () -> Unit) {
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
            Text(stringResource(R.string.game_motto),
                color = Color(0xFF8AA8AE), fontSize = 10.sp, lineHeight = 17.sp,
                letterSpacing = 1.2.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(Modifier.height(46.dp))
            Surface(color = Color(0xFF0B2028).copy(alpha = 0.82f), shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, Color(0xFF28515B)), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    MenuButton(stringResource(R.string.menu_continue_campaign), stringResource(R.string.menu_continue_campaign_desc), onContinue, true, CyberAssets.home)
                    MenuButton(stringResource(R.string.menu_new_campaign), stringResource(R.string.menu_new_campaign_desc), onNewCampaign, icon = CyberAssets.intelligence)
                    MenuButton(stringResource(R.string.menu_app_hub), stringResource(R.string.menu_app_hub_desc), onHub, icon = CyberAssets.connection)
                    MenuButton(stringResource(R.string.menu_profile), stringResource(R.string.menu_profile_desc), onProfile, icon = CyberAssets.profile)
                    MenuButton(stringResource(R.string.menu_archives), stringResource(R.string.menu_archives_desc), onArchives, icon = CyberAssets.archive)
                    MenuButton(stringResource(R.string.menu_achievements), stringResource(R.string.menu_achievements_desc), onAchievements, icon = CyberAssets.achievements)
                    MenuButton(stringResource(R.string.menu_settings), stringResource(R.string.menu_settings_desc), onSettings, icon = CyberAssets.settings)
                    MenuButton(stringResource(R.string.menu_language), stringResource(R.string.menu_language_desc), onLanguage, icon = CyberAssets.document)
                }
            }
            Spacer(Modifier.height(30.dp))
            Text(stringResource(R.string.menu_footer),
                color = Color(0xFF55747A), fontSize = 9.sp, letterSpacing = 1.sp)
        }
    }
}

@Composable
private fun MenuButton(title: String, subtitle: String, onClick: () -> Unit, primary: Boolean = false, icon: Int? = null) {
    Surface(onClick = onClick, color = if (primary) Color(0xFF12404A) else Color(0xFF0C2931),
        shape = RoundedCornerShape(3.dp),
        border = BorderStroke(1.dp, if (primary) Color(0xFF5A9BA3) else Color(0xFF25434A)),
        modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(horizontal = 14.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (icon != null) androidx.compose.foundation.Image(
                painterResource(icon), null, Modifier.size(34.dp),
                alpha = if (primary) 1f else 0.88f
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                Text(subtitle, color = Color(0xFF9AB4B8), fontSize = 11.sp)
            }
            Text("›", color = Color(0xFFBCE8EA), fontSize = 26.sp)
        }
    }
}

@Composable
private fun FreyaNotificationDialog(onOpenMessage: () -> Unit) {
    Dialog(onDismissRequest = {}) {
        Surface(
            color = Color(0xFF202C33),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color(0xFF3B514B)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(color = Color(0xFF315448), shape = RoundedCornerShape(50)) {
                        Text("FK", Modifier.padding(11.dp), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Column(Modifier.weight(1f)) {
                        Text("PESAN AMAN", color = Color(0xFFE6E9ED),
                            fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        Text("sekarang", color = Color(0xFF9AA2AC), fontSize = 10.sp)
                    }
                }
                Text("Freya Knox", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Halo. Aku Freya. Aku akan mendampingimu selama misi orientasi A.E.G.I.S.",
                    color = Color(0xFFD0D5DC), fontSize = 13.sp, lineHeight = 19.sp
                )
                Button(
                    onClick = onOpenMessage,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF17634F))
                ) {
                    Text("BUKA PESAN")
                }
            }
        }
    }
}

@Composable
private fun RecruitmentNotificationScreen(onOpenMessage: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black).padding(horizontal = 22.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 76.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(" ", color = Color.White, fontSize = 20.sp)
            Surface(
                onClick = onOpenMessage,
                color = Color(0xFF20242A),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, Color(0xFF343A42)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Surface(color = Color(0xFF285C4B), shape = RoundedCornerShape(9.dp)) {
                            Text("P", Modifier.padding(horizontal = 11.dp, vertical = 8.dp),
                                color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        }
                        Text("PESAN AMAN", color = Color(0xFFE6E9ED), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Spacer(Modifier.weight(1f))
                        Text("sekarang", color = Color(0xFF9AA2AC), fontSize = 10.sp)
                    }
                    Text("A.E.G.I.S. / Recruitment Office", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("Undangan resmi: permintaan perekrutan.", color = Color(0xFFD0D5DC), fontSize = 13.sp)
                }
            }
            Text(
                "Ketuk notifikasi untuk membuka pesan.",
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF69727E),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun CampaignScreen(storyStage: Int, onBack: () -> Unit, onContinue: () -> Unit, onHub: () -> Unit) {
    val completedBeats = (storyStage - 2).coerceIn(0, 14)
    val progress = completedBeats / 14f
    val milestones = listOf(
        "ORIENTASI A.E.G.I.S.", "BERKAS RAHASIA 001", "SINYAL PERTAMA",
        "VERIFIKASI IDENTITAS", "STATUS RECRUIT", "SELISIH PERTAMA",
        "PEMERIKSAAN LOG", "INVENTARIS SUMBER", "LAPORAN AWAL",
        "PEMERIKSAAN RX-14", "PERBANDINGAN SUMBER", "ANALISIS SELISIH",
        "KEPUTUSAN OPERASIONAL", "LAPORAN INVESTIGASI"
    )

    GamePage("KAMPANYE", "A.E.G.I.S. / OPERASI CERITA", onBack, onHub) {
        Box(
            Modifier.fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF202A34), Color(0xFF111820), Color(0xFF321923))
                    ),
                    RoundedCornerShape(18.dp)
                )
                .padding(20.dp)
        ) {
            Text(
                "01",
                modifier = Modifier.align(Alignment.TopEnd),
                color = Color(0x26FFFFFF),
                fontSize = 104.sp,
                fontWeight = FontWeight.Black
            )
            Column(
                Modifier.fillMaxWidth().padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Text("A.E.G.I.S.  /  CAMPAIGN 01", color = Color(0xFF9AB4B8),
                    fontSize = 9.sp, letterSpacing = 1.8.sp, fontWeight = FontWeight.Bold)
                Text("THE RECRUITMENT", color = Color.White,
                    fontSize = 26.sp, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
                Text("CHAPTER 001  ·  FIRST CONTACT", color = Color(0xFFE0A2AD),
                    fontSize = 10.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Sebuah undangan telah membuka pintu menuju dunia yang selama ini tersembunyi.",
                    color = Color(0xFFD0D8DE), fontSize = 13.sp, lineHeight = 20.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFF4B2732), shape = RoundedCornerShape(50)) {
                        Text(
                            if (storyStage >= 16) "CHAPTER COMPLETE" else "ACTIVE OPERATION",
                            Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            color = Color(0xFFFFDCE2), fontSize = 8.sp, fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text("${completedBeats} / 14", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        GamePanel {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("PROGRES CHAPTER", color = CyberPalette.SteelGray,
                        fontSize = 9.sp, letterSpacing = 1.4.sp, fontWeight = FontWeight.Bold)
                    Text(if (storyStage >= 16) "LAPORAN INVESTIGASI SELESAI" else "FIRST CONTACT",
                        color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Text("${(progress * 100).toInt()}%", color = CyberPalette.Crimson,
                    fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(6.dp),
                color = CyberPalette.Crimson,
                trackColor = CyberPalette.Divider
            )
            milestones.forEachIndexed { index, title ->
                val complete = index < completedBeats
                val current = index == completedBeats && storyStage < 16
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        color = when {
                            complete -> Color(0xFF24533F)
                            current -> Color(0xFF542632)
                            else -> Color(0xFF252A31)
                        },
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            if (complete) "✓" else "%02d".format(index + 1),
                            Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                            color = if (complete || current) Color.White else CyberPalette.SteelGray,
                            fontSize = 10.sp, fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        title,
                        color = if (complete || current) Color.White else CyberPalette.SteelGray,
                        fontSize = 11.sp,
                        fontWeight = if (current) FontWeight.Bold else FontWeight.Medium
                    )
                    Spacer(Modifier.weight(1f))
                    if (complete) Text("SELESAI", color = CyberPalette.Success, fontSize = 8.sp)
                    else if (current) Text("BERIKUTNYA", color = CyberPalette.Crimson, fontSize = 8.sp)
                    else Text("TERKUNCI", color = CyberPalette.SteelGray, fontSize = 8.sp)
                }
            }
            CPPrimaryButton(if (storyStage >= 16) "BUKA PUSAT A.E.G.I.S." else "LANJUTKAN CERITA", onContinue)
        }
        Text(
            "Chapter baru dan operasi lanjutan akan terbuka melalui perkembangan cerita. Setiap bukti dan akses memiliki sumber serta otorisasi.",
            color = CyberPalette.SteelGray, fontSize = 10.sp, lineHeight = 16.sp
        )
    }
}

@Composable
private fun StoryScreen(
    beat: StoryBeat, stage: Int, mission: com.zeroday.cyberprotocol.core.model.MissionDefinition?,
    freyaContactUnlocked: Boolean,
    caseComplete: Boolean,
    casePhase: CasePhase,
    onOpenOperations: () -> Unit,
    onOpenAnalysis: () -> Unit,
    hintLevel: Int, onRequestHint: () -> Unit,
    code: TextFieldValue, onCodeChange: (TextFieldValue) -> Unit,
    output: String, feedback: String, saveStatus: String, canSubmit: Boolean,
    onSubmit: () -> Unit, onSaveCode: () -> Unit,
    onRun: () -> Unit, onContinue: () -> Unit,
    onBack: () -> Unit, onHub: () -> Unit
) {
    GamePage("KONTAK PERTAMA", "BAB 001  /  REKRUTMEN", onBack, onHub) {
        Box(
            Modifier.fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        when (stage) {
                            2 -> listOf(Color(0xFF172B36), Color(0xFF101820), Color(0xFF241B27))
                            3 -> listOf(Color(0xFF292331), Color(0xFF171820), Color(0xFF30202A))
                            4, 5 -> listOf(Color(0xFF102B2A), Color(0xFF111A20), Color(0xFF1C2631))
                            else -> listOf(Color(0xFF29202A), Color(0xFF171820), Color(0xFF34202A))
                        }
                    ),
                    RoundedCornerShape(18.dp)
                )
                .padding(21.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(beat.eyebrow, color = Color(0xFFE0A2AD), fontSize = 9.sp,
                        letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    Text("SCENE ${(stage - 1).toString().padStart(2, '0')}",
                        color = Color(0xFF9AB4B8), fontSize = 9.sp, letterSpacing = 1.sp)
                }
                Text(beat.title, color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Black)
                HorizontalDivider(color = Color(0x554C5A66))
                Text(beat.body, color = Color(0xFFD1D9DF), lineHeight = 23.sp, fontSize = 13.sp)
            }
        }
        if (mission != null) {
            GamePanel {
                Text(
                    if (stage <= 5 && freyaContactUnlocked) "FREYA KNOX  /  BANTUAN PELATIHAN"
                    else if (stage <= 5) "A.E.G.I.S.  /  PANDUAN MISI"
                    else "A.E.G.I.S.  /  DATA REVIEW",
                    color = Color(0xFF83C7D0),
                    fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(mission?.briefing.orEmpty(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                mission?.objectives?.forEachIndexed { index, objective ->
                    Text("${index + 1}. ${objective.title}", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text(objective.description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                }
                mission?.runtimeVariables?.takeIf { it.isNotEmpty() }?.let { datasets ->
                    Text("DATASET MISI  /  HANYA BACA", color = Color(0xFF83C7D0),
                        fontSize = 9.sp, letterSpacing = 1.1.sp, fontWeight = FontWeight.Bold)
                    datasets.forEach { (name, values) ->
                        Text(name + " =", color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                        values.forEach { value ->
                            Text("  •  \"$value\"", color = Color(0xFFB7D9C2),
                                fontFamily = FontFamily.Monospace, fontSize = 10.sp)
                        }
                    }
                }
                if (mission != null) {
                    Text("RUANG KERJA MISI", color = CyberPalette.SteelGray, fontSize = 9.sp, letterSpacing = 1.2.sp)
                    mission.allowedFiles.sorted().forEach { fileName ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(fileName, color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                            Text(
                                if (fileName in mission.writableFiles) "BACA / TULIS" else "HANYA BACA",
                                color = if (fileName in mission.writableFiles) CyberPalette.Success else CyberPalette.SteelGray,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
                if (hintLevel > 0) {
                    val hint = mission?.hints?.firstOrNull { it.level == hintLevel }
                    if (hint != null) Text("PETUNJUK $hintLevel/3\n${hint.message}", color = Color(0xFF83C7D0), fontSize = 12.sp)
                }
                if (hintLevel < 3) OutlinedButton(onClick = onRequestHint) { Text("MINTA PETUNJUK ${hintLevel + 1}/3") }
                CodeEditor(code, onCodeChange, "EDITOR KODE PYTHON 3", 180)
                Text(saveStatus, color = CyberPalette.SteelGray, fontSize = 10.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onSaveCode, modifier = Modifier.weight(1f)) { Text("SIMPAN KODE") }
                    Button(onClick = onRun, modifier = Modifier.weight(1f)) { Text("JALANKAN") }
                }
                Button(
                    onClick = onSubmit,
                    enabled = canSubmit,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF28634D),
                        disabledContainerColor = Color(0xFF26343A)
                    )
                ) {
                    Text(if (canSubmit) "KIRIM / SUBMIT MISI" else "SUBMIT MISI — JALANKAN KODE DULU")
                }
                Surface(color = Color(0xFF080D12), shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, CyberPalette.Divider)) {
                    Column(Modifier.fillMaxWidth().padding(12.dp)) {
                        Text("KELUARAN TERMINAL", color = CyberPalette.SteelGray, fontSize = 9.sp)
                        Text(output, color = Color(0xFFB7E4C7), fontFamily = FontFamily.Monospace)
                    }
                }
                if (feedback.isNotBlank()) Text(feedback, color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                Text(
                    "SIMPAN KODE menyimpan draf. JALANKAN menguji kode di terminal. SUBMIT mengirim kode yang sama dengan eksekusi terakhir yang berhasil ke evaluator misi.",
                    color = CyberPalette.SteelGray,
                    fontSize = 10.sp
                )
            }
        } else {
            when (Campaign01StoryNavigationPolicy.action(stage, casePhase)) {
                com.zeroday.cyberprotocol.data.StoryCaseAction.OPEN_OPERATIONS ->
                    CPPrimaryButton(
                        if (stage in 12..13) "BUKA PUSAT OPERASI" else beat.button,
                        onOpenOperations
                    )
                com.zeroday.cyberprotocol.data.StoryCaseAction.OPEN_ANALYSIS ->
                    CPPrimaryButton(beat.button, onOpenAnalysis)
                com.zeroday.cyberprotocol.data.StoryCaseAction.CONTINUE -> {
                    val nextLabel = when {
                        stage == 11 && casePhase == CasePhase.ANALYSIS -> "LANJUTKAN KE PERBANDINGAN"
                        stage == 11 && casePhase != CasePhase.BRIEFING && casePhase != CasePhase.INVESTIGATION ->
                            "LANJUTKAN CERITA"
                        stage in 12..13 && casePhase == CasePhase.OPERATIONAL_DECISION ->
                            "LANJUTKAN KE KEPUTUSAN OPERASIONAL"
                        stage in 12..13 && (casePhase == CasePhase.DEBRIEFING || casePhase == CasePhase.COMPLETE) ->
                            "LANJUTKAN CERITA"
                        stage == 14 && caseComplete -> beat.completedButton ?: beat.button
                        else -> beat.button
                    }
                    CPPrimaryButton(nextLabel, onContinue)
                }
            }
        }
        Text("TITIK SIMPAN " + (stage - 1) + " / 12", color = CyberPalette.SteelGray, fontSize = 10.sp, letterSpacing = 1.2.sp)
    }
}

@Composable
private fun MissionDebriefScreen(
    title: String,
    feedback: String,
    xpAwarded: Int,
    rank: String,
    totalExperience: Int,
    nextClue: String?,
    onContinue: () -> Unit
) {
    GamePage("DEBRIEF MISI", "A.E.G.I.S. / HASIL VERIFIKASI", onContinue, null) {
        GamePanel {
            Text("MISI TERVERIFIKASI", color = CyberPalette.Success, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(title, color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Black)
            Text(feedback, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 22.sp)
            HorizontalDivider(color = CyberPalette.Divider)
            Text("REWARD  /  +$xpAwarded XP", color = Color(0xFF83C7D0), fontWeight = FontWeight.Bold)
            Text("TOTAL PENGALAMAN  /  $totalExperience XP", color = Color.White, fontSize = 12.sp)
            Text("RANK SAAT INI  /  $rank", color = Color.White, fontSize = 12.sp)
            if (!nextClue.isNullOrBlank()) {
                HorizontalDivider(color = CyberPalette.Divider)
                Text("PETUNJUK BERIKUTNYA", color = Color(0xFF83C7D0),
                    fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(nextClue, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp)
            }
        }
        CPPrimaryButton("LANJUTKAN OPERASI", onContinue)
    }
}

@Composable
private fun AppHubScreen(gameState: GameState, onClose: () -> Unit, onOpen: (GameScreen) -> Unit) {
    GamePage("PUSAT APLIKASI", "PERANGKAT AMAN A.E.G.I.S.", onClose, null) {
        GamePanel {
            Text("APLIKASI YANG DIIZINKAN", color = CyberPalette.SteelGray, fontSize = 10.sp, letterSpacing = 1.2.sp)
            Text("Pilih aplikasi. Menutupnya akan mengembalikanmu ke operasi sebelumnya.",
                color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
        data class HubApp(val id: String, val title: String, val icon: Int, val target: GameScreen)
        val apps = listOf(
            HubApp("MESSENGER", "PESAN AMAN", CyberAssets.connection, GameScreen.MESSENGER),
            HubApp("BROWSER", "A.E.G.I.S. BROWSER", CyberAssets.network, GameScreen.BROWSER),
            HubApp("TERMINAL", "TERMINAL", CyberAssets.terminal, GameScreen.TERMINAL),
            HubApp("DATABASE", "DATABASE INTELIJEN", CyberAssets.database, GameScreen.DATABASE),
            HubApp("DOSSIER", "BERKAS PERSONEL", CyberAssets.document, GameScreen.DOSSIER),
            HubApp("ANALYSIS", "ANALISIS", CyberAssets.dataAnalysis, GameScreen.ANALYSIS),
            HubApp("OPERATIONS", "OPERASI", CyberAssets.investigation, GameScreen.OPERATIONS)
        )
        apps.chunked(2).forEach { rowApps ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowApps.forEach { app ->
                    val unlocked = app.id in gameState.unlockedApps
                    Surface(onClick = { if (unlocked) onOpen(app.target) }, modifier = Modifier.weight(1f),
                        color = if (unlocked) MaterialTheme.colorScheme.surface else Color(0xFF101216),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (unlocked) CyberPalette.Divider else Color(0xFF22252B))) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            androidx.compose.foundation.Image(painterResource(app.icon), null,
                                Modifier.size(42.dp), alpha = if (unlocked) 1f else 0.3f)
                            Text(app.title, color = if (unlocked) Color.White else CyberPalette.SteelGray,
                                fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text(if (unlocked) "BUKA  ›" else "TERKUNCI  ·  IZIN DIPERLUKAN",
                                color = CyberPalette.SteelGray, fontSize = 9.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MessengerScreen(
    selectedContact: String,
    onSelectContact: (String) -> Unit,
    onClose: () -> Unit,
    recruitmentInvitation: Boolean,
    freyaContactUnlocked: Boolean,
    nexusUnlocked: Boolean,
    activeMissionTitle: String?,
    completedMissionCount: Int,
    evidenceAssetsAvailable: Boolean,
    onReadEvidenceAsset: (String) -> String?,
    canRecordEvidence: (String) -> Boolean,
    onRecordEvidence: (String) -> Unit,
    onOpenMissionLink: () -> Unit,
    onOpenNexusLink: () -> Unit,
    onOpenIntranetLink: () -> Unit,
    onDownloadMissionFile: (String) -> Boolean,
    replies: List<String>,
    onReply: (String) -> Unit
) {
    var search by rememberSaveable { mutableStateOf("") }
    var attachmentFeedback by rememberSaveable { mutableStateOf("") }
    var evidenceAssetPreview by remember { mutableStateOf<Triple<String, String, String>?>(null) }
    val chatScrollState = rememberScrollState()

    LaunchedEffect(selectedContact, replies.size) {
        withFrameNanos { }
        chatScrollState.animateScrollTo(chatScrollState.maxValue)
    }
    val contactName = when (selectedContact) {
        "AEGIS" -> "A.E.G.I.S. / Recruitment Office"
        "FREYA" -> "Freya Knox"
        "NEXUS" -> "NEXUS"
        else -> "Pesan Aman"
    }
    val quickReplies = when (selectedContact) {
        "FREYA" -> listOf(
            "Apa fungsi Terminal?",
            "Bagaimana membaca berkas misi?",
            "Apa itu variabel Python?",
            "Bagaimana menjalankan dan mengirim kode?",
            "Masih banyak yang harus kupelajari.",
            "Mengapa kamu memilihku?"
        )
        "NEXUS" -> listOf("Tampilkan status personel.", "Di mana catatan operasiku?", "Apakah sistem ini berjalan offline?")
        else -> emptyList()
    }

    Column(Modifier.fillMaxSize().background(Color(0xFF0B141A))) {
        Row(
            Modifier.fillMaxWidth().background(Color(0xFF202C33)).padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextButton(onClick = {
                if (selectedContact.isBlank()) onClose() else onSelectContact("")
            }) { Text("‹", color = Color.White, fontSize = 30.sp) }
            if (selectedContact.isNotBlank()) {
                Surface(color = if (selectedContact == "FREYA") Color(0xFF315448) else Color(0xFF35434B),
                    shape = RoundedCornerShape(50)) {
                    Text(
                        when (selectedContact) { "AEGIS" -> "A"; "FREYA" -> "FK"; else -> "NX" },
                        Modifier.padding(10.dp), color = Color.White, fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(contactName, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    when (selectedContact) {
                        "AEGIS" -> "kanal rekrutmen resmi"
                        "FREYA" -> "mentor  ·  tersedia melalui kanal aman"
                        "NEXUS" -> "sistem komunikasi aktif"
                        else -> "A.E.G.I.S.  ·  komunikasi aman"
                    },
                    color = Color(0xFFB4C2C8), fontSize = 10.sp
                )
            }
            Text("⋮", color = Color(0xFFB4C2C8), fontSize = 22.sp)
        }

        if (selectedContact.isBlank()) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it.take(60) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                singleLine = true,
                placeholder = { Text("Cari percakapan") },
                leadingIcon = { Text("⌕", color = Color(0xFF9BAEB4), fontSize = 22.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF202C33),
                    unfocusedContainerColor = Color(0xFF202C33),
                    focusedBorderColor = Color(0xFF315448),
                    unfocusedBorderColor = Color(0xFF34434A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Column(
                Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                if (recruitmentInvitation &&
                    ("A.E.G.I.S.".contains(search, true) || "recruitment".contains(search, true) ||
                        "perintah untuk merekrutmu".contains(search, true))
                ) {
                    MessengerContactRow(
                        initials = "A",
                        name = "A.E.G.I.S. / Recruitment Office",
                        preview = "Seseorang telah memberikan perintah kepada A.E.G.I.S. untuk merekrutmu.",
                        status = "RESMI",
                        onClick = { onSelectContact("AEGIS") }
                    )
                }
                if (freyaContactUnlocked &&
                    ("FREYA KNOX".contains(search, true) || "FREYA".contains(search, true) ||
                        "mentor".contains(search, true))
                ) {
                    MessengerContactRow(
                        initials = "FK",
                        name = "Freya Knox",
                        preview = "Halo. Aku Freya. Aku akan mendampingimu selama orientasi.",
                        status = "MENTOR",
                        onClick = { onSelectContact("FREYA") }
                    )
                }
                if (nexusUnlocked &&
                    ("NEXUS".contains(search, true) || "sistem komunikasi".contains(search, true))
                ) {
                    MessengerContactRow(
                        initials = "NX",
                        name = "NEXUS",
                        preview = "Kanal sistem A.E.G.I.S. telah diaktifkan.",
                        status = "SISTEM",
                        onClick = { onSelectContact("NEXUS") }
                    )
                }
                val visibleTerms = listOf(
                    "A.E.G.I.S.", "recruitment", "Recruitment Office", "perintah untuk merekrutmu",
                    "FREYA KNOX", "FREYA", "mentor", "NEXUS", "sistem komunikasi"
                )
                val visible = (recruitmentInvitation && visibleTerms.take(4).any { it.contains(search, true) }) ||
                    (freyaContactUnlocked && visibleTerms.slice(4..6).any { it.contains(search, true) }) ||
                    (nexusUnlocked && visibleTerms.slice(7..8).any { it.contains(search, true) })
                if (search.isNotBlank() && !visible) {
                    Text("Tidak ada percakapan yang cocok.", color = Color(0xFF9BAEB4),
                        modifier = Modifier.fillMaxWidth().padding(18.dp), fontSize = 12.sp)
                }
            }
        } else {
            Column(
                Modifier.weight(1f).fillMaxWidth().background(Color(0xFF0B141A))
                    .verticalScroll(chatScrollState).padding(horizontal = 14.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("PESAN TERENKRIPSI  ·  A.E.G.I.S.",
                    Modifier.align(Alignment.CenterHorizontally),
                    color = Color(0xFF91A5AC), fontSize = 9.sp, letterSpacing = 1.sp)
                when (selectedContact) {
                    "AEGIS" -> {
                        ChatBubble(
                            "A.E.G.I.S. / Recruitment Office",
                            "PEMBERITAHUAN REKRUTMEN RESMI\n\nAtas nama A.E.G.I.S., kami menghubungimu untuk menyampaikan bahwa seseorang telah memberikan perintah kepada organisasi ini untuk merekrutmu. Identitas pemberi perintah tidak disertakan dalam undangan ini.\n\nA.E.G.I.S. adalah organisasi intelijen internasional independen yang berkedudukan di Manila. Rangkaian awal ini dirancang untuk memperkenalkan prosedur, menguji ketelitian, dan menilai kesiapanmu sebelum akses yang lebih luas diberikan.\n\nJika kamu bersedia melanjutkan, buka tautan resmi di bawah untuk melihat rangkaian misi orientasi.",
                            false
                        )
                        Surface(
                            onClick = onOpenMissionLink,
                            color = Color(0xFF174C40),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, Color(0xFF3A806B)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text("TAUTAN RESMI  ·  A.E.G.I.S.", color = Color(0xFF9ED9C1),
                                    fontSize = 9.sp, letterSpacing = 1.sp)
                                Text("BUKA RANGKAIAN MISI  ↗", color = Color.White,
                                    fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("Orientasi dan tugas awal", color = Color(0xFFC2D9D0), fontSize = 10.sp)
                            }
                        }
                    }
                    "FREYA" -> {
                        ChatBubble(
                            "Freya Knox",
                            "Halo. Aku Freya Knox. Selamat datang. Aku akan mendampingimu selama rangkaian misi awal A.E.G.I.S. dan membantu menjelaskan apa yang perlu kamu lakukan. Kita jalani satu per satu—tidak ada alasan untuk berpura-pura sudah memahami sesuatu yang baru kamu temui.",
                            false
                        )
                        ChatBubble(
                            "Freya Knox",
                            if (activeMissionTitle != null) {
                                "Aku melihat tugas aktifmu sekarang: $activeMissionTitle. Periksa briefing dan tujuan satu per satu. Kita akan mempelajari terminal dan Python dari dasar sebelum kamu mengerjakan tugas teknis secara mandiri."
                            } else {
                                "Kalau belum ada tugas aktif, buka kembali rangkaian misi dari Campaign. Aku akan membantumu memahami instruksi saat tugas berikutnya tersedia."
                            },
                            false
                        )
                        ChatBubble(
                            "Freya Knox",
                            "Progresmu tercatat pada $completedMissionCount misi yang sudah diverifikasi. Kita tidak perlu terburu-buru; yang penting kamu memahami alasan di balik setiap langkah.",
                            false
                        )
                        ChatBubble(
                            "Freya Knox",
                            "Untuk dukungan teknis tambahan, ada NEXUS. Sistem ini membantu komunikasi intelijen dan diagnostik, tetapi bukan pengganti mentor. Aku tetap akan membimbingmu memahami setiap langkah.",
                            false
                        )
                        ChatBubble(
                            "Freya Knox",
                            "Terminal adalah tempat memberi perintah teks kepada lingkungan kerja game. Python adalah bahasa yang akan kita gunakan untuk menulis instruksi. Kita mulai dari perintah sederhana, membaca berkas, menyimpan nilai ke variabel, lalu menampilkan hasilnya.",
                            false
                        )
                        ChatBubble(
                            "Freya Knox",
                            "Setelah pengenalan dasar, buka A.E.G.I.S. Intranet melalui Browser internal. Gunakan ID personel RECRUIT-014 dan kode akses FIRSTCONTACT. Portal ini berjalan lokal di perangkatmu.",
                            false
                        )
                        ChatBubble(
                            "Freya Knox",
                            "Untuk tugas Sinyal Pertama, aku juga melampirkan message_fragment.txt. Simpan berkas itu ke ruang kerja misi, lalu baca melalui Terminal. Browser Repository dan lampiran ini menunjuk berkas yang sama.",
                            false
                        )
                        replies.forEach { message ->
                            ChatBubble("Kamu", message, true)
                            ChatBubble(
                                "Freya Knox",
                                MessengerDialogue.freyaResponse(message),
                                false
                            )
                        }
                    }
                    else -> {
                        ChatBubble("NEXUS", "Kanal komunikasi A.E.G.I.S. aktif. Status personel: Recruit. Aku adalah sistem pendukung analisis dan komunikasi, bukan pengambil keputusan operasional.", false)
                        ChatBubble("NEXUS", "Aku dapat menjelaskan data yang diizinkan, membantu memeriksa hasil teknis, dan meneruskan instruksi yang telah disahkan mentor.", false)
                        replies.forEach { message ->
                            ChatBubble("Kamu", message, true)
                            ChatBubble(
                                "NEXUS",
                                MessengerDialogue.nexusResponse(message, activeMissionTitle, completedMissionCount),
                                false
                            )
                        }
                    }
                }
            }
            if (selectedContact == "FREYA") {
                Surface(color = Color(0xFF202C33), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Text("TAUTAN DAN LAMPIRAN DARI FREYA", color = Color(0xFF9BAEB4), fontSize = 9.sp, letterSpacing = 1.sp)
                        Surface(
                            onClick = onOpenNexusLink,
                            color = Color(0xFF263A34),
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.dp, Color(0xFF36564A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("NEXUS  /  KANAL SISTEM", color = Color.White,
                                    fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Dukungan analisis dan komunikasi", color = Color(0xFFB9D1C6), fontSize = 10.sp)
                                Text("Ketuk untuk membuka kontak NEXUS", color = Color(0xFFB9D1C6), fontSize = 10.sp)
                            }
                        }
                        Surface(
                            onClick = onOpenIntranetLink,
                            color = Color(0xFF263A34),
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.dp, Color(0xFF36564A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("A.E.G.I.S. INTRANET  /  BROWSER", color = Color.White,
                                    fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("ID: RECRUIT-014  ·  KODE: FIRSTCONTACT", color = Color(0xFFB9D1C6), fontSize = 10.sp)
                                Text("Ketuk untuk membuka portal internal offline", color = Color(0xFFB9D1C6), fontSize = 10.sp)
                            }
                        }
                        if (evidenceAssetsAvailable) {
                            com.zeroday.cyberprotocol.data.EvidenceAssetCatalog.assets.forEach { asset ->
                                Surface(
                                    onClick = {
                                        evidenceAssetPreview = onReadEvidenceAsset(asset.id)?.let { Triple(asset.evidenceId, asset.title, it) }
                                    },
                                    color = Color(0xFF202E3B),
                                    shape = RoundedCornerShape(14.dp),
                                    border = BorderStroke(1.dp, Color(0xFF3B5265)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text("LAMPIRAN INVESTIGASI  /  ${asset.title}", color = Color.White,
                                            fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(asset.path, color = Color(0xFFB9D1C6), fontSize = 9.sp)
                                        Text("Sumber asli tersimpan di arsip virtual A.E.G.I.S.", color = Color(0xFF83C7D0), fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                        if (activeMissionTitle == "Sinyal Pertama") {
                            Surface(
                                onClick = {
                                    attachmentFeedback = if (onDownloadMissionFile("message_fragment.txt")) {
                                        "message_fragment.txt tersimpan di ruang kerja misi."
                                    } else {
                                        "Lampiran belum dapat diunduh. Pastikan Sinyal Pertama masih aktif."
                                    }
                                },
                                color = Color(0xFF202E3B),
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, Color(0xFF3B5265)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("LAMPIRAN  /  message_fragment.txt", color = Color.White,
                                        fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("Fragmen pesan A.E.G.I.S. · hanya baca", color = Color(0xFFB9D1C6), fontSize = 10.sp)
                                    Text("Ketuk untuk menyimpan ke ruang kerja misi", color = Color(0xFF83C7D0), fontSize = 10.sp)
                                }
                            }
                            if (attachmentFeedback.isNotBlank()) {
                                Text(attachmentFeedback, color = Color(0xFF83C7D0), fontSize = 10.sp)
                            }
                        }
                        Text("BALASAN CEPAT", color = Color(0xFF9BAEB4), fontSize = 9.sp)
                        quickReplies.forEach { choice ->
                            Surface(onClick = { onReply(choice) }, color = Color(0xFF263A34),
                                shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                                Text(choice, Modifier.padding(horizontal = 13.dp, vertical = 10.dp),
                                    color = Color(0xFFE2ECE7), fontSize = 12.sp)
                            }
                        }
                    }
                }
            } else if (selectedContact == "NEXUS") {
                Surface(color = Color(0xFF202C33), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Text("BALASAN CEPAT", color = Color(0xFF9BAEB4), fontSize = 9.sp)
                        quickReplies.forEach { choice ->
                            Surface(onClick = { onReply(choice) }, color = Color(0xFF263A34),
                                shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                                Text(choice, Modifier.padding(horizontal = 13.dp, vertical = 10.dp),
                                    color = Color(0xFFE2ECE7), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
        evidenceAssetPreview?.let { (evidenceId, title, content) ->
            AlertDialog(
                onDismissRequest = { evidenceAssetPreview = null },
                title = { Text(title) },
                text = { Text(content, fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                confirmButton = {
                    TextButton(onClick = {
                        onRecordEvidence(evidenceId)
                        evidenceAssetPreview = null
                    }) {
                        Text(if (canRecordEvidence(evidenceId)) "CATAT SEBAGAI DIPERIKSA" else "TUTUP")
                    }
                }
            )
        }
    }
}

@Composable
private fun MessengerContactRow(
    initials: String,
    name: String,
    preview: String,
    status: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color(0xFF182229),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF2A3940)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(horizontal = 13.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(color = Color(0xFF315448), shape = RoundedCornerShape(50)) {
                Text(initials, Modifier.padding(13.dp), color = Color.White, fontWeight = FontWeight.Bold)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(name, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Spacer(Modifier.weight(1f))
                    Text(status, color = Color(0xFF72C99B), fontSize = 8.sp, letterSpacing = 0.6.sp)
                }
                Text(preview, color = Color(0xFF9BAEB4), fontSize = 11.sp, maxLines = 1)
            }
        }
    }
}

@Composable
private fun ChatBubble(sender: String, message: String, outgoing: Boolean) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = if (outgoing) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        if (!outgoing) {
            Text(sender, color = Color(0xFF72C99B), fontSize = 9.sp)
        }
        Surface(
            color = if (outgoing) Color(0xFF005C4B) else Color(0xFF202C33),
            shape = RoundedCornerShape(
                topStart = 13.dp, topEnd = 13.dp,
                bottomEnd = if (outgoing) 4.dp else 13.dp,
                bottomStart = if (outgoing) 13.dp else 4.dp
            ),
            border = BorderStroke(1.dp, Color(0xFF34434A)),
            modifier = Modifier.fillMaxWidth(0.88f)
        ) {
            Text(message, Modifier.padding(horizontal = 13.dp, vertical = 10.dp),
                color = Color(0xFFE4E9EC), lineHeight = 19.sp, fontSize = 12.sp)
        }
    }
}

@Composable
private fun TerminalScreen(
    currentDirectory: String, workspaceLabel: String, command: String, onCommandChange: (String) -> Unit,
    output: String, busy: Boolean, onExecute: () -> Unit, onBack: () -> Unit
) {
    val outputScrollState = rememberScrollState()

    LaunchedEffect(output) {
        withFrameNanos { }
        outputScrollState.animateScrollTo(outputScrollState.maxValue)
    }

    GamePage("TERMINAL", workspaceLabel, onBack, null) {
        GamePanel {
            Text(currentDirectory, color = Color(0xFF8CC8A2), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            Surface(color = Color(0xFF070B0E), shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFF263B35))) {
                Text(output, Modifier.fillMaxWidth().heightIn(min = 240.dp, max = 430.dp)
                    .verticalScroll(outputScrollState).padding(12.dp),
                    color = Color(0xFFB7D9C2), fontFamily = FontFamily.Monospace, fontSize = 11.sp, lineHeight = 17.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = command, onValueChange = onCommandChange,
                    modifier = Modifier.weight(1f), singleLine = true, placeholder = { Text("Masukkan perintah…") },
                    textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { onExecute() }))
                Button(onClick = onExecute, enabled = !busy) { Text(if (busy) "…" else "JALANKAN") }
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
            Text("Perintah bergaya Linux • sistem file virtual terisolasi • tanpa akses ke shell Android",
                color = CyberPalette.SteelGray, fontSize = 10.sp)
            TextButton(onClick = { onCommandChange("help") }) { Text("TAMPILKAN PANDUAN PERINTAH") }
        }
    }
}

@Composable
private fun SettingsScreen(
    reducedMotion: Boolean,
    onReducedMotionChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    GamePage("PENGATURAN", "PREFERENSI PERANGKAT A.E.G.I.S.", onBack, null) {
        GamePanel {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("KURANGI GERAKAN", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(
                        "Mengganti animasi pembukaan dengan satu frame statis untuk mengurangi gerakan visual.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
                Switch(checked = reducedMotion, onCheckedChange = onReducedMotionChange)
            }
            HorizontalDivider(color = CyberPalette.Divider)
            Text("Preferensi disimpan secara lokal pada perangkat ini.", color = CyberPalette.SteelGray, fontSize = 11.sp)
            Text("Pengaturan audio dan kontrol akan tersedia setelah sistem audio dan pemetaan kontrol diimplementasikan.", color = CyberPalette.SteelGray, fontSize = 11.sp)
        }
    }
}

@Composable
private fun DatabaseScreen(gameState: GameState, onBack: () -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var selectedMissionId by rememberSaveable { mutableStateOf<String?>(null) }
    val matchingMissions = Campaign01Catalog.missions.filter { mission ->
        query.isBlank() || mission.title.contains(query, ignoreCase = true) ||
            mission.id.contains(query, ignoreCase = true) ||
            mission.briefing.contains(query, ignoreCase = true)
    }
    val selectedMission = selectedMissionId?.let(Campaign01Catalog::find)

    GamePage("DATABASE INTELIJEN", "A.E.G.I.S. / INDEKS MISI TERBATAS", onBack, null) {
        GamePanel {
            Text("INDEKS OPERASI", color = CyberPalette.SteelGray, fontSize = 10.sp, letterSpacing = 1.2.sp)
            OutlinedTextField(
                value = query,
                onValueChange = { query = it.take(100) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Cari nama, ID, atau briefing") },
                placeholder = { Text("Contoh: sinyal") }
            )
            Text(
                "Ditemukan ${matchingMissions.size} dari ${Campaign01Catalog.missions.size} berkas",
                color = CyberPalette.SteelGray,
                fontSize = 10.sp
            )
            if (matchingMissions.isEmpty()) {
                Text("Tidak ada berkas yang cocok dengan pencarian.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            matchingMissions.forEach { mission ->
                val completed = mission.id in gameState.completedMissionIds
                Surface(
                    onClick = { selectedMissionId = mission.id },
                    color = if (selectedMissionId == mission.id) Color(0xFF26313A) else Color(0xFF1A1D24),
                    shape = RoundedCornerShape(9.dp),
                    border = BorderStroke(1.dp, if (selectedMissionId == mission.id) CyberPalette.Crimson else CyberPalette.Divider),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(mission.title, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(mission.id, color = CyberPalette.SteelGray, fontSize = 9.sp)
                        Text(
                            if (completed) "STATUS: TERVERIFIKASI" else "STATUS: BELUM SELESAI",
                            color = if (completed) CyberPalette.Success else CyberPalette.SteelGray,
                            fontSize = 9.sp
                        )
                    }
                }
            }
            selectedMission?.let { mission ->
                HorizontalDivider(color = CyberPalette.Divider)
                Text("RINCIAN BERKAS", color = CyberPalette.Crimson, fontSize = 10.sp, letterSpacing = 1.2.sp)
                Text(mission.title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(mission.briefing, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp)
                Text("TUJUAN OPERASI", color = CyberPalette.SteelGray, fontSize = 10.sp)
                mission.objectives.forEachIndexed { index, objective ->
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("${index + 1}. ${objective.title}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text(objective.description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    }
                }
                Text(
                    "REWARD: ${mission.rewardXp} XP  ·  CLEARANCE: ${mission.requiredClearance}",
                    color = CyberPalette.SteelGray,
                    fontSize = 10.sp
                )
                OutlinedButton(onClick = { selectedMissionId = null }, modifier = Modifier.fillMaxWidth()) {
                    Text("TUTUP RINCIAN")
                }
            }
            Text(
                "Seluruh data pada indeks ini merupakan bagian dari simulasi fiktif A.E.G.I.S.",
                color = CyberPalette.SteelGray,
                fontSize = 10.sp
            )
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
            if (onHub != null) TextButton(onClick = onHub) { Text("APLIKASI") }
            TextButton(onClick = onBack) { Text("KEMBALI") }
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
