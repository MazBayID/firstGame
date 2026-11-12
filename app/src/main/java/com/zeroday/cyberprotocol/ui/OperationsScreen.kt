package com.zeroday.cyberprotocol.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroday.cyberprotocol.core.model.*

/** Operations is now the case-control surface, not a fixed list of coding missions. */
@Composable
internal fun OperationsScreen(
    gameState: GameState,
    caseDefinition: CaseDefinition,
    caseProgress: CaseProgress,
    evidenceRecords: List<EvidenceRecord>,
    selectedLanguage: GameLanguage,
    onBack: () -> Unit,
    onAcknowledgeBriefing: () -> Unit,
    onReadSource: (String) -> String?,
    onDiscoverEvidence: (String) -> Unit,
    onOpenAnalysis: () -> Unit,
    onDecisionRationaleChange: (String) -> Unit,
    onSelectDecision: (String, String) -> Unit,
    onAcknowledgeDebrief: () -> Unit
) {
    val phase = CasePhaseEvaluator.phase(caseDefinition, caseProgress)
    val records = caseDefinition.requiredEvidenceIds.mapNotNull { id ->
        evidenceRecords.firstOrNull { it.id == id }
    }
    val phaseNumber = when (phase) {
        CasePhase.BRIEFING -> 1
        CasePhase.INVESTIGATION -> 2
        CasePhase.ANALYSIS -> 3
        CasePhase.OPERATIONAL_DECISION -> 4
        CasePhase.DEBRIEFING, CasePhase.COMPLETE -> 5
    }
    val decision = caseDefinition.decisionOptions.firstOrNull { it.id == caseProgress.selectedDecisionId }
    val analysisConclusion = caseDefinition.analysisOptions.firstOrNull { it.id == caseProgress.analysisConclusionId }
    val decisionRationale = caseProgress.decisionRationale

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0A1D25), Color(0xFF090D13))))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("A.E.G.I.S.  /  MANILA COMMAND", color = Color(0xFF8AA8AE), fontSize = 9.sp)
                Text(localizedUiText(selectedLanguage, "OPERATIONS CENTER", "PUSAT OPERASI"), color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Black)
            }
            TextButton(onClick = onBack) { Text(localizedUiText(selectedLanguage, "BACK", "KEMBALI"), color = Color(0xFFBCE8EA)) }
        }

        Surface(
            color = Color(0xFF102A32), shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF31545B)), modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(localizedUiText(selectedLanguage, "ACTIVE CASE", "KASUS AKTIF"), color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(caseDefinition.title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(localizedUiText(selectedLanguage, "RX-14  ·  DATA SOURCE INVESTIGATION", "RX-14  ·  INVESTIGASI SUMBER DATA"), color = Color(0xFFB8C9CC), fontSize = 10.sp)
                LinearProgressIndicator(
                    progress = { phaseNumber / 5f }, modifier = Modifier.fillMaxWidth().height(5.dp),
                    color = Color(0xFF65C7C0), trackColor = Color(0xFF263A40)
                )
                Text(localizedUiText(selectedLanguage, "PHASE " + phaseNumber + " OF 5  ·  " + localizedPhaseName(phase, selectedLanguage), "TAHAP " + phaseNumber + " DARI 5  ·  " + localizedPhaseName(phase, selectedLanguage)),
                    color = Color(0xFF9AB4B8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("RANK " + gameState.rank.replace('_', ' ') + "   ·   " + gameState.experience +
                    " XP   ·   CLEARANCE " + gameState.clearance, color = Color(0xFFB8C9CC), fontSize = 10.sp)
            }
        }

        when (phase) {
            CasePhase.BRIEFING -> {
                SectionLabel(localizedUiText(selectedLanguage, "01  /  INTELLIGENCE BRIEFING", "01  /  BRIEFING INTELIJEN"))
                InfoPanel(caseDefinition.briefing)
                Text(localizedUiText(selectedLanguage, "You do not need to open every app. Start with the case objective, then choose relevant sources.", "Kamu tidak harus membuka semua aplikasi. Mulailah dari tujuan kasus, lalu pilih sumber yang relevan."),
                    color = Color(0xFF91A8AD), fontSize = 11.sp, lineHeight = 17.sp)
                PrimaryAction(localizedUiText(selectedLanguage, "ACKNOWLEDGE BRIEFING", "PAHAMI BRIEFING"), onAcknowledgeBriefing)
            }
            CasePhase.INVESTIGATION -> {
                SectionLabel(localizedUiText(selectedLanguage, "02  /  INVESTIGATION", "02  /  INVESTIGASI"))
                InfoPanel(localizedUiText(selectedLanguage, "Inspect the two RX-14 intake records. The time difference is a fact to record, not automatic proof of tampering.", "Periksa dua catatan penerimaan RX-14. Perbedaan waktu adalah fakta yang perlu dicatat, bukan bukti otomatis adanya manipulasi."))
                records.forEach { record ->
                    val found = record.id in caseProgress.discoveredEvidenceIds
                    EvidencePanel(
                        record = record,
                        discovered = found,
                        analyzed = record.id in caseProgress.analyzedEvidenceIds,
                        sourceContent = if (found) null else onReadSource(record.id),
                        actionLabel = if (found) localizedUiText(selectedLanguage, "ALREADY REVIEWED", "SUDAH DIPERIKSA") else localizedUiText(selectedLanguage, "OPEN ORIGINAL SOURCE", "BUKA SUMBER ASLI"),
                        actionEnabled = !found,
                        onAction = { onDiscoverEvidence(record.id) },
                        selectedLanguage = selectedLanguage
                    )
                }
                Text(caseProgress.discoveredEvidenceIds.intersect(caseDefinition.requiredEvidenceIds).size.toString() +
                    localizedUiText(selectedLanguage, " of " + caseDefinition.requiredEvidenceIds.size + " required sources inspected.", " dari " + caseDefinition.requiredEvidenceIds.size + " sumber wajib telah diperiksa."),
                    color = Color(0xFF91A8AD), fontSize = 10.sp)
            }
            CasePhase.ANALYSIS -> {
                SectionLabel(localizedUiText(selectedLanguage, "03  /  INTELLIGENCE ANALYSIS", "03  /  ANALISIS INTELIJEN"))
                InfoPanel(localizedUiText(selectedLanguage, "Both required sources have been opened. Use the Analysis workspace to record each source review and select a conclusion supported by the evidence.", "Kedua sumber wajib telah dibuka. Gunakan ruang Analisis untuk mencatat hasil pemeriksaan tiap sumber dan memilih kesimpulan yang benar-benar didukung bukti."))
                records.forEach { record ->
                    val analyzed = record.id in caseProgress.analyzedEvidenceIds
                    Surface(
                        color = Color(0xFF111F27), shape = RoundedCornerShape(7.dp),
                        border = BorderStroke(1.dp, Color(0xFF31545B)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(record.title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(record.provenance.sourceLabel, color = Color(0xFF83C7D0), fontSize = 9.sp)
                            Text(if (analyzed) localizedUiText(selectedLanguage, "ANALYSIS RECORDED", "ANALISIS TERCATAT") else localizedUiText(selectedLanguage, "AWAITING ANALYSIS", "MENUNGGU HASIL ANALISIS"),
                                color = if (analyzed) Color(0xFF8CD5AC) else Color(0xFF9AB4B8),
                                fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                PrimaryAction(localizedUiText(selectedLanguage, "OPEN ANALYSIS WORKSPACE", "BUKA RUANG ANALISIS"), onOpenAnalysis)
            }
            CasePhase.OPERATIONAL_DECISION -> {
                SectionLabel(localizedUiText(selectedLanguage, "04  /  OPERATIONAL DECISION", "04  /  KEPUTUSAN OPERASIONAL"))
                InfoPanel(analysisConclusion?.feedback
                    ?: localizedUiText(selectedLanguage, "Both sources have been analyzed. Choose an action proportionate to the available evidence.", "Dua sumber telah dianalisis. Pilih tindakan yang proporsional dengan bukti yang tersedia."))
                OutlinedTextField(
                    value = decisionRationale,
                    onValueChange = onDecisionRationaleChange,
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    label = { Text(localizedUiText(selectedLanguage, "DECISION RATIONALE", "ALASAN KEPUTUSAN")) },
                    placeholder = { Text(localizedUiText(selectedLanguage, "Explain the facts and evidence limits that support your action.", "Jelaskan fakta dan batas bukti yang mendukung tindakanmu.")) }
                )
                Text(localizedUiText(selectedLanguage, "This rationale is saved with your decision and shown during the debrief.", "Alasan ini disimpan bersama keputusan dan ditampilkan saat debrief."),
                    color = Color(0xFF91A8AD), fontSize = 10.sp, lineHeight = 15.sp)
                caseDefinition.decisionOptions.forEach { option ->
                    Surface(color = Color(0xFF111F27), shape = RoundedCornerShape(7.dp),
                        border = BorderStroke(1.dp, Color(0xFF31545B)), modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                            Text(option.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(option.description, color = Color(0xFFB7C6CB), fontSize = 11.sp, lineHeight = 16.sp)
                            OutlinedButton(
                                onClick = { onSelectDecision(option.id, decisionRationale.trim()) },
                                enabled = decisionRationale.isNotBlank(),
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFBCE8EA))
                            ) {
                                Text(localizedUiText(selectedLanguage, "SELECT ACTION", "PILIH TINDAKAN"))
                            }
                        }
                    }
                }
            }
            CasePhase.DEBRIEFING -> {
                SectionLabel(localizedUiText(selectedLanguage, "05  /  DEBRIEFING", "05  /  DEBRIEFING"))
                InfoPanel(localizedUiText(selectedLanguage, "Your decision has been recorded. Review what the sources support and what remains unknown.", "Keputusanmu telah dicatat. Tinjau kembali apa yang didukung sumber dan apa yang masih belum diketahui."))
                analysisConclusion?.let {
                    Text(localizedUiText(selectedLanguage, "ANALYSIS CONCLUSION", "KESIMPULAN ANALISIS"), color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(it.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    InfoPanel(it.feedback)
                }
                decision?.let {
                    Text(localizedUiText(selectedLanguage, "RECORDED DECISION", "KEPUTUSAN TERCATAT"), color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(it.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(it.description, color = Color(0xFFB7C6CB), fontSize = 11.sp, lineHeight = 17.sp)
                }
                Text(localizedUiText(selectedLanguage, "DECISION RATIONALE", "ALASAN KEPUTUSAN"), color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                InfoPanel(caseProgress.decisionRationale.ifBlank { localizedUiText(selectedLanguage, "No rationale was stored in this older save.", "Tidak ada alasan yang tersimpan pada save lama.") })
                InfoPanel(localizedUiText(selectedLanguage, "Fact: two records reference RX-14 and their intake times differ by four seconds. The cause has not been established. There is no basis to identify an actor or claim tampering.", "Fakta: dua catatan menyebut RX-14 dan waktu penerimaannya berselisih empat detik. Penyebabnya belum dapat dipastikan. Tidak ada dasar untuk menunjuk pelaku atau menyatakan manipulasi."))
                PrimaryAction(localizedUiText(selectedLanguage, "COMPLETE DEBRIEF", "SELESAIKAN DEBRIEF"), onAcknowledgeDebrief)
            }
            CasePhase.COMPLETE -> {
                SectionLabel(localizedUiText(selectedLanguage, "CASE STATUS  /  COMPLETE", "STATUS KASUS  /  SELESAI"))
                InfoPanel(localizedUiText(selectedLanguage, "First Contact is complete. The report preserves the distinction between facts, hypotheses, and unknowns. The RX-14 anomaly remains open for review if new evidence becomes available.", "First Contact selesai. Laporan mempertahankan perbedaan antara fakta, dugaan, dan hal yang belum diketahui. Anomali RX-14 tetap terbuka untuk pemeriksaan jika bukti baru tersedia."))
                Text(localizedUiText(selectedLanguage, "ANALYSIS CONCLUSION", "KESIMPULAN ANALISIS"), color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(analysisConclusion?.title ?: localizedUiText(selectedLanguage, "No conclusion saved", "Tidak ada kesimpulan tersimpan"), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(localizedUiText(selectedLanguage, "FINAL DECISION", "KEPUTUSAN AKHIR"), color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(decision?.title ?: localizedUiText(selectedLanguage, "No decision saved", "Tidak ada keputusan tersimpan"), color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(localizedUiText(selectedLanguage, "DECISION RATIONALE", "ALASAN KEPUTUSAN"), color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                InfoPanel(caseProgress.decisionRationale.ifBlank { localizedUiText(selectedLanguage, "No rationale was stored in this older save.", "Tidak ada alasan yang tersimpan pada save lama.") })
                if (caseDefinition.id in gameState.completedMissionIds) {
                    Text(
                        localizedUiText(selectedLanguage, "CASE REWARD  /  +${com.zeroday.cyberprotocol.data.Campaign01CaseCatalog.RX14_COMPLETION_REWARD_XP} XP", "HADIAH KASUS  /  +${com.zeroday.cyberprotocol.data.Campaign01CaseCatalog.RX14_COMPLETION_REWARD_XP} XP"),
                        color = Color(0xFF8CD5AC), fontSize = 11.sp, fontWeight = FontWeight.Bold
                    )
                }
                Text(localizedUiText(selectedLanguage, "TOTAL EXPERIENCE  /  ${gameState.experience} XP", "TOTAL PENGALAMAN  /  ${gameState.experience} XP"),
                    color = Color(0xFFB8C9CC), fontSize = 11.sp)
            }
        }

        Text(localizedUiText(selectedLanguage, "Operations manages the case and its decisions. Other apps are investigative tools; opening an app does not itself advance the case.", "Pusat Operasi mengelola kasus dan keputusan. Aplikasi lain adalah alat investigasi; membuka aplikasi bukan syarat progres dengan sendirinya."),
            color = Color(0xFF71858B), fontSize = 10.sp, lineHeight = 16.sp)
    }
}

@Composable
private fun SectionLabel(value: String) {
    Text(value, color = Color(0xFF83C7D0), fontSize = 10.sp, letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun InfoPanel(value: String) {
    Surface(color = Color(0xFF111F27), shape = RoundedCornerShape(7.dp),
        border = BorderStroke(1.dp, Color(0xFF263F48)), modifier = Modifier.fillMaxWidth()) {
        Text(value, Modifier.padding(14.dp), color = Color(0xFFC5D3D0), fontSize = 12.sp, lineHeight = 19.sp)
    }
}

@Composable
private fun EvidencePanel(
    record: EvidenceRecord,
    discovered: Boolean,
    analyzed: Boolean,
    sourceContent: String?,
    actionLabel: String,
    actionEnabled: Boolean,
    onAction: () -> Unit,
    selectedLanguage: GameLanguage
) {
    var sourceOpened by remember(record.id) { mutableStateOf(false) }
    if (sourceOpened && sourceContent != null) {
        AlertDialog(
            onDismissRequest = { sourceOpened = false },
            title = { Text(record.title) },
            text = { Text(sourceContent, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace) },
            confirmButton = {
                TextButton(onClick = {
                    sourceOpened = false
                    onAction()
                }) { Text(localizedUiText(selectedLanguage, "RECORD AS REVIEWED", "CATAT SEBAGAI DIPERIKSA")) }
            },
            dismissButton = { TextButton(onClick = { sourceOpened = false }) { Text(localizedUiText(selectedLanguage, "CLOSE", "TUTUP")) } }
        )
    }
    Surface(color = Color(0xFF111F27), shape = RoundedCornerShape(7.dp),
        border = BorderStroke(1.dp, Color(0xFF31545B)), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(record.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(record.provenance.sourceLabel, color = Color(0xFF83C7D0), fontSize = 10.sp)
            if (discovered) {
                Text(record.summary, color = Color(0xFFC5D3D0), fontSize = 11.sp, lineHeight = 17.sp)
                Text(localizedUiText(selectedLanguage, "EVIDENCE STATUS: ", "STATUS BUKTI: ") + localizedEvidenceStatus(record.status, selectedLanguage), color = Color(0xFF91A8AD), fontSize = 9.sp)
            } else {
                Text(localizedUiText(selectedLanguage, "The source record is available. Open it to inspect its contents and record it as case evidence.", "Catatan sumber tersedia. Periksa untuk membuka isi dan mencatatnya sebagai bukti kasus."),
                    color = Color(0xFF91A8AD), fontSize = 10.sp, lineHeight = 15.sp)
            }
            if (analyzed) Text(localizedUiText(selectedLanguage, "ANALYSIS RECORDED", "ANALISIS TERCATAT"), color = Color(0xFF8CD5AC), fontSize = 9.sp, fontWeight = FontWeight.Bold)
            OutlinedButton(
                onClick = {
                    if (!discovered && sourceContent != null) sourceOpened = true else onAction()
                },
                enabled = actionEnabled && (discovered || sourceContent != null),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFBCE8EA))
            ) { Text(actionLabel) }
        }
    }
}

@Composable
private fun PrimaryAction(label: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28634D))) { Text(label) }
}

