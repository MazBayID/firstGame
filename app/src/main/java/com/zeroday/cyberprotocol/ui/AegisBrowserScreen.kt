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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.core.model.MissionDefinition

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
    page: String,
    mission: MissionDefinition?,
    gameState: GameState,
    evidence: List<EvidenceRecord>,
    downloadedFiles: Set<String>,
    onReadEvidenceAsset: (String) -> String?,
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
                Text("A.E.G.I.S. BROWSER", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Black)
                Text("INTRANET NODE  /  LOCAL OFFLINE NETWORK", color = Color(0xFF83C7D0), fontSize = 9.sp)
            }
            OutlinedButton(onClick = onClose) { Text("KELUAR") }
        }
        HorizontalDivider(color = Color(0xFF26343D))

        if (!authenticated) {
            BrowserPanel {
                Text("SECURE ACCESS", color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("Portal internal A.E.G.I.S.", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Masuk menggunakan kredensial fiktif yang diberikan Freya. Portal ini berjalan lokal di dalam game dan tidak mengirim data ke internet.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                OutlinedTextField(
                    value = agentId,
                    onValueChange = { agentId = it.take(40) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("ID PERSONEL") }
                )
                OutlinedTextField(
                    value = accessCode,
                    onValueChange = { accessCode = it.take(40) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("KODE AKSES") },
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
                            "Kredensial tidak cocok. Periksa kembali pesan dari Freya."
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("MASUK KE INTRANET") }
            }
        } else {
            BrowserNavigation(page, onPageChange)
            when (page) {
                "mission" -> BrowserPanel {
                    Text("MISSION CONTROL", color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    if (mission == null) {
                        Text("Belum ada operasi aktif.", color = Color.White, fontSize = 18.sp)
                        Text("Kembali ke Campaign untuk melanjutkan rangkaian orientasi.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        Text(mission.title, color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                        Text(mission.briefing, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        mission.objectives.forEachIndexed { index, objective ->
                            Text("${index + 1}. ${objective.title}", color = Color.White, fontWeight = FontWeight.SemiBold)
                            Text(objective.description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        }
                        HorizontalDivider(color = Color(0xFF26343D))
                        Text("PERSYARATAN OPERASI", color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("CLEARANCE MINIMUM  /  ${mission.requiredClearance}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                        mission.allowedFiles.sorted().forEach { fileName ->
                            Text(
                                "${fileName}  /  " + if (fileName in mission.writableFiles) "BACA-TULIS" else "HANYA BACA",
                                color = Color(0xFFB9D1C6),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp
                            )
                        }
                        HorizontalDivider(color = Color(0xFF26343D))
                        Text("STATUS PENGIRIMAN", color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(submissionStatus.ifBlank { "Belum ada hasil yang dikirim." }, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        OutlinedButton(onClick = onOpenEditor, modifier = Modifier.fillMaxWidth()) {
                            Text("BUKA CODE EDITOR")
                        }
                        Button(
                            onClick = onSubmitMission,
                            enabled = canSubmitMission,
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("KIRIM HASIL UNTUK VERIFIKASI") }
                        Text(
                            "Pengiriman memakai kode yang sama dengan eksekusi RUN terakhir yang berhasil. Mengubah kode mengharuskan RUN ulang.",
                            color = Color(0xFF9BAEB4),
                            fontSize = 10.sp
                        )
                    }
                }
                "repository" -> BrowserPanel {
                    Text("FILE REPOSITORY", color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("Berkas resmi untuk operasi yang sedang aktif.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    if (mission == null || mission.initialFiles.isEmpty()) {
                        Text("Belum ada berkas unduhan untuk operasi aktif.", color = Color.White)
                    } else {
                        mission.initialFiles.toSortedMap().forEach { (fileName, content) ->
                            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text(fileName, color = Color.White, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                Text("A.E.G.I.S. / ${content.length} karakter / hanya baca", color = Color(0xFF9BAEB4), fontSize = 9.sp)
                                if (fileName in downloadedFiles || fileName in downloadedInSession) {
                                    Text("TERSIMPAN DI RUANG KERJA MISI", color = Color(0xFF83C7D0), fontSize = 10.sp)
                                } else {
                                    OutlinedButton(
                                        onClick = {
                                            val installed = onDownloadFile(fileName)
                                            if (installed) downloadedInSession = downloadedInSession + fileName
                                            downloadFeedback = if (installed) {
                                                "$fileName berhasil diunduh ke ruang kerja misi."
                                            } else {
                                                "Unduhan ditolak oleh kebijakan ruang kerja."
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) { Text("UNDUH BERKAS") }
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
                    Text("INTELLIGENCE REPORTS", color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    if (evidence.isEmpty()) {
                        Text("Belum ada laporan bukti yang tercatat.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        evidence.sortedByDescending { it.recordedAtEpochMillis }.forEach { record ->
                            Text(record.title, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(record.summary, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                            Text("${record.status.name}  /  ${record.source}", color = Color(0xFF83C7D0), fontSize = 9.sp)
                            record.provenance.sourceAssetId?.let { assetId ->
                                Text("SHARED SOURCE  /  $assetId", color = Color(0xFF9AB4B8), fontSize = 8.sp)
                                record.provenance.sourcePath?.let { path ->
                                    Text(path, color = Color(0xFF71858B), fontSize = 8.sp)
                                }
                                OutlinedButton(
                                    onClick = {
                                        val content = onReadEvidenceAsset(assetId)
                                        evidenceAssetPreview = content?.let { record.title to it }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) { Text("BUKA SUMBER ASLI") }
                            }
                            HorizontalDivider(color = Color(0xFF26343D))
                        }
                    }
                }
                "profile" -> BrowserPanel {
                    Text("AGENT PROFILE", color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("RECRUIT-014", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("RANK  /  ${gameState.rank.replace('_', ' ')}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("EXPERIENCE  /  ${gameState.experience} XP", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("CLEARANCE  /  ${gameState.clearance}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("MISI SELESAI  /  ${gameState.completedMissionIds.size}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                "archive" -> BrowserPanel {
                    Text("OPERATIONS ARCHIVE", color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    if (gameState.completedMissionIds.isEmpty()) {
                        Text("Belum ada operasi yang diarsipkan.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        gameState.completedMissionIds.sorted().forEach { missionId ->
                            val archivedMission = com.zeroday.cyberprotocol.data.Campaign01Catalog.find(missionId)
                            Text(archivedMission?.title ?: missionId, color = Color.White, fontWeight = FontWeight.Bold)
                            Text("TERVERIFIKASI  /  ${missionId}", color = Color(0xFF83C7D0), fontSize = 9.sp)
                        }
                    }
                }
                else -> BrowserPanel {
                    Text("SELAMAT DATANG DI A.E.G.I.S.", color = Color(0xFF83C7D0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("Internal Operations Network", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Black)
                    Text(
                        "Portal ini menyediakan akses ke penugasan, repositori berkas, laporan intelijen, dan profil personel. Seluruh konten merupakan simulasi offline.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                    BrowserLink("MISSION CONTROL", "Briefing dan status operasi aktif") { onPageChange("mission") }
                    BrowserLink("FILE REPOSITORY", "Berkas pendukung yang diizinkan") { onPageChange("repository") }
                    BrowserLink("INTELLIGENCE REPORTS", "Catatan bukti dan sumber") { onPageChange("reports") }
                    BrowserLink("OPERATIONS ARCHIVE", "Riwayat operasi yang telah diverifikasi") { onPageChange("archive") }
                    BrowserLink("AGENT PROFILE", "Identitas dan akses personel") { onPageChange("profile") }
                }
            }
        }
        evidenceAssetPreview?.let { (title, content) ->
            AlertDialog(
                onDismissRequest = { evidenceAssetPreview = null },
                title = { Text(title) },
                text = { Text(content, fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                confirmButton = {
                    TextButton(onClick = { evidenceAssetPreview = null }) { Text("TUTUP") }
                }
            )
        }
    }
}

@Composable
private fun BrowserNavigation(page: String, onPageChange: (String) -> Unit) {
    Row(
        Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        listOf(
            "home" to "HOME",
            "mission" to "MISI",
            "repository" to "BERKAS",
            "reports" to "LAPORAN",
            "archive" to "ARSIP",
            "profile" to "PROFIL"
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
