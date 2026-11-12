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
import com.zeroday.cyberprotocol.core.model.CasePhaseEvaluator
import com.zeroday.cyberprotocol.core.model.CaseProgress
import com.zeroday.cyberprotocol.core.model.GameLanguage
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.data.Campaign01Catalog

internal object ArchiveQueryPolicy {
    fun completedMissions(
        completedMissionIds: Set<String>,
        query: String,
        language: GameLanguage
    ) = Campaign01Catalog.missions.filter { mission ->
        mission.id in completedMissionIds &&
            (query.isBlank() ||
                CaseLocalizedContent.missionTitle(mission, language).contains(query, true) ||
                mission.id.contains(query, true) ||
                CaseLocalizedContent.missionBriefing(mission, language).contains(query, true))
    }

    fun evidence(
        records: List<com.zeroday.cyberprotocol.core.model.EvidenceRecord>,
        query: String,
        language: GameLanguage
    ) = records.filter { record ->
        query.isBlank() ||
            CaseLocalizedContent.evidenceTitle(record, language).contains(query, true) ||
            CaseLocalizedContent.evidenceSummary(record, language).contains(query, true) ||
            CaseLocalizedContent.evidenceSource(record, language).contains(query, true) ||
            record.id.contains(query, true) || record.missionId.contains(query, true)
    }.sortedByDescending { it.recordedAtEpochMillis }
}

@Composable
internal fun ArchivesScreen(
    gameState: GameState,
    selectedLanguage: GameLanguage,
    caseDefinition: CaseDefinition,
    caseProgress: CaseProgress,
    onBack: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf("MISSIONS") }
    var query by rememberSaveable { mutableStateOf("") }
    var selectedMissionId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedEvidenceId by rememberSaveable { mutableStateOf<String?>(null) }

    val completedMissions = ArchiveQueryPolicy.completedMissions(
        gameState.completedMissionIds, query, selectedLanguage
    )
    val archivedEvidence = ArchiveQueryPolicy.evidence(gameState.evidence, query, selectedLanguage)
    val selectedMission = selectedMissionId?.let(Campaign01Catalog::find)
        ?.takeIf { it.id in gameState.completedMissionIds }
    val selectedEvidence = archivedEvidence.firstOrNull { it.id == selectedEvidenceId }
    val phase = CasePhaseEvaluator.phase(caseDefinition, caseProgress)
    val analysis = caseDefinition.analysisOptions.firstOrNull { it.id == caseProgress.analysisConclusionId }
    val decision = caseDefinition.decisionOptions.firstOrNull { it.id == caseProgress.selectedDecisionId }

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0A1D25), Color(0xFF090D13))))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(archiveText(selectedLanguage, "A.E.G.I.S. / RECORDS DIVISION", "A.E.G.I.S. / DIVISI ARSIP"),
                    color = Color(0xFF83C7D0), fontSize = 9.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
                Text(archiveText(selectedLanguage, "OPERATION ARCHIVES", "ARSIP OPERASI"),
                    color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Black)
            }
            TextButton(onClick = onBack) {
                Text(archiveText(selectedLanguage, "BACK", "KEMBALI"), color = Color(0xFFBCE8EA))
            }
        }

        Surface(
            color = Color(0xFF102A32), shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF31545B)), modifier = Modifier.fillMaxWidth()
        ) {
            Row(Modifier.padding(13.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ArchiveMetric(
                    archiveText(selectedLanguage, "MISSIONS", "MISI"),
                    gameState.completedMissionIds.size.toString(),
                    Modifier.weight(1f)
                )
                ArchiveMetric(
                    archiveText(selectedLanguage, "EVIDENCE", "BUKTI"),
                    gameState.evidence.size.toString(),
                    Modifier.weight(1f)
                )
                ArchiveMetric(
                    archiveText(selectedLanguage, "CASE PHASE", "FASE KASUS"),
                    localizedPhaseName(phase, selectedLanguage),
                    Modifier.weight(1f)
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            FilterChip(
                selected = selectedTab == "MISSIONS",
                onClick = { selectedTab = "MISSIONS"; selectedEvidenceId = null },
                label = { Text(archiveText(selectedLanguage, "MISSIONS", "MISI")) }
            )
            FilterChip(
                selected = selectedTab == "EVIDENCE",
                onClick = { selectedTab = "EVIDENCE"; selectedMissionId = null },
                label = { Text(archiveText(selectedLanguage, "EVIDENCE", "BUKTI")) }
            )
            FilterChip(
                selected = selectedTab == "CASE",
                onClick = { selectedTab = "CASE"; selectedMissionId = null; selectedEvidenceId = null },
                label = { Text(archiveText(selectedLanguage, "CASE REPORT", "LAPORAN KASUS")) }
            )
        }

        if (selectedTab != "CASE") {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it.take(100) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = {
                    Text(archiveText(selectedLanguage, "Search archived records", "Cari catatan arsip"))
                },
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
        }

        when (selectedTab) {
            "MISSIONS" -> {
                ArchiveSectionTitle(
                    archiveText(selectedLanguage, "VERIFIED MISSION FILES", "BERKAS MISI TERVERIFIKASI")
                )
                if (completedMissions.isEmpty()) {
                    ArchiveEmptyNote(
                        archiveText(selectedLanguage, "No completed mission files match this search.",
                            "Tidak ada berkas misi selesai yang cocok dengan pencarian.")
                    )
                }
                completedMissions.forEach { mission ->
                    ArchiveRecordCard(
                        title = CaseLocalizedContent.missionTitle(mission, selectedLanguage),
                        metadata = mission.id,
                        status = archiveText(selectedLanguage, "VERIFIED", "TERVERIFIKASI"),
                        summary = CaseLocalizedContent.missionBriefing(mission, selectedLanguage),
                        selected = selectedMissionId == mission.id,
                        onClick = {
                            selectedMissionId = if (selectedMissionId == mission.id) null else mission.id
                        }
                    )
                }
                selectedMission?.let { mission ->
                    ArchiveDetail(
                        title = CaseLocalizedContent.missionTitle(mission, selectedLanguage),
                        subtitle = archiveText(selectedLanguage, "MISSION RECORD", "CATATAN MISI")
                    ) {
                        Text(CaseLocalizedContent.missionBriefing(mission, selectedLanguage),
                            color = Color(0xFFC2D0D4), fontSize = 11.sp, lineHeight = 17.sp)
                        ArchiveValue(archiveText(selectedLanguage, "MISSION ID", "ID MISI"), mission.id)
                        ArchiveValue(archiveText(selectedLanguage, "REWARD", "HADIAH"), "+${mission.rewardXp} XP")
                        ArchiveValue(archiveText(selectedLanguage, "CLEARANCE", "TINGKAT AKSES"), mission.requiredClearance.toString())
                        ArchiveValue(archiveText(selectedLanguage, "WORKSPACE", "RUANG KERJA"), mission.workspaceDirectory)
                        Text(archiveText(selectedLanguage, "OBJECTIVES", "SASARAN"),
                            color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        mission.objectives.forEachIndexed { index, objective ->
                            Text("${index + 1}. ${CaseLocalizedContent.objectiveTitle(mission, objective, index, selectedLanguage)}",
                                color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text(CaseLocalizedContent.objectiveDescription(mission, objective, index, selectedLanguage),
                                color = Color(0xFF9AB4B8), fontSize = 10.sp)
                        }
                    }
                }
            }
            "EVIDENCE" -> {
                ArchiveSectionTitle(
                    archiveText(selectedLanguage, "COLLECTED EVIDENCE", "BUKTI YANG DIKUMPULKAN")
                )
                if (archivedEvidence.isEmpty()) {
                    ArchiveEmptyNote(
                        archiveText(selectedLanguage, "No evidence records match this search.",
                            "Tidak ada catatan bukti yang cocok dengan pencarian.")
                    )
                }
                archivedEvidence.forEach { record ->
                    ArchiveRecordCard(
                        title = CaseLocalizedContent.evidenceTitle(record, selectedLanguage),
                        metadata = record.id,
                        status = localizedEvidenceStatus(record.status, selectedLanguage),
                        summary = CaseLocalizedContent.evidenceSummary(record, selectedLanguage),
                        selected = selectedEvidenceId == record.id,
                        onClick = {
                            selectedEvidenceId = if (selectedEvidenceId == record.id) null else record.id
                        }
                    )
                }
                selectedEvidence?.let { record ->
                    ArchiveDetail(
                        title = CaseLocalizedContent.evidenceTitle(record, selectedLanguage),
                        subtitle = archiveText(selectedLanguage, "EVIDENCE RECORD", "CATATAN BUKTI")
                    ) {
                        Text(CaseLocalizedContent.evidenceSummary(record, selectedLanguage),
                            color = Color(0xFFC2D0D4), fontSize = 11.sp, lineHeight = 17.sp)
                        ArchiveValue(archiveText(selectedLanguage, "STATUS", "STATUS"),
                            localizedEvidenceStatus(record.status, selectedLanguage))
                        ArchiveValue(archiveText(selectedLanguage, "SOURCE", "SUMBER"),
                            CaseLocalizedContent.evidenceSource(record, selectedLanguage))
                        ArchiveValue(archiveText(selectedLanguage, "ORIGIN", "ASAL"),
                            localizedEvidenceOrigin(record.provenance.origin, selectedLanguage))
                        ArchiveValue(archiveText(selectedLanguage, "MISSION", "MISI"), record.missionId)
                        ArchiveValue(archiveText(selectedLanguage, "RECORD ID", "ID CATATAN"), record.id)
                        record.provenance.sourcePath?.let {
                            ArchiveValue(archiveText(selectedLanguage, "SOURCE PATH", "LOKASI SUMBER"), it)
                        }
                        if (record.relations.isNotEmpty()) {
                            Text(archiveText(selectedLanguage, "RELATIONSHIPS", "HUBUNGAN"),
                                color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            record.relations.forEach { relation ->
                                Text(
                                    "${localizedEvidenceRelation(relation.type, selectedLanguage)} / ${relation.relatedEvidenceId}",
                                    color = Color(0xFF9AB4B8), fontSize = 10.sp
                                )
                            }
                        }
                        if (record.reviewHistory.isNotEmpty()) {
                            Text(archiveText(selectedLanguage, "REVIEW HISTORY", "RIWAYAT PENINJAUAN"),
                                color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            record.reviewHistory.forEach { review ->
                                Text(
                                    "${localizedEvidenceStatus(review.previousStatus, selectedLanguage)} → ${localizedEvidenceStatus(review.newStatus, selectedLanguage)}",
                                    color = Color.White, fontSize = 10.sp
                                )
                                Text("${review.reviewerId}: ${review.reason}",
                                    color = Color(0xFF9AB4B8), fontSize = 10.sp)
                            }
                        } else {
                            ArchiveValue(archiveText(selectedLanguage, "REVIEW HISTORY", "RIWAYAT PENINJAUAN"),
                                archiveText(selectedLanguage, "No reviews recorded", "Belum ada riwayat peninjauan"))
                        }
                    }
                }
            }
            else -> {
                ArchiveSectionTitle(
                    archiveText(selectedLanguage, "FIRST CONTACT / RX-14 CASE FILE", "FIRST CONTACT / BERKAS KASUS RX-14")
                )
                ArchiveDetail(
                    title = CaseLocalizedContent.title(caseDefinition, selectedLanguage),
                    subtitle = archiveText(selectedLanguage, "CASE REPORT", "LAPORAN KASUS")
                ) {
                    Text(CaseLocalizedContent.briefing(caseDefinition, selectedLanguage),
                        color = Color(0xFFC2D0D4), fontSize = 11.sp, lineHeight = 17.sp)
                    ArchiveValue(archiveText(selectedLanguage, "CASE ID", "ID KASUS"), caseDefinition.id)
                    ArchiveValue(archiveText(selectedLanguage, "PHASE", "FASE"), localizedPhaseName(phase, selectedLanguage))
                    ArchiveValue(
                        archiveText(selectedLanguage, "SOURCES REVIEWED", "SUMBER DIPERIKSA"),
                        "${caseProgress.discoveredEvidenceIds.intersect(caseDefinition.requiredEvidenceIds).size} / ${caseDefinition.requiredEvidenceIds.size}"
                    )
                    ArchiveValue(
                        archiveText(selectedLanguage, "SOURCES ANALYZED", "SUMBER DIANALISIS"),
                        "${caseProgress.analyzedEvidenceIds.intersect(caseDefinition.requiredEvidenceIds).size} / ${caseDefinition.requiredEvidenceIds.size}"
                    )
                    ArchiveValue(
                        archiveText(selectedLanguage, "ANALYTICAL CONCLUSION", "KESIMPULAN ANALISIS"),
                        analysis?.let { CaseLocalizedContent.analysisTitle(it, selectedLanguage) }
                            ?: archiveText(selectedLanguage, "Not selected", "Belum dipilih")
                    )
                    analysis?.let {
                        Text(CaseLocalizedContent.analysisFeedback(it, selectedLanguage),
                            color = Color(0xFF9AB4B8), fontSize = 10.sp)
                    }
                    ArchiveValue(
                        archiveText(selectedLanguage, "OPERATIONAL DECISION", "KEPUTUSAN OPERASIONAL"),
                        decision?.let { CaseLocalizedContent.decisionTitle(it, selectedLanguage) }
                            ?: archiveText(selectedLanguage, "Not selected", "Belum dipilih")
                    )
                    decision?.let {
                        Text(CaseLocalizedContent.decisionDescription(it, selectedLanguage),
                            color = Color(0xFF9AB4B8), fontSize = 10.sp)
                    }
                    ArchiveValue(
                        archiveText(selectedLanguage, "PLAYER RATIONALE", "ALASAN PEMAIN"),
                        caseProgress.decisionRationale.ifBlank {
                            archiveText(selectedLanguage, "No rationale recorded.", "Belum ada alasan tercatat.")
                        }
                    )
                    ArchiveValue(
                        archiveText(selectedLanguage, "DEBRIEF", "DEBRIEFING"),
                        if (caseProgress.debriefViewed) archiveText(selectedLanguage, "ACKNOWLEDGED", "DITINJAU")
                        else archiveText(selectedLanguage, "NOT YET ACKNOWLEDGED", "BELUM DITINJAU")
                    )
                    ArchiveValue(
                        archiveText(selectedLanguage, "CHAPTER REWARD", "HADIAH CHAPTER"),
                        if (caseDefinition.id in gameState.completedMissionIds) "+100 XP"
                        else archiveText(selectedLanguage, "NOT AWARDED", "BELUM DIBERIKAN")
                    )
                }
            }
        }

        Text(
            archiveText(selectedLanguage,
                "Archive records are read-only. Original evidence, player drafts, and mission identifiers remain unchanged.",
                "Catatan arsip hanya-baca. Bukti asli, draf pemain, dan ID misi tidak diubah."),
            color = Color(0xFF71858B), fontSize = 9.sp, lineHeight = 15.sp
        )
    }
}

@Composable
private fun ArchiveMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(label, color = Color(0xFF8AA8AE), fontSize = 8.sp, fontWeight = FontWeight.Bold)
        Text(value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ArchiveSectionTitle(value: String) {
    Text(value, color = Color(0xFF83C7D0), fontSize = 9.sp, letterSpacing = 1.1.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun ArchiveEmptyNote(value: String) {
    Surface(color = Color(0xFF111B22), shape = RoundedCornerShape(7.dp),
        border = BorderStroke(1.dp, Color(0xFF263940)), modifier = Modifier.fillMaxWidth()) {
        Text(value, Modifier.padding(14.dp), color = Color(0xFF9AB4B8), fontSize = 11.sp)
    }
}

@Composable
private fun ArchiveRecordCard(
    title: String,
    metadata: String,
    status: String,
    summary: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (selected) Color(0xFF1A3038) else Color(0xFF111B22),
        shape = RoundedCornerShape(7.dp),
        border = BorderStroke(1.dp, if (selected) Color(0xFF83C7D0) else Color(0xFF263940)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, Modifier.weight(1f), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(status, color = Color(0xFF8CD5AC), fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
            Text(metadata, color = Color(0xFF8AA8AE), fontSize = 8.sp)
            Text(summary, color = Color(0xFF9AB4B8), fontSize = 10.sp, maxLines = 2)
        }
    }
}

@Composable
private fun ArchiveDetail(title: String, subtitle: String, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        color = Color(0xFF10232B), shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFF31545B)), modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(subtitle, color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            content()
        }
    }
}

@Composable
private fun ArchiveValue(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, color = Color(0xFF8AA8AE), fontSize = 8.sp, fontWeight = FontWeight.Bold)
        Text(value, color = Color(0xFFD0D8DE), fontSize = 10.sp, lineHeight = 15.sp)
    }
}

private fun archiveText(language: GameLanguage, english: String, indonesian: String): String =
    localizedUiText(language, english, indonesian)
