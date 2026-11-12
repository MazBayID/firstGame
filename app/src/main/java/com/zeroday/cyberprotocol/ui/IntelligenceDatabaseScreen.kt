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
import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.data.Campaign01Catalog

@Composable
internal fun IntelligenceDatabaseScreen(
    gameState: GameState,
    caseDefinition: CaseDefinition,
    caseProgress: CaseProgress,
    onBack: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf("MISSIONS") }
    var query by rememberSaveable { mutableStateOf("") }
    var selectedMissionId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedEvidenceId by rememberSaveable { mutableStateOf<String?>(null) }

    val missions = Campaign01Catalog.missions.filter {
        query.isBlank() || it.title.contains(query, true) ||
            it.id.contains(query, true) || it.briefing.contains(query, true) ||
            it.objectives.any { objective -> objective.title.contains(query, true) ||
                objective.description.contains(query, true) }
    }
    val evidence = gameState.evidence.filter {
        query.isBlank() || it.title.contains(query, true) ||
            it.summary.contains(query, true) || it.source.contains(query, true) ||
            it.missionId.contains(query, true)
    }.sortedByDescending { it.recordedAtEpochMillis }
    val selectedMission = selectedMissionId?.let(Campaign01Catalog::find)
    val selectedEvidence = evidence.firstOrNull { it.id == selectedEvidenceId }
    val casePhase = CasePhaseEvaluator.phase(caseDefinition, caseProgress)
    val caseRecords = caseDefinition.requiredEvidenceIds.mapNotNull { id ->
        gameState.evidence.firstOrNull { it.id == id }
    }
    val analysisConclusion = caseDefinition.analysisOptions.firstOrNull { it.id == caseProgress.analysisConclusionId }
    val decision = caseDefinition.decisionOptions.firstOrNull { it.id == caseProgress.selectedDecisionId }

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0A1D25), Color(0xFF090D13))))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("A.E.G.I.S. / INTELLIGENCE INDEX", color = Color(0xFF83C7D0),
                    fontSize = 9.sp, letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
                Text("DATABASE INTELIJEN", color = Color.White, fontSize = 23.sp,
                    fontWeight = FontWeight.Black)
            }
            TextButton(onClick = onBack) { Text("KEMBALI", color = Color(0xFFBCE8EA)) }
        }

        OutlinedTextField(
            value = query, onValueChange = { query = it.take(100) },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            label = { Text("Cari indeks, sumber, atau bukti") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color(0xFFD0D8DE),
                focusedBorderColor = Color(0xFF83C7D0), unfocusedBorderColor = Color(0xFF3B5158),
                focusedLabelColor = Color(0xFF83C7D0), unfocusedLabelColor = Color(0xFF8AA8AE),
                cursorColor = Color(0xFF83C7D0)
            )
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = selectedTab == "MISSIONS", onClick = {
                selectedTab = "MISSIONS"; selectedEvidenceId = null
            }, label = { Text("INDEKS MISI") })
            FilterChip(selected = selectedTab == "EVIDENCE", onClick = {
                selectedTab = "EVIDENCE"; selectedMissionId = null
            }, label = { Text("BUKTI TERSIMPAN") })
            FilterChip(selected = selectedTab == "CASE", onClick = {
                selectedTab = "CASE"; selectedMissionId = null; selectedEvidenceId = null
            }, label = { Text("KASUS RX-14") })
        }

        Surface(color = Color(0xFF102A32), shape = RoundedCornerShape(7.dp),
            border = BorderStroke(1.dp, Color(0xFF31545B)), modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(if (selectedTab == "MISSIONS") "DOKUMEN MISI" else "REKAMAN BUKTI",
                        color = Color(0xFF83C7D0), fontSize = 9.sp, letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold)
                    Text(if (selectedTab == "MISSIONS") "${missions.size} / ${Campaign01Catalog.missions.size} DOKUMEN"
                        else "${evidence.size} REKAMAN",
                        color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Text("LOCAL INDEX", color = Color(0xFF8AA8AE), fontSize = 8.sp)
            }
        }

        if (selectedTab == "MISSIONS") {
            if (missions.isEmpty()) EmptyDatabaseNote("Tidak ada dokumen misi yang cocok.")
            missions.forEach { mission ->
                val completed = mission.id in gameState.completedMissionIds
                val active = mission.id == gameState.activeMissionId
                Surface(onClick = { selectedMissionId = if (selectedMissionId == mission.id) null else mission.id },
                    color = if (selectedMissionId == mission.id) Color(0xFF1A3038) else Color(0xFF111B22),
                    shape = RoundedCornerShape(7.dp),
                    border = BorderStroke(1.dp, if (selectedMissionId == mission.id) Color(0xFF83C7D0) else Color(0xFF263940)),
                    modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(mission.title, Modifier.weight(1f), color = Color.White,
                                fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(when { completed -> "TERVERIFIKASI"; active -> "AKTIF"; else -> "TERARSIP" },
                                color = when { completed -> Color(0xFF8CD5AC); active -> Color(0xFFE0A2AD); else -> Color(0xFF8AA8AE) },
                                fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(mission.id, color = Color(0xFF8AA8AE), fontSize = 8.sp)
                    }
                }
            }
            selectedMission?.let { mission ->
                DatabaseDetailPanel("MISSION FILE", mission.title) {
                    Text(mission.briefing, color = Color(0xFFC2D0D4), fontSize = 11.sp, lineHeight = 17.sp)
                    Text("OBJECTIVES", color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    mission.objectives.forEachIndexed { index, objective ->
                        Text("${index + 1}. ${objective.title}", color = Color.White, fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold)
                        Text(objective.description, color = Color(0xFF9AB4B8), fontSize = 10.sp)
                    }
                    Text("REWARD ${mission.rewardXp} XP  ·  CLEARANCE ${mission.requiredClearance}",
                        color = Color(0xFF8AA8AE), fontSize = 9.sp)
                    Text("WORKSPACE  /  ${mission.workspaceDirectory}",
                        color = Color(0xFF83C7D0), fontSize = 8.sp)
                }
            }
        } else if (selectedTab == "EVIDENCE") {
            if (evidence.isEmpty()) EmptyDatabaseNote(
                if (gameState.evidence.isEmpty()) "Belum ada bukti. Temuan yang lolos verifikasi misi akan masuk ke indeks ini."
                else "Tidak ada bukti yang cocok dengan pencarian."
            )
            evidence.forEach { record ->
                val color = evidenceColor(record.status)
                Surface(onClick = { selectedEvidenceId = if (selectedEvidenceId == record.id) null else record.id },
                    color = if (selectedEvidenceId == record.id) Color(0xFF1A3038) else Color(0xFF111B22),
                    shape = RoundedCornerShape(7.dp),
                    border = BorderStroke(1.dp, if (selectedEvidenceId == record.id) color else Color(0xFF263940)),
                    modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(record.title, Modifier.weight(1f), color = Color.White,
                                fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(evidenceLabel(record.status), color = color, fontSize = 8.sp,
                                fontWeight = FontWeight.Bold)
                        }
                        Text(record.id, color = Color(0xFF8AA8AE), fontSize = 8.sp)
                        Text(record.summary, color = Color(0xFF9AB4B8), fontSize = 10.sp, maxLines = 2)
                    }
                }
            }
            selectedEvidence?.let { record ->
                DatabaseDetailPanel("EVIDENCE RECORD", record.title) {
                    Text(record.summary, color = Color(0xFFC2D0D4), fontSize = 11.sp, lineHeight = 17.sp)
                    DatabaseValue("STATUS", evidenceLabel(record.status))
                    DatabaseValue("SOURCE", record.source)
                    DatabaseValue("PROVENANCE", record.provenance.origin.name + " / " + record.provenance.sourceLabel)
                    record.provenance.sourceAssetId?.let { DatabaseValue("SHARED ASSET", it) }
                    record.provenance.sourcePath?.let { DatabaseValue("SOURCE PATH", it) }
                    DatabaseValue("RELATIONSHIPS", record.relations.joinToString { "${it.type}: ${it.relatedEvidenceId}" }.ifBlank { "No linked evidence" })
                    DatabaseValue("REVIEW HISTORY", record.reviewHistory.joinToString { "${it.previousStatus} → ${it.newStatus} / ${it.reviewerId}: ${it.reason}" }.ifBlank { "No reviews recorded" })
                    DatabaseValue("MISSION", record.missionId)
                    DatabaseValue("RECORD ID", record.id)
                    DatabaseValue("TIMESTAMP", record.recordedAtEpochMillis.toString())
                    val related = Campaign01Catalog.find(record.missionId)
                    if (related != null) {
                        Text("DOKUMEN TERKAIT  /  ${related.title}",
                            color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(related.briefing, color = Color(0xFF9AB4B8), fontSize = 10.sp,
                            lineHeight = 16.sp)
                    }
                }
            }
        } else {
            DatabaseDetailPanel("ACTIVE CASE FILE", caseDefinition.title) {
                DatabaseValue("CASE ID", caseDefinition.id)
                DatabaseValue("PHASE", casePhase.name.replace('_', ' '))
                DatabaseValue("BRIEFING", caseDefinition.briefing)
                DatabaseValue("REQUIRED SOURCES", caseRecords.size.toString())
                DatabaseValue("DISCOVERED", "${caseProgress.discoveredEvidenceIds.intersect(caseDefinition.requiredEvidenceIds).size} / ${caseDefinition.requiredEvidenceIds.size}")
                DatabaseValue("ANALYZED", "${caseProgress.analyzedEvidenceIds.intersect(caseDefinition.requiredEvidenceIds).size} / ${caseDefinition.requiredEvidenceIds.size}")
                DatabaseValue("ANALYTICAL CONCLUSION", analysisConclusion?.title ?: "Belum dipilih")
                DatabaseValue("CONCLUSION FEEDBACK", analysisConclusion?.feedback ?: "Belum ada kesimpulan tersimpan.")
                DatabaseValue("OPERATIONAL DECISION", decision?.title ?: "Belum dipilih")
                DatabaseValue("DECISION RATIONALE", caseProgress.decisionRationale.ifBlank { "Belum ada alasan tersimpan." })
                DatabaseValue("DEBRIEF", if (caseProgress.debriefViewed) "SELESAI" else "BELUM SELESAI")
                caseRecords.forEach { record ->
                    HorizontalDivider(color = Color(0xFF293A40))
                    Text(record.title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    DatabaseValue("SOURCE", record.provenance.sourceLabel + " / " + record.provenance.origin.name)
                    DatabaseValue("STATUS", if (record.id in caseProgress.discoveredEvidenceIds) "DIPERIKSA" else "BELUM DIPERIKSA")
                    DatabaseValue("RELATIONSHIPS", record.relations.joinToString { "${it.type}: ${it.relatedEvidenceId}" }.ifBlank { "No linked evidence" })
                    record.provenance.sourcePath?.let { DatabaseValue("SOURCE PATH", it) }
                }
            }
        }
        Text("Indeks hanya menampilkan data lokal yang tersedia. Tidak ada catatan fiktif yang ditambahkan sebagai bukti hasil investigasi.",
            color = Color(0xFF71858B), fontSize = 9.sp, lineHeight = 15.sp)
    }
}

@Composable
private fun DatabaseDetailPanel(title: String, heading: String, content: @Composable ColumnScope.() -> Unit) {
    Surface(color = Color(0xFF10232B), shape = RoundedCornerShape(7.dp),
        border = BorderStroke(1.dp, Color(0xFF31545B)), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, color = Color(0xFF83C7D0), fontSize = 8.sp, letterSpacing = 1.2.sp,
                fontWeight = FontWeight.Bold)
            Text(heading, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            content()
        }
    }
}

@Composable
private fun DatabaseValue(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, color = Color(0xFF83C7D0), fontSize = 8.sp, fontWeight = FontWeight.Bold)
        Text(value, color = Color(0xFFC2D0D4), fontSize = 10.sp, lineHeight = 15.sp)
    }
}

@Composable
private fun EmptyDatabaseNote(message: String) {
    Surface(color = Color(0xFF111B22), shape = RoundedCornerShape(7.dp),
        border = BorderStroke(1.dp, Color(0xFF263940)), modifier = Modifier.fillMaxWidth()) {
        Text(message, Modifier.padding(14.dp), color = Color(0xFF9AB4B8), fontSize = 10.sp,
            lineHeight = 16.sp)
    }
}

private fun evidenceLabel(status: EvidenceStatus): String = when (status) {
    EvidenceStatus.CONFIRMED -> "TERKONFIRMASI"
    EvidenceStatus.CORROBORATED -> "DIKOROBORASI"
    EvidenceStatus.INFERRED -> "DISIMPULKAN"
    EvidenceStatus.DISPUTED -> "DIPERSELISIHKAN"
    EvidenceStatus.RETRACTED -> "DICABUT"
    EvidenceStatus.UNVERIFIED -> "BELUM TERVERIFIKASI"
}

private fun evidenceColor(status: EvidenceStatus): Color = when (status) {
    EvidenceStatus.CONFIRMED -> Color(0xFF8CD5AC)
    EvidenceStatus.CORROBORATED -> Color(0xFF9BC7E8)
    EvidenceStatus.INFERRED -> Color(0xFFE3C17B)
    EvidenceStatus.DISPUTED -> Color(0xFFE09AA6)
    EvidenceStatus.RETRACTED -> Color(0xFF9A879D)
    EvidenceStatus.UNVERIFIED -> Color(0xFF83C7D0)
}
