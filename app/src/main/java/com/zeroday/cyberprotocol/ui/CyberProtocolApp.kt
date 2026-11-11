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
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.data.Campaign01Catalog
import com.zeroday.cyberprotocol.data.Campaign01Story
import com.zeroday.cyberprotocol.data.StoryBeat
import com.zeroday.cyberprotocol.data.LocalGameStateRepository
import com.zeroday.cyberprotocol.data.VirtualFileSystem
import com.zeroday.cyberprotocol.execution.PythonExecutionService
import com.zeroday.cyberprotocol.domain.usecase.CompleteMissionUseCase
import com.zeroday.cyberprotocol.domain.usecase.RecordEvidenceUseCase
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
    val context = LocalContext.current
    val preferences = remember { context.getSharedPreferences("cyber_protocol_campaign_v2", 0) }
    val gameStateRepository = remember { LocalGameStateRepository(context) }
    val initialGameState = remember { gameStateRepository.load() }
    var currentGameState by remember { mutableStateOf(initialGameState) }
    val fileSystem = remember { VirtualFileSystem(context) }
    val python = remember { PythonExecutionService() }
    val scope = rememberCoroutineScope()

    var screenName by rememberSaveable { mutableStateOf(GameScreen.MAIN_MENU.name) }
    val screen = runCatching { GameScreen.valueOf(screenName) }.getOrDefault(GameScreen.MAIN_MENU)
    var returnScreen by rememberSaveable { mutableStateOf(GameScreen.CAMPAIGN.name) }
    var storyStage by rememberSaveable {
        mutableIntStateOf(initialGameState.storyBeat.coerceIn(0, 7))
    }
    var code by remember {
        mutableStateOf(TextFieldValue(
            initialGameState.savedCode["story_$storyStage"]
                ?: preferences.getString("saved_code_$storyStage",
                    Campaign01Story.codeTemplates.getOrElse((storyStage - 4).coerceIn(0, 1)) { "" }).orEmpty()
        ))
    }
    var codeOutput by rememberSaveable { mutableStateOf("KELUARAN AKAN MUNCUL DI SINI") }
    var codeFeedback by rememberSaveable { mutableStateOf("") }
    var command by rememberSaveable { mutableStateOf("") }
    var currentDirectory by rememberSaveable { mutableStateOf(fileSystem.initialDirectory()) }
    var terminalOutput by rememberSaveable {
        mutableStateOf("TERMINAL VIRTUAL A.E.G.I.S.  ·  BUILD 0.1\nLingkungan game siap digunakan.\nKetik 'help' untuk melihat daftar perintah.\n")
    }
    var terminalBusy by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    val messengerPreferences = remember { context.getSharedPreferences("cp_messenger_v1", 0) }
    var freyaReplies by remember {
        mutableStateOf(messengerPreferences.getString("freya_replies", "").orEmpty()
            .split("\u001F").filter { it.isNotBlank() })
    }
    var nexusReplies by remember {
        mutableStateOf(messengerPreferences.getString("nexus_replies", "").orEmpty()
            .split("\u001F").filter { it.isNotBlank() })
    }
    var messengerContact by rememberSaveable { mutableStateOf("") }
    var savedCodeStatus by rememberSaveable { mutableStateOf("PERUBAHAN BELUM DISIMPAN") }
    var debriefTitle by rememberSaveable { mutableStateOf("") }
    var debriefFeedback by rememberSaveable { mutableStateOf("") }
    var debriefXp by rememberSaveable { mutableIntStateOf(0) }
    var hintLevel by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(screenName, storyStage) {
        currentGameState = gameStateRepository.load()
    }

    fun saveStage(value: Int) {
        storyStage = value.coerceIn(0, 7)
        preferences.edit().putInt("story_stage", storyStage).apply()
        val previousState = gameStateRepository.load()
        gameStateRepository.save(previousState.copy(
            storyBeat = storyStage,
            clearance = if (storyStage >= 7) maxOf(previousState.clearance, 1) else previousState.clearance,
            unlockedApps = com.zeroday.cyberprotocol.domain.usecase.ProgressionRules.unlockedAppsForStoryBeat(storyStage),
            lastCheckpoint = "story_beat_$storyStage"
        ))
        if (storyStage >= 7 && gameStateRepository.load().evidence.none { it.id == "transmission_timestamp_discrepancy" }) {
            RecordEvidenceUseCase(gameStateRepository).record(
                com.zeroday.cyberprotocol.core.model.EvidenceRecord(
                    id = "transmission_timestamp_discrepancy",
                    title = "Perbedaan Waktu Penerimaan Transmisi",
                    summary = "Dua sistem internal mencatat waktu penerimaan pesan yang berbeda. Perbedaan teramati, tetapi penyebabnya belum diketahui.",
                    source = "NEXUS / perbandingan metadata tanda terima",
                    status = com.zeroday.cyberprotocol.core.model.EvidenceStatus.UNVERIFIED,
                    missionId = "first_contact.recruitment"
                )
            )
            val withLead = gameStateRepository.load()
            gameStateRepository.save(withLead.copy(lastCheckpoint = "story_beat_7"))
        }
        currentGameState = gameStateRepository.load()
        val savedDraft = gameStateRepository.load().savedCode["story_$storyStage"]
            ?: preferences.getString("saved_code_$storyStage",
                Campaign01Story.codeTemplates.getOrElse((storyStage - 4).coerceIn(0, 1)) { "" }).orEmpty()
        code = TextFieldValue(savedDraft)
        codeOutput = "KELUARAN AKAN MUNCUL DI SINI"
        codeFeedback = ""
        hintLevel = 0
        savedCodeStatus = "DRAF TERSIMPAN"
    }

    fun openHub(from: GameScreen) {
        returnScreen = from.name
        screenName = GameScreen.APP_HUB.name
    }

    fun appendTerminalOutput(value: String) {
        terminalOutput = (terminalOutput + value).takeLast(24_000)
    }

    fun executeTerminalCommand() {
        val input = command.trim()
        if (input.isEmpty() || terminalBusy) return
        command = ""
        val response = fileSystem.execute(input, currentDirectory)
        currentDirectory = response.currentDirectory
        if (response.clearScreen) {
            terminalOutput = "A.E.G.I.S. VIRTUAL TERMINAL  ·  KELUARAN DIBERSIHKAN\n"
            return
        }
        appendTerminalOutput("\n$ $input\n")
        val output = response.output
        if (output.startsWith("PYTHON_LAUNCH:")) {
            val fileName = output.removePrefix("PYTHON_LAUNCH:")
            val source = fileSystem.readFile(fileName, currentDirectory)
            if (source == null) {
                appendTerminalOutput("python: tidak dapat membuka '$fileName': file tidak ditemukan\n")
            } else {
                terminalBusy = true
                scope.launch {
                    val result = withContext(Dispatchers.IO) { python.run(source) }
                    appendTerminalOutput((result.output.ifBlank { "(tidak ada keluaran)" }) + "\n")
                    if (result.error.isNotBlank()) appendTerminalOutput("KESALAHAN: " + result.error + "\n")
                    terminalBusy = false
                }
            }
        } else if (output.isNotBlank()) {
            appendTerminalOutput(output + "\n")
        }
    }

    BackHandler(enabled = screen != GameScreen.MAIN_MENU) {
        screenName = when (screen) {
            GameScreen.CAMPAIGN -> GameScreen.MAIN_MENU.name
            GameScreen.STORY -> GameScreen.CAMPAIGN.name
            GameScreen.DEBRIEF -> if (storyStage >= 7) GameScreen.CAMPAIGN.name else GameScreen.STORY.name
            GameScreen.APP_HUB -> returnScreen
            GameScreen.MESSENGER -> if (messengerContact.isNotBlank()) {
                messengerContact = ""
                GameScreen.MESSENGER.name
            } else GameScreen.APP_HUB.name
            GameScreen.TERMINAL, GameScreen.DATABASE, GameScreen.DOSSIER,
            GameScreen.ANALYSIS, GameScreen.OPERATIONS -> GameScreen.APP_HUB.name
            GameScreen.PROFILE, GameScreen.ARCHIVES, GameScreen.ACHIEVEMENTS,
            GameScreen.SETTINGS, GameScreen.LANGUAGE -> GameScreen.MAIN_MENU.name
            GameScreen.MAIN_MENU -> GameScreen.MAIN_MENU.name
        }
    }

    when (screen) {
        GameScreen.MAIN_MENU -> MainMenuScreen(
            onContinue = { screenName = GameScreen.CAMPAIGN.name },
            onNewCampaign = { showResetDialog = true },
            onHub = { openHub(GameScreen.MAIN_MENU) },
            onProfile = { screenName = GameScreen.PROFILE.name },
            onArchives = { screenName = GameScreen.ARCHIVES.name },
            onAchievements = { screenName = GameScreen.ACHIEVEMENTS.name },
            onSettings = { screenName = GameScreen.SETTINGS.name },
            onLanguage = { screenName = GameScreen.LANGUAGE.name }
        )
        GameScreen.CAMPAIGN -> CampaignScreen(
            storyStage = storyStage,
            onBack = { screenName = GameScreen.MAIN_MENU.name },
            onContinue = { screenName = if (storyStage < 7) GameScreen.STORY.name else GameScreen.APP_HUB.name },
            onHub = { openHub(GameScreen.CAMPAIGN) }
        )
        GameScreen.STORY -> {
            val beatIndex = storyStage.coerceIn(0, 6)
            val authoredMission = when (beatIndex) {
                4 -> Campaign01Catalog.find("first_contact.signal")
                5 -> Campaign01Catalog.find("first_contact.identity")
                else -> null
            }
            LaunchedEffect(authoredMission?.id) {
                authoredMission?.let { mission ->
                    val latest = gameStateRepository.load()
                    if (mission.id !in latest.completedMissionIds && latest.activeMissionId != mission.id) {
                        gameStateRepository.save(latest.copy(
                            activeMissionId = mission.id,
                            lastCheckpoint = "mission_started:" + mission.id
                        ))
                        currentGameState = gameStateRepository.load()
                    }
                }
            }
            StoryScreen(
                beat = Campaign01Story.beats[beatIndex],
                stage = beatIndex,
                mission = authoredMission,
                hintLevel = hintLevel,
                onRequestHint = { hintLevel = (hintLevel + 1).coerceAtMost(3) },
                code = code,
                onCodeChange = { code = it; savedCodeStatus = "PERUBAHAN BELUM DISIMPAN" },
                output = codeOutput,
                feedback = codeFeedback,
                saveStatus = savedCodeStatus,
                onSaveCode = {
                    authoredMission?.let { mission ->
                        fileSystem.openMissionWorkspace(mission).writeFile(mission.writableFiles.first(), code.text)
                    }
                    preferences.edit().putString("saved_code_$storyStage", code.text).apply()
                    val previousState = gameStateRepository.load()
                    gameStateRepository.save(previousState.copy(
                        savedCode = previousState.savedCode + ("story_$storyStage" to code.text),
                        lastCheckpoint = "code_saved_story_$storyStage"
                    ))
                    currentGameState = gameStateRepository.load()
                    savedCodeStatus = "TERSIMPAN"
                },
                onRun = {
                    scope.launch {
                        codeFeedback = "MENJALANKAN — hanya eksekusi; belum ada evaluasi misi."
                        val workspace = authoredMission?.let { fileSystem.openMissionWorkspace(it) }
                        val result = withContext(Dispatchers.IO) { python.run(code.text, workspace) }
                        codeOutput = result.output.ifBlank { "(tidak ada keluaran)" }
                        if (result.error.isNotBlank()) codeOutput += "\n" + result.error
                        codeFeedback = "JALANKAN SELESAI — progres kampanye belum berubah."
                    }
                },
                onSubmit = {
                    val missionId = authoredMission?.evaluatorId ?: -1
                    scope.launch {
                        codeFeedback = "MEMVERIFIKASI..."
                        val result = withContext(Dispatchers.IO) {
                            python.submit(missionId, code.text)
                        }
                        codeOutput = result.output.ifBlank { "(tidak ada keluaran)" }
                        if (result.error.isNotBlank()) codeOutput += "\n" + result.error
                        if (result.passed) {
                            var rewardGranted = 0
                            authoredMission?.let { mission ->
                                val alreadyCompleted = mission.id in gameStateRepository.load().completedMissionIds
                                CompleteMissionUseCase(gameStateRepository).complete(
                                    mission.id, mission.rewardXp, mission.requiredClearance
                                )
                                if (!alreadyCompleted) rewardGranted = mission.rewardXp
                                RecordEvidenceUseCase(gameStateRepository).record(
                                    com.zeroday.cyberprotocol.core.model.EvidenceRecord(
                                        id = "evidence_" + mission.id,
                                        title = mission.title,
                                        summary = "Hasil tugas latihan berhasil diverifikasi oleh evaluator A.E.G.I.S.",
                                        source = "NEXUS / Python evaluator " + mission.evaluatorId,
                                        status = com.zeroday.cyberprotocol.core.model.EvidenceStatus.CONFIRMED,
                                        missionId = mission.id
                                    )
                                )
                            }
                            authoredMission?.let { mission ->
                                fileSystem.openMissionWorkspace(mission).writeFile(mission.writableFiles.first(), code.text)
                            }
                            preferences.edit().putString("saved_code_$storyStage", code.text).apply()
                            val previousState = gameStateRepository.load()
                            gameStateRepository.save(previousState.copy(
                                savedCode = previousState.savedCode + ("story_$storyStage" to code.text),
                                lastCheckpoint = "code_checkpoint_story_$storyStage"
                            ))
                            currentGameState = gameStateRepository.load()
                            savedCodeStatus = "TERSIMPAN PADA CHECKPOINT"
                            saveStage(storyStage + 1)
                            debriefTitle = authoredMission?.title ?: "MISI SELESAI"
                            debriefFeedback = result.feedback
                            debriefXp = rewardGranted
                            screenName = GameScreen.DEBRIEF.name
                        } else {
                            codeFeedback = result.feedback.ifBlank {
                                "BELUM TERVERIFIKASI — periksa kembali kode dan tujuan misi."
                            }
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
        GameScreen.DEBRIEF -> MissionDebriefScreen(
            title = debriefTitle,
            feedback = debriefFeedback,
            xpAwarded = debriefXp,
            rank = currentGameState.rank.replace('_', ' '),
            totalExperience = currentGameState.experience,
            onContinue = {
                screenName = if (storyStage >= 7) GameScreen.CAMPAIGN.name else GameScreen.STORY.name
            }
        )
        GameScreen.APP_HUB -> AppHubScreen(
            gameState = currentGameState,
            onClose = { screenName = returnScreen },
            onOpen = { target -> screenName = target.name }
        )
        GameScreen.MESSENGER -> MessengerScreen(
            selectedContact = messengerContact,
            onSelectContact = { messengerContact = it },
            onClose = { screenName = GameScreen.APP_HUB.name },
            replies = if (messengerContact == "FREYA") freyaReplies else nexusReplies,
            onReply = { reply ->
                if (messengerContact == "FREYA") {
                    freyaReplies = (freyaReplies + reply).takeLast(50)
                    messengerPreferences.edit().putString("freya_replies", freyaReplies.joinToString("\u001F")).apply()
                } else {
                    nexusReplies = (nexusReplies + reply).takeLast(50)
                    messengerPreferences.edit().putString("nexus_replies", nexusReplies.joinToString("\u001F")).apply()
                }
            }
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
            "DATABASE INTELIJEN", "A.E.G.I.S. / INDEKS TERBATAS",
            buildString {
                append("ORGANISASI  /  A.E.G.I.S.\n")
                append("Advanced Espionage & Global Intelligence Service\n")
                append("MARKAS PUSAT  /  MANILA, FILIPINA\n\n")
                append("INDEKS MISI TERDAFTAR\n")
                Campaign01Catalog.missions.forEach { mission ->
                    val status = if (mission.id in currentGameState.completedMissionIds) "TERVERIFIKASI" else "BELUM SELESAI"
                    append("• ").append(mission.title).append("  /  ").append(status).append("\n")
                    append("  ID: ").append(mission.id).append("\n")
                    append("  TUJUAN: ").append(mission.objectives.size).append("  /  XP: ").append(mission.rewardXp).append("\n\n")
                }
                append("Seluruh data pada indeks ini merupakan bagian dari simulasi fiktif A.E.G.I.S.")
            },
            CyberAssets.database, { screenName = GameScreen.APP_HUB.name }
        )
        GameScreen.DOSSIER -> InfoAppScreen(
            "BERKAS PERSONEL", "CATATAN PERSONEL / REKRUT",
            "IDENTITAS: AGEN MAGANG\n" +
                "ORGANISASI: A.E.G.I.S.\n" +
                "MENTOR: FREYA KNOX\n" +
                "RANK: " + currentGameState.rank.replace('_', ' ') + "\n" +
                "XP: " + currentGameState.experience + "\n" +
                "CLEARANCE: " + currentGameState.clearance + "\n" +
                "BAB: " + currentGameState.storyBeat.coerceAtMost(7) + "/7\n" +
                "MISI SELESAI: " + currentGameState.completedMissionIds.size + "\n" +
                "BUKTI TERCATAT: " + currentGameState.evidence.size + "\n\n" +
                "CATATAN: Akses personel dan arsip berkembang melalui operasi yang berhasil diverifikasi." +
                if (currentGameState.storyBeat >= 7) "\n\nCATATAN TERBATAS / ANOMALI WAKTU\nDua sistem internal mencatat waktu penerimaan transmisi yang berbeda. Penyebab belum diketahui. Status: BELUM TERVERIFIKASI." else "",
            CyberAssets.document, { screenName = GameScreen.APP_HUB.name }
        )
        GameScreen.ANALYSIS -> InfoAppScreen(
            "ANALISIS", "RUANG KERJA INTELIJEN",
            buildString {
                append("CATATAN BUKTI  /  ").append(currentGameState.evidence.size).append("\n\n")
                if (currentGameState.evidence.isEmpty()) {
                    append("Belum ada bukti yang dicatat. Selesaikan tugas dan verifikasi hasilnya untuk menambahkan rekaman dengan sumber yang jelas.")
                } else {
                    currentGameState.evidence.forEach { evidence ->
                        append(evidence.title.uppercase()).append("\n")
                        append("STATUS: ").append(evidence.status.name.replace('_', ' ')).append("\n")
                        append("SUMBER: ").append(evidence.source).append("\n")
                        append("MISI: ").append(evidence.missionId).append("\n")
                        append(evidence.summary).append("\n\n")
                    }
                }
                append("Status: TERKONFIRMASI, DISIMPULKAN, DIPERSELISIHKAN, BELUM TERVERIFIKASI.")
            },
            CyberAssets.dataAnalysis, { screenName = GameScreen.APP_HUB.name }
        )
        GameScreen.OPERATIONS -> InfoAppScreen(
            "OPERASI", "AKSES MISI / PEMERIKSAAN IZIN",
            buildString {
                append("CAMPAIGN 01  /  THE RECRUITMENT\n")
                append("CHAPTER 001  /  FIRST CONTACT\n")
                append("PROGRES CERITA: ").append(currentGameState.storyBeat.coerceAtMost(7)).append("/7\n\n")
                Campaign01Catalog.missions.forEach { mission ->
                    val completed = mission.id in currentGameState.completedMissionIds
                    val active = mission.id == currentGameState.activeMissionId
                    append(if (completed) "[SELESAI] " else if (active) "[AKTIF] " else "[TERCATAT] ")
                    append(mission.title).append("\n")
                    append("XP: ").append(mission.rewardXp).append("  /  CLEARANCE: ").append(mission.requiredClearance).append("\n\n")
                }
                append("Operasi lanjutan belum dibuka. Kasus berikutnya tidak akan ditampilkan sebelum konten dan otorisasinya siap.")
            },
            CyberAssets.investigation, { screenName = GameScreen.APP_HUB.name }
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
            CyberAssets.document, { screenName = GameScreen.MAIN_MENU.name }
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
            CyberAssets.document, { screenName = GameScreen.MAIN_MENU.name }
        )
        GameScreen.ACHIEVEMENTS -> InfoAppScreen(
            "PENCAPAIAN", "A.E.G.I.S. / PENGHARGAAN",
            buildString {
                val awards = listOf(
                    "FIRST_SIGNAL" to ("Sinyal Pertama" to "first_contact.signal"),
                    "IDENTITY_VERIFIED" to ("Identitas Terverifikasi" to "first_contact.identity")
                )
                awards.forEach { (_, award) ->
                    val earned = award.second in currentGameState.completedMissionIds
                    append(if (earned) "[DIPEROLEH] " else "[TERKUNCI] ")
                    append(award.first).append("\n")
                }
                append(if (currentGameState.storyBeat >= 7) "[DIPEROLEH] Rekrutmen A.E.G.I.S. Selesai" else "[TERKUNCI] Rekrutmen A.E.G.I.S. Selesai")
            },
            CyberAssets.investigation, { screenName = GameScreen.MAIN_MENU.name }
        )
        GameScreen.SETTINGS -> SettingsScreen(
            reducedMotion = reducedMotion,
            onReducedMotionChange = onReducedMotionChange,
            onBack = { screenName = GameScreen.MAIN_MENU.name }
        )
        GameScreen.LANGUAGE -> InfoAppScreen(
            "BAHASA", "PREFERENSI LOKALISASI",
            "BAHASA AKTIF: BAHASA INDONESIA\n\nPaket bahasa Inggris belum tersedia. Pilihan bahasa lain akan diaktifkan setelah seluruh dialog, misi, dan antarmuka memiliki terjemahan yang konsisten.",
            CyberAssets.document, { screenName = GameScreen.MAIN_MENU.name }
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
                    gameStateRepository.save(GameState(lastCheckpoint = "new_campaign"))
                    preferences.edit().putInt("story_stage", 0).apply()
                    saveStage(0)
                    screenName = GameScreen.CAMPAIGN.name
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
            Text("PENGETAHUAN ADALAH KEKUATAN.\nINTELIJEN ADALAH TANGGUNG JAWAB.",
                color = Color(0xFF8AA8AE), fontSize = 10.sp, lineHeight = 17.sp,
                letterSpacing = 1.2.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(Modifier.height(46.dp))
            Surface(color = Color(0xFF0B2028).copy(alpha = 0.82f), shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, Color(0xFF28515B)), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    MenuButton("LANJUTKAN KAMPANYE", "Lanjutkan berkas rekrutmenmu", onContinue, true, CyberAssets.home)
                    MenuButton("KAMPANYE BARU", "Mulai Bab 001 — Kontak Pertama", onNewCampaign, icon = CyberAssets.intelligence)
                    MenuButton("PUSAT APLIKASI", "Buka perangkat A.E.G.I.S.-mu", onHub, icon = CyberAssets.connection)
                    MenuButton("PROFIL AGEN", "Rank, pengalaman, dan clearance", onProfile, icon = CyberAssets.profile)
                    MenuButton("ARSIP", "Bukti dan riwayat operasi", onArchives, icon = CyberAssets.archive)
                    MenuButton("PENCAPAIAN", "Penghargaan dan milestone", onAchievements, icon = CyberAssets.achievements)
                    MenuButton("PENGATURAN", "Aksesibilitas dan preferensi tampilan", onSettings, icon = CyberAssets.settings)
                    MenuButton("BAHASA", "Preferensi bahasa permainan", onLanguage, icon = CyberAssets.document)
                }
            }
            Spacer(Modifier.height(30.dp))
            Text("MANILA  /  KOMANDO PUSAT  /  AKSES AMAN",
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
private fun CampaignScreen(storyStage: Int, onBack: () -> Unit, onContinue: () -> Unit, onHub: () -> Unit) {
    GamePage("KAMPANYE", "A.E.G.I.S. / OPERASI CERITA", onBack, onHub) {
        GamePanel {
            Text("KAMPANYE 01", color = CyberPalette.SteelGray, fontSize = 10.sp, letterSpacing = 1.5.sp)
            Text("REKRUTMEN", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Black)
            Text("Sebuah undangan. Pilihan yang tak diketahui. Langkah pertama menuju dunia intelijen.",
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            HorizontalDivider(color = CyberPalette.Divider)
            Text("BAB 001", color = CyberPalette.Crimson, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("KONTAK PERTAMA", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Progres: " + if (storyStage >= 7) "REKRUTMEN SELESAI" else "$storyStage/7 tahap cerita",
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            LinearProgressIndicator(progress = { storyStage.coerceAtMost(7) / 7f },
                modifier = Modifier.fillMaxWidth(), color = CyberPalette.Crimson, trackColor = CyberPalette.Divider)
            CPPrimaryButton(if (storyStage >= 7) "BUKA PUSAT A.E.G.I.S." else "LANJUTKAN CERITA", onContinue)
        }
        GamePanel {
            Text("STRUKTUR KAMPANYE", color = CyberPalette.SteelGray, fontSize = 10.sp, letterSpacing = 1.3.sp)
            Text("Adegan cerita dan penemuan akan mengarah ke operasi terpandu. Bab baru tetap tersembunyi sampai waktunya terungkap.",
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun StoryScreen(
    beat: StoryBeat, stage: Int, mission: com.zeroday.cyberprotocol.core.model.MissionDefinition?,
    hintLevel: Int, onRequestHint: () -> Unit,
    code: TextFieldValue, onCodeChange: (TextFieldValue) -> Unit,
    output: String, feedback: String, saveStatus: String, onSaveCode: () -> Unit,
    onRun: () -> Unit, onSubmit: () -> Unit, onContinue: () -> Unit,
    onBack: () -> Unit, onHub: () -> Unit
) {
    GamePage("KONTAK PERTAMA", "BAB 001  /  REKRUTMEN", onBack, onHub) {
        GamePanel {
            Text(beat.eyebrow, color = CyberPalette.Crimson, fontSize = 10.sp,
                letterSpacing = 1.4.sp, fontWeight = FontWeight.Bold)
            Text(beat.title, color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Black)
            Text(beat.body, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 22.sp)
        }
        if (stage == 4 || stage == 5) {
            GamePanel {
                Text("NEXUS  /  BANTUAN PELATIHAN", color = Color(0xFF83C7D0),
                    fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(mission?.briefing.orEmpty(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                mission?.objectives?.forEachIndexed { index, objective ->
                    Text("${index + 1}. ${objective.title}", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text(objective.description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
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
                Surface(color = Color(0xFF080D12), shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, CyberPalette.Divider)) {
                    Column(Modifier.fillMaxWidth().padding(12.dp)) {
                        Text("KELUARAN TERMINAL", color = CyberPalette.SteelGray, fontSize = 9.sp)
                        Text(output, color = Color(0xFFB7E4C7), fontFamily = FontFamily.Monospace)
                    }
                }
                if (feedback.isNotBlank()) Text(feedback, color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                CPPrimaryButton("KIRIM UNTUK VERIFIKASI", onSubmit)
                Text("JALANKAN hanya menjalankan kode. SUBMIT memeriksa tujuan dan melanjutkan cerita hanya jika hasilnya terverifikasi.",
                    color = CyberPalette.SteelGray, fontSize = 10.sp)
            }
        } else {
            CPPrimaryButton(beat.button, onContinue)
        }
        Text("TITIK SIMPAN " + (stage + 1) + " / 7", color = CyberPalette.SteelGray, fontSize = 10.sp, letterSpacing = 1.2.sp)
    }
}

@Composable
private fun MissionDebriefScreen(
    title: String,
    feedback: String,
    xpAwarded: Int,
    rank: String,
    totalExperience: Int,
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
    replies: List<String>,
    onReply: (String) -> Unit
) {
    val contactName = when (selectedContact) {
        "FREYA" -> "FREYA KNOX"
        "NEXUS" -> "NEXUS"
        else -> "PESAN AMAN"
    }
    GamePage(
        contactName,
        if (selectedContact.isBlank()) "A.E.G.I.S. / DAFTAR KONTAK" else "A.E.G.I.S. / KANAL TERENKRIPSI",
        onBack = { if (selectedContact.isBlank()) onClose() else onSelectContact("") },
        onHub = null
    ) {
        if (selectedContact.isBlank()) {
            GamePanel {
                Text("KONTAK TERSEDIA", color = CyberPalette.SteelGray, fontSize = 10.sp, letterSpacing = 1.2.sp)
                Surface(
                    onClick = { onSelectContact("FREYA") },
                    color = Color(0xFF1A1D24),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, CyberPalette.Divider),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(color = Color(0xFF24333A), shape = RoundedCornerShape(50)) {
                            Text("FK", Modifier.padding(12.dp), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Column(Modifier.weight(1f)) {
                            Text("FREYA KNOX", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("MENTOR UTAMA  ·  ONLINE", color = CyberPalette.Success, fontSize = 9.sp)
                        }
                        Text("›", color = CyberPalette.Crimson, fontSize = 24.sp)
                    }
                }
                Surface(
                    onClick = { onSelectContact("NEXUS") },
                    color = Color(0xFF1A1D24),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, CyberPalette.Divider),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(color = Color(0xFF24333A), shape = RoundedCornerShape(50)) {
                            Text("NX", Modifier.padding(12.dp), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Column(Modifier.weight(1f)) {
                            Text("NEXUS", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("SISTEM KOMUNIKASI INTELIJEN", color = CyberPalette.SteelGray, fontSize = 9.sp)
                        }
                        Text("›", color = CyberPalette.Crimson, fontSize = 24.sp)
                    }
                }
            }
        } else {
            GamePanel {
                if (selectedContact == "FREYA") {
                    ChatBubble("Freya Knox", "Bagus. Rekrutmenmu kini resmi. Aku akan membimbing pelatihan awalmu dan memastikan kamu memahami setiap sistem yang boleh kamu gunakan.", false)
                    ChatBubble("Freya Knox", "Aku sudah memeriksa berkasmu. Aku melihat potensi. Sementara itu, mungkin kamu melihat daftar panjang hal-hal yang belum kamu kuasai. Penilaian yang sangat masuk akal.", false)
                    replies.forEachIndexed { index, message ->
                        ChatBubble("KAMU", message, true)
                        ChatBubble("Freya Knox",
                            if (index % 2 == 0) "Dimengerti. Kita akan menjalani satu operasi demi satu operasi. Kamu boleh bertanya; tidak perlu berpura-pura paham."
                            else "Pertanyaan bagus. Aku akan menjawabnya saat konteks operasinya tersedia. Jangan khawatir, aku jarang kehabisan kata-kata.",
                            false)
                    }
                    listOf("Dimengerti. Kita mulai dari mana?", "Masih banyak yang harus kupelajari.", "Mengapa kamu memilihku?").forEach { choice ->
                        OutlinedButton(onClick = { onReply(choice) }, modifier = Modifier.fillMaxWidth()) { Text(choice) }
                    }
                } else {
                    ChatBubble("NEXUS", "Kanal komunikasi A.E.G.I.S. aktif. Status personel: rekrut. Seluruh tugas latihan berjalan secara lokal pada perangkat ini.", false)
                    ChatBubble("NEXUS", "Aku dapat menjelaskan status operasi, menyimpan konteks teknis, dan meneruskan instruksi yang telah disahkan mentor.", false)
                    replies.forEach { message ->
                        ChatBubble("KAMU", message, true)
                        ChatBubble("NEXUS", "Permintaan diterima. Untuk saat ini, gunakan panel Operasi dan Berkas untuk meninjau informasi yang tersedia.", false)
                    }
                    listOf("Tampilkan status personel.", "Di mana catatan operasiku?", "Apakah sistem ini berjalan offline?").forEach { choice ->
                        OutlinedButton(onClick = { onReply(choice) }, modifier = Modifier.fillMaxWidth()) { Text(choice) }
                    }
                }
                Text("Pilihan dialog awal hanya memengaruhi balasan saat ini. Pilihan tersebut tidak mengubah cabang kampanye atau nilai hubungan.",
                    color = CyberPalette.SteelGray, fontSize = 10.sp)
            }
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
    GamePage("TERMINAL", "LINGKUNGAN VIRTUAL A.E.G.I.S.", onBack, null) {
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
