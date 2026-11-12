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
                Text("PUSAT OPERASI", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Black)
            }
            TextButton(onClick = onBack) { Text("KEMBALI", color = Color(0xFFBCE8EA)) }
        }

        Surface(
            color = Color(0xFF102A32), shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF31545B)), modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("KASUS AKTIF", color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(caseDefinition.title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("RX-14  ·  INVESTIGASI SUMBER DATA", color = Color(0xFFB8C9CC), fontSize = 10.sp)
                LinearProgressIndicator(
                    progress = { phaseNumber / 5f }, modifier = Modifier.fillMaxWidth().height(5.dp),
                    color = Color(0xFF65C7C0), trackColor = Color(0xFF263A40)
                )
                Text("TAHAP " + phaseNumber + " DARI 5  ·  " + phaseLabel(phase),
                    color = Color(0xFF9AB4B8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("RANK " + gameState.rank.replace('_', ' ') + "   ·   " + gameState.experience +
                    " XP   ·   CLEARANCE " + gameState.clearance, color = Color(0xFFB8C9CC), fontSize = 10.sp)
            }
        }

        when (phase) {
            CasePhase.BRIEFING -> {
                SectionLabel("01  /  INTELLIGENCE BRIEFING")
                InfoPanel(caseDefinition.briefing)
                Text("Kamu tidak harus membuka semua aplikasi. Mulailah dari tujuan kasus, lalu pilih sumber yang relevan.",
                    color = Color(0xFF91A8AD), fontSize = 11.sp, lineHeight = 17.sp)
                PrimaryAction("PAHAMI BRIEFING", onAcknowledgeBriefing)
            }
            CasePhase.INVESTIGATION -> {
                SectionLabel("02  /  INVESTIGATION")
                InfoPanel("Periksa dua catatan penerimaan RX-14. Perbedaan waktu adalah fakta yang perlu dicatat, bukan bukti otomatis adanya manipulasi.")
                records.forEach { record ->
                    val found = record.id in caseProgress.discoveredEvidenceIds
                    EvidencePanel(
                        record = record,
                        discovered = found,
                        analyzed = record.id in caseProgress.analyzedEvidenceIds,
                        sourceContent = if (found) null else onReadSource(record.id),
                        actionLabel = if (found) "SUDAH DIPERIKSA" else "BUKA SUMBER ASLI",
                        actionEnabled = !found,
                        onAction = { onDiscoverEvidence(record.id) }
                    )
                }
                Text(caseProgress.discoveredEvidenceIds.intersect(caseDefinition.requiredEvidenceIds).size.toString() +
                    " dari " + caseDefinition.requiredEvidenceIds.size + " sumber wajib telah diperiksa.",
                    color = Color(0xFF91A8AD), fontSize = 10.sp)
            }
            CasePhase.ANALYSIS -> {
                SectionLabel("03  /  INTELLIGENCE ANALYSIS")
                InfoPanel("Kedua sumber wajib telah dibuka. Gunakan ruang Analisis untuk mencatat hasil pemeriksaan tiap sumber dan memilih kesimpulan yang benar-benar didukung bukti.")
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
                            Text(if (analyzed) "ANALISIS TERCATAT" else "MENUNGGU HASIL ANALISIS",
                                color = if (analyzed) Color(0xFF8CD5AC) else Color(0xFF9AB4B8),
                                fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                PrimaryAction("BUKA RUANG ANALISIS", onOpenAnalysis)
            }
            CasePhase.OPERATIONAL_DECISION -> {
                SectionLabel("04  /  OPERATIONAL DECISION")
                InfoPanel(analysisConclusion?.feedback
                    ?: "Dua sumber telah dianalisis. Pilih tindakan yang proporsional dengan bukti yang tersedia.")
                OutlinedTextField(
                    value = decisionRationale,
                    onValueChange = onDecisionRationaleChange,
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    label = { Text("ALASAN KEPUTUSAN") },
                    placeholder = { Text("Jelaskan fakta dan batas bukti yang mendukung tindakanmu.") }
                )
                Text("Alasan ini disimpan bersama keputusan dan ditampilkan saat debrief.",
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
                                Text("PILIH TINDAKAN")
                            }
                        }
                    }
                }
            }
            CasePhase.DEBRIEFING -> {
                SectionLabel("05  /  DEBRIEFING")
                InfoPanel("Keputusanmu telah dicatat. Tinjau kembali apa yang didukung sumber dan apa yang masih belum diketahui.")
                analysisConclusion?.let {
                    Text("KESIMPULAN ANALISIS", color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(it.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    InfoPanel(it.feedback)
                }
                decision?.let {
                    Text("KEPUTUSAN TERCATAT", color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(it.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(it.description, color = Color(0xFFB7C6CB), fontSize = 11.sp, lineHeight = 17.sp)
                }
                Text("ALASAN KEPUTUSAN", color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                InfoPanel(caseProgress.decisionRationale.ifBlank { "Tidak ada alasan yang tersimpan pada save lama." })
                InfoPanel("Fakta: dua catatan menyebut RX-14 dan waktu penerimaannya berselisih empat detik. Penyebabnya belum dapat dipastikan. Tidak ada dasar untuk menunjuk pelaku atau menyatakan manipulasi.")
                PrimaryAction("SELESAIKAN DEBRIEF", onAcknowledgeDebrief)
            }
            CasePhase.COMPLETE -> {
                SectionLabel("CASE STATUS  /  COMPLETE")
                InfoPanel("First Contact selesai. Laporan mempertahankan perbedaan antara fakta, dugaan, dan hal yang belum diketahui. Anomali RX-14 tetap terbuka untuk pemeriksaan jika bukti baru tersedia.")
                Text("KESIMPULAN ANALISIS", color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(analysisConclusion?.title ?: "Tidak ada kesimpulan tersimpan", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("KEPUTUSAN AKHIR", color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(decision?.title ?: "Tidak ada keputusan tersimpan", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text("ALASAN KEPUTUSAN", color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                InfoPanel(caseProgress.decisionRationale.ifBlank { "Tidak ada alasan yang tersimpan pada save lama." })
                if (caseDefinition.id in gameState.completedMissionIds) {
                    Text(
                        "REWARD KASUS  /  +${com.zeroday.cyberprotocol.data.Campaign01CaseCatalog.RX14_COMPLETION_REWARD_XP} XP",
                        color = Color(0xFF8CD5AC), fontSize = 11.sp, fontWeight = FontWeight.Bold
                    )
                }
                Text("TOTAL PENGALAMAN  /  ${gameState.experience} XP",
                    color = Color(0xFFB8C9CC), fontSize = 11.sp)
            }
        }

        Text("Pusat Operasi mengelola kasus dan keputusan. Aplikasi lain adalah alat investigasi; membuka aplikasi bukan syarat progres dengan sendirinya.",
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
    onAction: () -> Unit
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
                }) { Text("CATAT SEBAGAI DIPERIKSA") }
            },
            dismissButton = { TextButton(onClick = { sourceOpened = false }) { Text("TUTUP") } }
        )
    }
    Surface(color = Color(0xFF111F27), shape = RoundedCornerShape(7.dp),
        border = BorderStroke(1.dp, Color(0xFF31545B)), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(record.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(record.provenance.sourceLabel, color = Color(0xFF83C7D0), fontSize = 10.sp)
            if (discovered) {
                Text(record.summary, color = Color(0xFFC5D3D0), fontSize = 11.sp, lineHeight = 17.sp)
                Text("STATUS BUKTI: " + record.status.name, color = Color(0xFF91A8AD), fontSize = 9.sp)
            } else {
                Text("Catatan sumber tersedia. Periksa untuk membuka isi dan mencatatnya sebagai bukti kasus.",
                    color = Color(0xFF91A8AD), fontSize = 10.sp, lineHeight = 15.sp)
            }
            if (analyzed) Text("ANALISIS TERCATAT", color = Color(0xFF8CD5AC), fontSize = 9.sp, fontWeight = FontWeight.Bold)
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

private fun phaseLabel(phase: CasePhase): String = when (phase) {
    CasePhase.BRIEFING -> "BRIEFING"
    CasePhase.INVESTIGATION -> "INVESTIGATION"
    CasePhase.ANALYSIS -> "ANALYSIS"
    CasePhase.OPERATIONAL_DECISION -> "DECISION"
    CasePhase.DEBRIEFING -> "DEBRIEFING"
    CasePhase.COMPLETE -> "COMPLETE"
}
