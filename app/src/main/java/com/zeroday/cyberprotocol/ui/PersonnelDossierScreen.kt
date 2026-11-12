package com.zeroday.cyberprotocol.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.core.model.GameLanguage
import com.zeroday.cyberprotocol.data.Campaign01Catalog
import com.zeroday.cyberprotocol.domain.usecase.ProgressionRules

@Composable
internal fun PersonnelDossierScreen(
    gameState: GameState,
    caseDefinition: CaseDefinition,
    caseProgress: CaseProgress,
    selectedLanguage: GameLanguage,
    onBack: () -> Unit
) {
    val completed = Campaign01Catalog.missions.filter { it.id in gameState.completedMissionIds }
    val guidedTraining = Campaign01Catalog.missions.filter { it.id in Campaign01Catalog.guidedTrainingMissionIds }
    val completedGuidedTraining = guidedTraining.count { it.id in gameState.completedMissionIds }
    val completedLegacyMissions = completed.count { it.id in Campaign01Catalog.retiredLegacyStoryMissionIds }
    val nextRank = ProgressionRules.nextRankThreshold(gameState.experience)
    val storyProgress = (gameState.storyBeat - 2).coerceIn(0, 14)
    val casePhase = CasePhaseEvaluator.phase(caseDefinition, caseProgress)
    val analysisConclusion = caseDefinition.analysisOptions.firstOrNull { it.id == caseProgress.analysisConclusionId }
    val caseDecision = caseDefinition.decisionOptions.firstOrNull { it.id == caseProgress.selectedDecisionId }

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0A1D25), Color(0xFF090D13))))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(dossierText(selectedLanguage, "A.E.G.I.S. / PERSONNEL RECORD", "A.E.G.I.S. / BERKAS PERSONEL"), color = Color(0xFF83C7D0),
                    fontSize = 9.sp, letterSpacing = 1.4.sp, fontWeight = FontWeight.Bold)
                Text(dossierText(selectedLanguage, "PERSONNEL DOSSIER", "BERKAS PERSONEL"), color = Color.White, fontSize = 24.sp,
                    fontWeight = FontWeight.Black)
            }
            TextButton(onClick = onBack) { Text(dossierText(selectedLanguage, "BACK", "KEMBALI"), color = Color(0xFFBCE8EA)) }
        }

        Surface(
            color = Color(0xFF102A32),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF31545B)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(dossierText(selectedLanguage, "RECRUIT IDENTITY", "IDENTITAS REKRUT"), color = Color(0xFF83C7D0), fontSize = 9.sp,
                    letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
                Text(dossierText(selectedLanguage, "TRAINEE AGENT", "AGEN MAGANG"), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text(dossierText(selectedLanguage, "ORGANIZATION  /  A.E.G.I.S.", "ORGANISASI  /  A.E.G.I.S."), color = Color(0xFFB8C9CC), fontSize = 10.sp)
                Text(dossierText(selectedLanguage, "ASSIGNMENT  /  MANILA COMMAND", "PENUGASAN  /  KOMANDO MANILA"), color = Color(0xFFB8C9CC), fontSize = 10.sp)
                HorizontalDivider(color = Color(0xFF31545B))
                DossierValue(dossierText(selectedLanguage, "MENTOR", "MENTOR"), if (gameState.freyaContactOpened) "FREYA KNOX" else dossierText(selectedLanguage, "NOT ASSIGNED", "BELUM DITETAPKAN"))
                DossierValue(dossierText(selectedLanguage, "SUPPORT SYSTEM", "SISTEM PENDAMPING"), if (gameState.nexusContactUnlocked) "NEXUS / AKTIF" else dossierText(selectedLanguage, "NOT ACTIVATED", "BELUM DIAKTIFKAN"))
                DossierValue(dossierText(selectedLanguage, "RECRUITMENT STATUS", "STATUS REKRUTMEN"), if (gameState.recruitmentNotificationOpened) dossierText(selectedLanguage, "INVITATION OPENED", "UNDANGAN DIBUKA") else dossierText(selectedLanguage, "AWAITING CONFIRMATION", "MENUNGGU KONFIRMASI"))
            }
        }

        Surface(
            color = Color(0xFF111B22),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF2B4149)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(dossierText(selectedLanguage, "QUALIFICATIONS AND PROGRESS", "KUALIFIKASI DAN PROGRES"), color = Color(0xFF83C7D0),
                    fontSize = 9.sp, letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
                DossierValue(dossierText(selectedLanguage, "RANK", "PANGKAT"), localizedRankName(gameState.rank, selectedLanguage))
                DossierValue(dossierText(selectedLanguage, "EXPERIENCE", "PENGALAMAN"), "${gameState.experience} XP")
                DossierValue(dossierText(selectedLanguage, "CLEARANCE", "OTORISASI"), gameState.clearance.toString())
                DossierValue(dossierText(selectedLanguage, "TECHNICAL COMPETENCY", "KOMPETENSI TEKNIS"), "${gameState.technicalCompetencyXp} XP")
                DossierValue(dossierText(selectedLanguage, "INTELLIGENCE COMPETENCY", "KOMPETENSI INTELIJEN"), "${gameState.intelligenceCompetencyXp} XP")
                DossierValue(dossierText(selectedLanguage, "CAMPAIGN", "KAMPANYE"), dossierText(selectedLanguage, "FIRST CONTACT  /  $storyProgress OF 14 BEATS", "FIRST CONTACT  /  $storyProgress DARI 14 TAHAP"))
                DossierValue(dossierText(selectedLanguage, "REQUIRED TRAINING VERIFIED", "PELATIHAN WAJIB TERVERIFIKASI"), "$completedGuidedTraining / ${guidedTraining.size}")
                DossierValue(dossierText(selectedLanguage, "OPTIONAL TRAINING VERIFIED", "PELATIHAN OPSIONAL TERVERIFIKASI"), completedLegacyMissions.toString())
                DossierValue(dossierText(selectedLanguage, "EVIDENCE RECORDS", "CATATAN BUKTI"), gameState.evidence.size.toString())
                DossierValue(dossierText(selectedLanguage, "RX-14 CASE", "KASUS RX-14"), localizedPhaseName(casePhase, selectedLanguage))
                if (nextRank != null) {
                    val currentFloor = ProgressionRules.rankThreshold(gameState.rank)
                    val remaining = (nextRank - gameState.experience).coerceAtLeast(0)
                    Text(dossierText(selectedLanguage, "NEXT RANK PROGRESS  /  $remaining XP REMAINING", "PROGRES RANK BERIKUTNYA  /  $remaining XP TERSISA"),
                        color = Color(0xFF8AA8AE), fontSize = 9.sp)
                    LinearProgressIndicator(
                        progress = { ((gameState.experience - currentFloor).coerceAtLeast(0).toFloat() /
                            (nextRank - currentFloor).coerceAtLeast(1)).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(5.dp),
                        color = Color(0xFFB85B70), trackColor = Color(0xFF263A40)
                    )
                } else {
                    Text(dossierText(selectedLanguage, "HIGHEST RANK REACHED", "RANK TERTINGGI TERCAPAI"), color = Color(0xFF8CD5AC), fontSize = 9.sp)
                }
            }
        }

        Text(dossierText(selectedLanguage, "QUALIFICATION HISTORY", "RIWAYAT KUALIFIKASI"), color = Color(0xFF83C7D0), fontSize = 9.sp,
            letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
        if (completed.isEmpty()) {
            DossierNote(dossierText(selectedLanguage, "No missions have been verified. Operation results will appear here after evaluator approval.", "Belum ada misi yang terverifikasi. Hasil operasi akan dicatat di sini setelah evaluator menyetujuinya."))
        } else {
            completed.forEachIndexed { index, mission ->
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text("%02d".format(index + 1), color = Color(0xFF8CD5AC),
                        fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                        Text(CaseLocalizedContent.missionTitle(mission, selectedLanguage), color = Color.White, fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold)
                        Text("+${mission.rewardXp} XP  ·  ${mission.id}",
                            color = Color(0xFF8AA8AE), fontSize = 8.sp)
                    }
                    Text(dossierText(selectedLanguage, "PASSED", "LULUS"), color = Color(0xFF8CD5AC), fontSize = 8.sp,
                        fontWeight = FontWeight.Bold)
                }
            }
        }
        Text(dossierText(selectedLanguage, "CASE HISTORY", "RIWAYAT KASUS"), color = Color(0xFF83C7D0), fontSize = 9.sp,
            letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
        Surface(
            color = Color(0xFF111B22), shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF2B4149)), modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(caseDefinition.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                DossierValue(dossierText(selectedLanguage, "STATUS", "STATUS"), localizedPhaseName(casePhase, selectedLanguage))
                DossierValue(dossierText(selectedLanguage, "SOURCES REVIEWED", "SUMBER DIPERIKSA"), "${caseProgress.discoveredEvidenceIds.intersect(caseDefinition.requiredEvidenceIds).size} / ${caseDefinition.requiredEvidenceIds.size}")
                DossierValue(dossierText(selectedLanguage, "SOURCES ANALYZED", "SUMBER DIANALISIS"), "${caseProgress.analyzedEvidenceIds.intersect(caseDefinition.requiredEvidenceIds).size} / ${caseDefinition.requiredEvidenceIds.size}")
                Text(dossierText(selectedLanguage, "CONCLUSION", "KESIMPULAN"), color = Color(0xFF83C7D0), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                DossierNote(analysisConclusion?.title ?: dossierText(selectedLanguage, "No conclusion saved.", "Belum ada kesimpulan tersimpan."))
                DossierValue(dossierText(selectedLanguage, "DECISION", "KEPUTUSAN"), caseDecision?.title ?: dossierText(selectedLanguage, "Not selected", "Belum dipilih"))
                Text(dossierText(selectedLanguage, "DECISION RATIONALE", "ALASAN KEPUTUSAN"), color = Color(0xFF83C7D0), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                DossierNote(caseProgress.decisionRationale.ifBlank { dossierText(selectedLanguage, "No rationale saved.", "Belum ada alasan tersimpan.") })
                DossierValue(dossierText(selectedLanguage, "DEBRIEF", "DEBRIEFING"), if (caseProgress.debriefViewed) dossierText(selectedLanguage, "COMPLETE", "SELESAI") else dossierText(selectedLanguage, "INCOMPLETE", "BELUM SELESAI"))
                DossierValue(dossierText(selectedLanguage, "REWARD", "HADIAH"), if (caseDefinition.id in gameState.completedMissionIds) "+100 XP" else dossierText(selectedLanguage, "NOT AWARDED", "BELUM DITERIMA"))
            }
        }
        DossierNote(dossierText(selectedLanguage, "This dossier is a local A.E.G.I.S. simulation record. The player’s personal identity is not collected or sent to a server.", "Berkas ini merupakan catatan lokal simulasi A.E.G.I.S. Identitas pribadi pemain tidak dikumpulkan atau dikirim ke server."))
    }
}

@Composable
private fun DossierValue(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = Color(0xFF8AA8AE), fontSize = 9.sp, fontWeight = FontWeight.Medium)
        Text(value, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun DossierNote(value: String) {
    Surface(color = Color(0xFF111B22), shape = RoundedCornerShape(7.dp),
        border = BorderStroke(1.dp, Color(0xFF263940)), modifier = Modifier.fillMaxWidth()) {
        Text(value, Modifier.padding(14.dp), color = Color(0xFF9AB4B8),
            fontSize = 10.sp, lineHeight = 16.sp)
    }
}


internal fun dossierText(language: GameLanguage, english: String, indonesian: String): String =
    if (language == GameLanguage.ENGLISH) english else indonesian
