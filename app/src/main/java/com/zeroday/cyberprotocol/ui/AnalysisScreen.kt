package com.zeroday.cyberprotocol.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroday.cyberprotocol.core.model.CaseDefinition
import com.zeroday.cyberprotocol.core.model.CasePhase
import com.zeroday.cyberprotocol.core.model.CasePhaseEvaluator
import com.zeroday.cyberprotocol.core.model.CaseProgress
import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.core.model.GameLanguage
import com.zeroday.cyberprotocol.domain.usecase.EvidenceReviewPolicy

/** Pure evidence filtering shared by the analysis surface and its tests. */
internal object EvidenceAnalysisFilter {
    fun filter(
        records: List<EvidenceRecord>,
        status: EvidenceStatus?,
        query: String,
        concealedRecordIds: Set<String> = emptySet()
    ): List<EvidenceRecord> {
        val normalizedQuery = query.trim().lowercase()
        return records.asSequence()
            .filter { status == null || it.status == status }
            .filter { record ->
                normalizedQuery.isEmpty() ||
                    record.title.lowercase().contains(normalizedQuery) ||
                    (record.id !in concealedRecordIds && record.summary.lowercase().contains(normalizedQuery)) ||
                    record.source.lowercase().contains(normalizedQuery) ||
                    record.missionId.lowercase().contains(normalizedQuery)
            }
            .sortedByDescending { it.recordedAtEpochMillis }
            .toList()
    }
}

@Composable
internal fun AnalysisScreen(
    gameState: GameState,
    caseDefinition: CaseDefinition,
    caseProgress: CaseProgress,
    selectedLanguage: GameLanguage,
    onBack: () -> Unit,
    onAnalyzeCaseEvidence: (String) -> Unit,
    onSelectAnalysisConclusion: (String) -> Unit,
    onReviewEvidence: (String, EvidenceStatus, String) -> Unit
) {
    var selectedStatus by rememberSaveable { mutableStateOf("ALL") }
    var query by rememberSaveable { mutableStateOf("") }
    var reviewTarget by remember { mutableStateOf<EvidenceRecord?>(null) }
    var reviewStatus by remember { mutableStateOf(EvidenceStatus.UNVERIFIED) }
    var reviewReason by remember { mutableStateOf("") }
    val status = when (selectedStatus) {
        "UNVERIFIED" -> EvidenceStatus.UNVERIFIED
        "INFERRED" -> EvidenceStatus.INFERRED
        "CORROBORATED" -> EvidenceStatus.CORROBORATED
        "CONFIRMED" -> EvidenceStatus.CONFIRMED
        "DISPUTED" -> EvidenceStatus.DISPUTED
        "RETRACTED" -> EvidenceStatus.RETRACTED
        else -> null
    }
    val concealedCaseEvidenceIds = caseDefinition.requiredEvidenceIds - caseProgress.discoveredEvidenceIds
    val filtered = EvidenceAnalysisFilter.filter(
        gameState.evidence, status, query, concealedCaseEvidenceIds
    )
    val casePhase = CasePhaseEvaluator.phase(caseDefinition, caseProgress)
    val caseConclusion = caseDefinition.analysisOptions.firstOrNull { it.id == caseProgress.analysisConclusionId }
    val caseRecords = caseDefinition.requiredEvidenceIds.mapNotNull { id ->
        gameState.evidence.firstOrNull { it.id == id }
    }

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0A1D25), Color(0xFF090D13))))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("A.E.G.I.S.  /  INTELLIGENCE", color = Color(0xFF83C7D0),
                    fontSize = 9.sp, letterSpacing = 1.5.sp, fontWeight = FontWeight.Bold)
                Text(localizedUiText(selectedLanguage, "EVIDENCE ANALYSIS", "ANALISIS BUKTI"), color = Color.White, fontSize = 24.sp,
                    fontWeight = FontWeight.Black)
            }
            TextButton(onClick = onBack) { Text(localizedUiText(selectedLanguage, "BACK", "KEMBALI"), color = Color(0xFFBCE8EA)) }
        }

        CaseAnalysisWorkspace(
            definition = caseDefinition,
            progress = caseProgress,
            phase = casePhase,
            selectedConclusion = caseConclusion,
            records = caseRecords,
            onAnalyzeEvidence = onAnalyzeCaseEvidence,
            onSelectConclusion = onSelectAnalysisConclusion
        )

        OutlinedTextField(
            value = query,
            onValueChange = { query = it.take(120) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(localizedUiText(selectedLanguage, "Search title, source, or mission", "Cari judul, sumber, atau misi")) },
            placeholder = { Text(localizedUiText(selectedLanguage, "Example: RX-14", "Contoh: RX-14")) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color(0xFFD0D8DE),
                focusedBorderColor = Color(0xFF83C7D0),
                unfocusedBorderColor = Color(0xFF3B5158),
                focusedLabelColor = Color(0xFF83C7D0),
                unfocusedLabelColor = Color(0xFF8AA8AE),
                cursorColor = Color(0xFF83C7D0)
            )
        )

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("ALL", "CONFIRMED", "UNVERIFIED").forEach { key ->
                FilterChip(
                    selected = selectedStatus == key,
                    onClick = { selectedStatus = key },
                    label = { Text(when (key) {
                        "ALL" -> localizedUiText(selectedLanguage, "ALL", "SEMUA")
                        "CONFIRMED" -> localizedUiText(selectedLanguage, "CONFIRMED", "TERKONFIRMASI")
                        else -> localizedUiText(selectedLanguage, "UNVERIFIED", "BELUM TERVERIFIKASI")
                    }, fontSize = 9.sp) }
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("INFERRED", "CORROBORATED", "DISPUTED", "RETRACTED").forEach { key ->
                FilterChip(
                    selected = selectedStatus == key,
                    onClick = { selectedStatus = key },
                    label = { Text(when (key) {
                        "INFERRED" -> localizedUiText(selectedLanguage, "INFERRED", "DISIMPULKAN")
                        "CORROBORATED" -> localizedUiText(selectedLanguage, "CORROBORATED", "KOROBORASI")
                        "RETRACTED" -> localizedUiText(selectedLanguage, "RETRACTED", "DICABUT")
                        else -> localizedUiText(selectedLanguage, "DISPUTED", "DIPERSELISIHKAN")
                    }, fontSize = 9.sp) }
                )
            }
        }

        Surface(
            color = Color(0xFF102A32),
            shape = RoundedCornerShape(7.dp),
            border = BorderStroke(1.dp, Color(0xFF31545B)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(localizedUiText(selectedLanguage, "EVIDENCE RECORDS", "REKAMAN BUKTI"), color = Color(0xFF83C7D0), fontSize = 9.sp,
                        letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
                    Text("${filtered.size} DARI ${gameState.evidence.size} REKAMAN",
                        color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Text("AUDITED REVIEW", color = Color(0xFF8AA8AE), fontSize = 9.sp,
                    fontWeight = FontWeight.Bold)
            }
        }

        if (filtered.isEmpty()) {
            Surface(
                color = Color(0xFF111B22),
                shape = RoundedCornerShape(7.dp),
                border = BorderStroke(1.dp, Color(0xFF263940)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (gameState.evidence.isEmpty())
                        "Belum ada bukti. Selesaikan dan verifikasi misi untuk mencatat temuan beserta sumbernya."
                    else "Tidak ada rekaman yang cocok dengan pencarian dan filter ini.",
                    Modifier.padding(16.dp), color = Color(0xFF9AB4B8),
                    fontSize = 12.sp, lineHeight = 18.sp
                )
            }
        } else {
            filtered.forEach { record ->
                EvidenceRecordCard(
                    record = record,
                    concealed = record.id in concealedCaseEvidenceIds,
                    onReview = {
                        val targets = EvidenceReviewPolicy.allowedTransitions(record.status)
                        if (targets.isNotEmpty()) {
                            reviewTarget = record
                            reviewStatus = targets.first()
                            reviewReason = ""
                        }
                    }
                )
            }
        }

        Text(
            "Setiap perubahan klasifikasi memerlukan alasan dan tercatat bersama identitas peninjau dalam riwayat audit.",
            color = Color(0xFF71858B), fontSize = 10.sp, lineHeight = 16.sp
        )
        reviewTarget?.let { target ->
            val targets = EvidenceReviewPolicy.allowedTransitions(target.status).toList()
            AlertDialog(
                onDismissRequest = { reviewTarget = null },
                title = { Text(localizedUiText(selectedLanguage, "REVIEW CLASSIFICATION", "TINJAU KLASIFIKASI")) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(target.title, fontWeight = FontWeight.Bold)
                        targets.forEach { candidate ->
                            FilterChip(
                                selected = reviewStatus == candidate,
                                onClick = { reviewStatus = candidate },
                                label = { Text(candidate.name.replace('_', ' ')) }
                            )
                        }
                        OutlinedTextField(
                            value = reviewReason,
                            onValueChange = { reviewReason = it.take(400) },
                            label = { Text(localizedUiText(selectedLanguage, "Review rationale", "Alasan peninjauan")) },
                            placeholder = { Text(localizedUiText(selectedLanguage, "Explain the source or finding that supports this change", "Jelaskan sumber atau temuan yang mendasari perubahan")) },
                            minLines = 2
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        enabled = reviewReason.isNotBlank() && reviewStatus in targets,
                        onClick = {
                            onReviewEvidence(target.id, reviewStatus, reviewReason.trim())
                            reviewTarget = null
                        }
                    ) { Text(localizedUiText(selectedLanguage, "SAVE REVIEW", "SIMPAN REVIEW")) }
                },
                dismissButton = {
                    TextButton(onClick = { reviewTarget = null }) { Text(localizedUiText(selectedLanguage, "CANCEL", "BATAL")) }
                }
            )
        }
    }
}

@Composable
private fun EvidenceRecordCard(record: EvidenceRecord, concealed: Boolean, onReview: () -> Unit) {
    val (label, tint) = when (record.status) {
        EvidenceStatus.CONFIRMED -> localizedUiText(selectedLanguage, "CONFIRMED", "TERKONFIRMASI") to Color(0xFF8CD5AC)
        EvidenceStatus.CORROBORATED -> "DIKOROBORASI" to Color(0xFF9BC7E8)
        EvidenceStatus.INFERRED -> localizedUiText(selectedLanguage, "INFERRED", "DISIMPULKAN") to Color(0xFFE3C17B)
        EvidenceStatus.DISPUTED -> localizedUiText(selectedLanguage, "DISPUTED", "DIPERSELISIHKAN") to Color(0xFFE09AA6)
        EvidenceStatus.RETRACTED -> localizedUiText(selectedLanguage, "RETRACTED", "DICABUT") to Color(0xFF9A879D)
        EvidenceStatus.UNVERIFIED -> localizedUiText(selectedLanguage, "UNVERIFIED", "BELUM TERVERIFIKASI") to Color(0xFF83C7D0)
    }
    Surface(
        color = Color(0xFF111B22),
        shape = RoundedCornerShape(7.dp),
        border = BorderStroke(1.dp, Color(0xFF2B4149)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(label, color = tint, fontSize = 8.sp, fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp, modifier = Modifier.weight(1f))
                Text(record.id, color = Color(0xFF71858B), fontSize = 8.sp)
            }
            Text(record.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(
                if (concealed) "ISI SUMBER TERSEMBUNYI — periksa sumber asli melalui Operasi atau Browser."
                else record.summary,
                color = Color(0xFFC2D0D4), fontSize = 11.sp, lineHeight = 17.sp
            )
            HorizontalDivider(color = Color(0xFF293A40))
            Text("SUMBER  /  ${record.source}", color = Color(0xFF83C7D0), fontSize = 9.sp,
                lineHeight = 14.sp)
            Text("MISI  /  ${record.missionId}", color = Color(0xFF8AA8AE), fontSize = 9.sp)
            Text("ASAL  /  ${record.provenance.origin.name}", color = Color(0xFF8AA8AE), fontSize = 9.sp)
            Text("RELASI  /  ${record.relations.size}  ·  RIWAYAT REVIEW  /  ${record.reviewHistory.size}",
                color = Color(0xFF8AA8AE), fontSize = 9.sp)
            if (EvidenceReviewPolicy.allowedTransitions(record.status).isNotEmpty()) {
                TextButton(onClick = onReview) { Text(localizedUiText(selectedLanguage, "REVIEW EVIDENCE", "TINJAU BUKTI")) }
            } else {
                Text(localizedUiText(selectedLanguage, "FINAL STATUS — CANNOT BE REACTIVATED", "STATUS FINAL — TIDAK DAPAT DIAKTIFKAN KEMBALI"),
                    color = Color(0xFF9A879D), fontSize = 8.sp)
            }
        }
    }
}


@Composable
private fun CaseAnalysisWorkspace(
    definition: CaseDefinition,
    progress: CaseProgress,
    phase: CasePhase,
    selectedConclusion: com.zeroday.cyberprotocol.core.model.CaseAnalysisOption?,
    records: List<EvidenceRecord>,
    onAnalyzeEvidence: (String) -> Unit,
    onSelectConclusion: (String) -> Unit
) {
    Surface(
        color = Color(0xFF10232B),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFF31545B)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Text("CASE WORKSPACE  /  RX-14", color = Color(0xFF83C7D0),
                fontSize = 9.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
            Text(definition.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("FASE  /  " + phase.name.replace('_', ' '), color = Color(0xFF9AB4B8),
                fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text("Analisis hanya dapat dilakukan pada sumber yang sudah diperiksa. Pilih kesimpulan setelah semua sumber wajib dianalisis.",
                color = Color(0xFFB7C6CB), fontSize = 10.sp, lineHeight = 15.sp)
            records.forEach { record ->
                val discovered = record.id in progress.discoveredEvidenceIds
                val analyzed = record.id in progress.analyzedEvidenceIds
                Surface(color = Color(0xFF111B22), shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFF263F48)), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(11.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(record.title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(record.provenance.sourceLabel + "  /  " + record.provenance.origin.name,
                            color = Color(0xFF83C7D0), fontSize = 8.sp)
                        Text(
                            if (discovered) record.summary else "Isi sumber terkunci sampai catatan asli diperiksa.",
                            color = Color(0xFFB7C6CB), fontSize = 10.sp, lineHeight = 15.sp
                        )
                        Text(when {
                            analyzed -> "ANALISIS TERCATAT"
                            discovered -> "SIAP DIANALISIS"
                            else -> "SUMBER BELUM DIPERIKSA — gunakan Messenger, Browser, atau Operations"
                        }, color = if (analyzed) Color(0xFF8CD5AC) else Color(0xFF9AB4B8),
                            fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        OutlinedButton(
                            onClick = { onAnalyzeEvidence(record.id) },
                            enabled = phase == CasePhase.ANALYSIS && discovered && !analyzed,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFBCE8EA))
                        ) { Text(if (analyzed) "SUDAH DIANALISIS" else "CATAT HASIL ANALISIS") }
                    }
                }
            }
            val allAnalyzed = definition.requiredEvidenceIds.all { it in progress.analyzedEvidenceIds }
            if (allAnalyzed && definition.analysisOptions.isNotEmpty()) {
                Text(localizedUiText(selectedLanguage, "ANALYSIS CONCLUSION", "KESIMPULAN ANALISIS"), color = Color(0xFF83C7D0), fontSize = 9.sp,
                    fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                definition.analysisOptions.forEach { option ->
                    val selected = progress.analysisConclusionId == option.id
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(option.title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(option.description, color = Color(0xFFB7C6CB), fontSize = 10.sp, lineHeight = 15.sp)
                        OutlinedButton(
                            onClick = { onSelectConclusion(option.id) },
                            enabled = phase == CasePhase.ANALYSIS &&
                                CasePhaseEvaluator.canSelectAnalysisConclusion(definition, progress, option.id),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFBCE8EA))
                        ) {
                            Text(when {
                                !selected -> localizedUiText(selectedLanguage, "TEST CONCLUSION", "UJI KESIMPULAN")
                                option.supportedByEvidence -> localizedUiText(selectedLanguage, "SUPPORTED CONCLUSION", "KESIMPULAN DIDUKUNG")
                                else -> localizedUiText(selectedLanguage, "SELECTED — NOT SUPPORTED", "DIPILIH — BELUM DIDUKUNG")
                            })
                        }
                    }
                }
                selectedConclusion?.let {
                    Text(it.feedback, color = if (it.supportedByEvidence) Color(0xFF8CD5AC) else Color(0xFFE3C17B),
                        fontSize = 10.sp, lineHeight = 16.sp)
                }
            }
        }
    }
}
