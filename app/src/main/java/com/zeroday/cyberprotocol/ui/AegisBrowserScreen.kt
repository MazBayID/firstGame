package com.zeroday.cyberprotocol.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroday.cyberprotocol.core.model.CaseDefinition
import com.zeroday.cyberprotocol.core.model.CaseProgress
import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.core.model.MissionDefinition
import com.zeroday.cyberprotocol.core.model.GameLanguage

/** Fictional credentials used only by the local A.E.G.I.S. intranet simulation. */
internal object AegisIntranetAccess {
    const val AGENT_ID = "RECRUIT-014"
    const val ACCESS_CODE = "FIRSTCONTACT"

    fun authenticate(agentId: String, accessCode: String): Boolean =
        agentId.trim().equals(AGENT_ID, ignoreCase = true) &&
            accessCode.trim().equals(ACCESS_CODE, ignoreCase = true)
}

@Composable
internal fun AegisBrowserScreen(
    authenticated: Boolean,
    selectedLanguage: GameLanguage,
    page: String,
    mission: MissionDefinition?,
    gameState: GameState,
    evidence: List<EvidenceRecord>,
    caseDefinition: CaseDefinition,
    caseProgress: CaseProgress,
    downloadedFiles: Set<String>,
    onReadEvidenceAsset: (String) -> String?,
    canRecordEvidence: (String) -> Boolean,
    onRecordEvidence: (String) -> Unit,
    onPageChange: (String) -> Unit,
    onAuthenticate: (String, String) -> Boolean,
    onDownloadFile: (String) -> Boolean,
    onOpenEditor: () -> Unit,
    onSubmitMission: () -> Unit,
    canSubmitMission: Boolean,
    submissionStatus: String,
    onClose: () -> Unit
) {
    var agentId by rememberSaveable { mutableStateOf("") }
    var accessCode by rememberSaveable { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    var downloadFeedback by remember { mutableStateOf("") }
    var evidenceAssetPreview by remember { mutableStateOf<Triple<String, String, String>?>(null) }
    var downloadedInSession by remember { mutableStateOf(emptySet<String>()) }

    Column(
        Modifier.fillMaxSize()
            .background(Color(0xFF0A1118))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(browserText(selectedLanguage, "A.E.G.I.S. BROWSER", "A.E.G.I.S. BROWSER"), color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Black)
                Text(browserText(selectedLanguage, "INTRANET NODE  /  LOCAL OFFLINE NETWORK", "NODE INTRANET  /  JARINGAN LOKAL OFFLINE"), color = Color(0xFF83C7D0), fontSize = 9.sp)
            }
            OutlinedButton(onClick = onClose) { Text(browserText(selectedLanguage, "EXIT", "KELUAR")) }
        }
        HorizontalDivider(color = Color(0xFF26343D))

        if (!authenticated) {
            BrowserPanel {
                Text(browserText(selectedLanguage, "SECURE ACCESS", "AKSES AMAN"), color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(browserText(selectedLanguage, "A.E.G.I.S. internal portal", "Portal internal A.E.G.I.S."), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(
                    browserText(selectedLanguage, "Sign in with the fictional credentials provided by Freya. This portal runs locally in the game and sends no data to the internet.", "Masuk menggunakan kredensial fiktif yang diberikan Freya. Portal ini berjalan lokal di dalam game dan tidak mengirim data ke internet."),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                OutlinedTextField(
                    value = agentId,
                    onValueChange = { agentId = it.take(40) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text(browserText(selectedLanguage, "PERSONNEL ID", "ID PERSONEL")) }
                )
                OutlinedTextField(
                    value = accessCode,
                    onValueChange = { accessCode = it.take(40) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text(browserText(selectedLanguage, "ACCESS CODE", "KODE AKSES")) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                if (loginError.isNotBlank()) {
                    Text(loginError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
                Button(
                    onClick = {
                        loginError = if (onAuthenticate(agentId, accessCode)) {
                            ""
                        } else {
                            browserText(selectedLanguage, "Credentials do not match. Check Freya’s message again.", "Kredensial tidak cocok. Periksa kembali pesan dari Freya.")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(browserText(selectedLanguage, "ENTER INTRANET", "MASUK KE INTRANET")) }
            }
        } else {
            BrowserNavigation(page, selectedLanguage, onPageChange)
            when (page) {
                "mission" -> BrowserPanel {
                    Text(browserText(selectedLanguage, "MISSION CONTROL", "KENDALI MISI"), color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    if (mission == null) {
                        Text(browserText(selectedLanguage, "No active operation.", "Belum ada operasi aktif."), color = Color.White, fontSize = 18.sp)
                        Text(browserText(selectedLanguage, "Return to Campaign to continue the orientation sequence.", "Kembali ke Campaign untuk melanjutkan rangkaian orientasi."), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        Text(CaseLocalizedContent.missionTitle(mission, selectedLanguage), color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                        Text(CaseLocalizedContent.missionBriefing(mission, selectedLanguage), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        mission.objectives.forEachIndexed { index, objective ->
                            Text("${index + 1}. ${CaseLocalizedContent.objectiveTitle(mission, objective, index, selectedLanguage)}", color = Color.White, fontWeight = FontWeight.SemiBold)
                            Text(CaseLocalizedContent.objectiveDescription(mission, objective, index, selectedLanguage), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        }
                        HorizontalDivider(color = Color(0xFF26343D))
                        Text(browserText(selectedLanguage, "OPERATION REQUIREMENTS", "PERSYARATAN OPERASI"), color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(browserText(selectedLanguage, "MINIMUM CLEARANCE  /  ${mission.requiredClearance}", "OTORISASI MINIMUM  /  ${mission.requiredClearance}"), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                        mission.allowedFiles.sorted().forEach { fileName ->
                            Text(
                                "${fileName}  /  " + if (fileName in mission.writableFiles) browserText(selectedLanguage, "READ-WRITE", "BACA-TULIS") else browserText(selectedLanguage, "READ ONLY", "HANYA BACA"),
                                color = Color(0xFFB9D1C6),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp
                            )
                        }
                        HorizontalDivider(color = Color(0xFF26343D))
                        Text(browserText(selectedLanguage, "SUBMISSION STATUS", "STATUS PENGIRIMAN"), color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(submissionStatus.ifBlank { browserText(selectedLanguage, "No result submitted yet.", "Belum ada hasil yang dikirim.") }, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        OutlinedButton(onClick = onOpenEditor, modifier = Modifier.fillMaxWidth()) {
                            Text(browserText(selectedLanguage, "OPEN CODE EDITOR", "BUKA CODE EDITOR"))
                        }
                        Button(
                            onClick = onSubmitMission,
                            enabled = canSubmitMission,
                            modifier = Modifier.fillMaxWidth()
                        ) { Text(browserText(selectedLanguage, "SUBMIT RESULT FOR VERIFICATION", "KIRIM HASIL UNTUK VERIFIKASI")) }
                        Text(
                            browserText(selectedLanguage, "Submission uses the exact source from the latest successful RUN. Any code change requires another RUN.", "Pengiriman memakai kode yang sama dengan eksekusi RUN terakhir yang berhasil. Mengubah kode mengharuskan RUN ulang."),
                            color = Color(0xFF9BAEB4),
                            fontSize = 10.sp
                        )
                    }
                }
                "repository" -> BrowserPanel {
                    Text(browserText(selectedLanguage, "FILE REPOSITORY", "FILE REPOSITORY"), color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(browserText(selectedLanguage, "Official files for the active operation.", "Berkas resmi untuk operasi yang sedang aktif."), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    if (mission == null || mission.initialFiles.isEmpty()) {
                        Text(browserText(selectedLanguage, "No downloadable files for the active operation.", "Belum ada berkas unduhan untuk operasi aktif."), color = Color.White)
                    } else {
                        mission.initialFiles.toSortedMap().forEach { (fileName, content) ->
                            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text(fileName, color = Color.White, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                Text(browserText(selectedLanguage, "A.E.G.I.S. / ${content.length} characters / read only", "A.E.G.I.S. / ${content.length} karakter / hanya baca"), color = Color(0xFF9BAEB4), fontSize = 9.sp)
                                if (fileName in downloadedFiles || fileName in downloadedInSession) {
                                    Text(browserText(selectedLanguage, "SAVED IN MISSION WORKSPACE", "TERSIMPAN DI RUANG KERJA MISI"), color = Color(0xFF83C7D0), fontSize = 10.sp)
                                } else {
                                    OutlinedButton(
                                        onClick = {
                                            val installed = onDownloadFile(fileName)
                                            if (installed) downloadedInSession = downloadedInSession + fileName
                                            downloadFeedback = if (installed) {
                                                browserText(selectedLanguage, "$fileName downloaded to the mission workspace.", "$fileName berhasil diunduh ke ruang kerja misi.")
                                            } else {
                                                browserText(selectedLanguage, "Download rejected by workspace policy.", "Unduhan ditolak oleh kebijakan ruang kerja.")
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) { Text(browserText(selectedLanguage, "DOWNLOAD FILE", "UNDUH BERKAS")) }
                                }
                            }
                            HorizontalDivider(color = Color(0xFF26343D))
                        }
                    }
                    if (downloadFeedback.isNotBlank()) {
                        Text(downloadFeedback, color = MaterialTheme.colorScheme.secondary, fontSize = 11.sp)
                    }
                }
                "reports" -> BrowserPanel {
                    Text(browserText(selectedLanguage, "INTELLIGENCE REPORTS", "LAPORAN INTELIJEN"), color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    if (evidence.isEmpty()) {
                        Text(browserText(selectedLanguage, "No evidence reports have been recorded.", "Belum ada laporan bukti yang tercatat."), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        evidence.sortedByDescending { it.recordedAtEpochMillis }.forEach { record ->
                            val concealed = record.id in caseDefinition.requiredEvidenceIds &&
                                record.id !in caseProgress.discoveredEvidenceIds
                            Text(record.title, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(
                                if (concealed) browserText(selectedLanguage, "Source content has not been opened. Use the original source button to inspect it.", "Isi sumber belum dibuka. Gunakan tombol sumber asli untuk memeriksanya.")
                                else record.summary,
                                color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp
                            )
                            Text("${localizedEvidenceStatus(record.status, selectedLanguage)}  /  ${record.source}", color = Color(0xFF83C7D0), fontSize = 9.sp)
                            record.provenance.sourceAssetId?.let { assetId ->
                                Text(browserText(selectedLanguage, "SHARED SOURCE  /  $assetId", "SUMBER BERSAMA  /  $assetId"), color = Color(0xFF9AB4B8), fontSize = 8.sp)
                                record.provenance.sourcePath?.let { path ->
                                    Text(path, color = Color(0xFF71858B), fontSize = 8.sp)
                                }
                                OutlinedButton(
                                    onClick = {
                                        val content = onReadEvidenceAsset(assetId)
                                        evidenceAssetPreview = content?.let { Triple(record.id, record.title, it) }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) { Text(browserText(selectedLanguage, "OPEN ORIGINAL SOURCE", "BUKA SUMBER ASLI")) }
                            }
                            HorizontalDivider(color = Color(0xFF26343D))
                        }
                    }
                }
                "profile" -> BrowserPanel {
                    Text(browserText(selectedLanguage, "AGENT PROFILE", "PROFIL AGEN"), color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("RECRUIT-014", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(browserText(selectedLanguage, "RANK  /  ${gameState.rank.replace('_', ' ')}", "PANGKAT  /  ${gameState.rank.replace('_', ' ')}"), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(browserText(selectedLanguage, "EXPERIENCE  /  ${gameState.experience} XP", "PENGALAMAN  /  ${gameState.experience} XP"), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(browserText(selectedLanguage, "CLEARANCE  /  ${gameState.clearance}", "OTORISASI  /  ${gameState.clearance}"), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(browserText(selectedLanguage, "MISSIONS COMPLETED  /  ${gameState.completedMissionIds.size}", "MISI SELESAI  /  ${gameState.completedMissionIds.size}"), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                "archive" -> BrowserPanel {
                    Text(browserText(selectedLanguage, "OPERATIONS ARCHIVE", "ARSIP OPERASI"), color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    if (gameState.completedMissionIds.isEmpty()) {
                        Text(browserText(selectedLanguage, "No operations have been archived.", "Belum ada operasi yang diarsipkan."), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        gameState.completedMissionIds.sorted().forEach { missionId ->
                            val archivedMission = com.zeroday.cyberprotocol.data.Campaign01Catalog.find(missionId)
                            Text(archivedMission?.let { CaseLocalizedContent.missionTitle(it, selectedLanguage) } ?: missionId, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(browserText(selectedLanguage, "VERIFIED  /  ${missionId}", "TERVERIFIKASI  /  ${missionId}"), color = Color(0xFF83C7D0), fontSize = 9.sp)
                        }
                    }
                }
                else -> BrowserPanel {
                    Text(browserText(selectedLanguage, "WELCOME TO A.E.G.I.S.", "SELAMAT DATANG DI A.E.G.I.S."), color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(browserText(selectedLanguage, "Internal Operations Network", "Jaringan Operasi Internal"), color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Black)
                    Text(
                        browserText(selectedLanguage, "This portal provides access to assignments, the file repository, intelligence reports, and personnel profiles. All content is an offline simulation.", "Portal ini menyediakan akses ke penugasan, repositori berkas, laporan intelijen, dan profil personel. Seluruh konten merupakan simulasi offline."),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                    BrowserLink("MISSION CONTROL", browserText(selectedLanguage, "Briefing and active operation status", "Briefing dan status operasi aktif")) { onPageChange("mission") }
                    BrowserLink(browserText(selectedLanguage, "FILE REPOSITORY", "FILE REPOSITORY"), browserText(selectedLanguage, "Authorized supporting files", "Berkas pendukung yang diizinkan")) { onPageChange("repository") }
                    BrowserLink(browserText(selectedLanguage, "INTELLIGENCE REPORTS", "LAPORAN INTELIJEN"), browserText(selectedLanguage, "Evidence and source records", "Catatan bukti dan sumber")) { onPageChange("reports") }
                    BrowserLink(browserText(selectedLanguage, "OPERATIONS ARCHIVE", "ARSIP OPERASI"), browserText(selectedLanguage, "Verified operation history", "Riwayat operasi yang telah diverifikasi")) { onPageChange("archive") }
                    BrowserLink(browserText(selectedLanguage, "AGENT PROFILE", "PROFIL AGEN"), browserText(selectedLanguage, "Personnel identity and access", "Identitas dan akses personel")) { onPageChange("profile") }
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
                        Text(if (canRecordEvidence(evidenceId)) browserText(selectedLanguage, "MARK AS REVIEWED", "CATAT SEBAGAI DIPERIKSA") else browserText(selectedLanguage, "CLOSE", "TUTUP"))
                    }
                }
            )
        }
    }
}

@Composable
private fun BrowserNavigation(page: String, selectedLanguage: GameLanguage, onPageChange: (String) -> Unit) {
    Row(
        Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        listOf(
            "home" to browserText(selectedLanguage, "HOME", "HOME"),
            "mission" to browserText(selectedLanguage, "MISSIONS", "MISI"),
            "repository" to browserText(selectedLanguage, "FILES", "BERKAS"),
            "reports" to browserText(selectedLanguage, "REPORTS", "LAPORAN"),
            "archive" to browserText(selectedLanguage, "ARCHIVE", "ARSIP"),
            "profile" to browserText(selectedLanguage, "PROFILE", "PROFIL")
        ).forEach { (target, label) ->
            if (page == target) {
                Button(onClick = { onPageChange(target) }) { Text(label, fontSize = 9.sp) }
            } else {
                OutlinedButton(onClick = { onPageChange(target) }) { Text(label, fontSize = 9.sp) }
            }
        }
    }
}

@Composable
private fun BrowserPanel(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141F28)),
        border = BorderStroke(1.dp, Color(0xFF2A3B45))
    ) {
        Column(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) { content() }
    }
}

@Composable
private fun BrowserLink(title: String, description: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color(0xFF172832),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF2A3B45)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(description, color = Color(0xFF9BAEB4), fontSize = 10.sp)
        }
    }
}


private fun browserText(language: GameLanguage, english: String, indonesian: String): String =
    localizedUiText(language, english, indonesian)
