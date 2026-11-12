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
import com.zeroday.cyberprotocol.data.Campaign01Catalog
import com.zeroday.cyberprotocol.domain.usecase.ProgressionRules

@Composable
internal fun PersonnelDossierScreen(
    gameState: GameState,
    caseDefinition: CaseDefinition,
    caseProgress: CaseProgress,
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
                Text("A.E.G.I.S. / PERSONNEL RECORD", color = Color(0xFF83C7D0),
                    fontSize = 9.sp, letterSpacing = 1.4.sp, fontWeight = FontWeight.Bold)
                Text("BERKAS PERSONEL", color = Color.White, fontSize = 24.sp,
                    fontWeight = FontWeight.Black)
            }
            TextButton(onClick = onBack) { Text("KEMBALI", color = Color(0xFFBCE8EA)) }
        }

        Surface(
            color = Color(0xFF102A32),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF31545B)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("IDENTITAS REKRUT", color = Color(0xFF83C7D0), fontSize = 9.sp,
                    letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
                Text("AGEN MAGANG", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text("ORGANISASI  /  A.E.G.I.S.", color = Color(0xFFB8C9CC), fontSize = 10.sp)
                Text("PENEMPATAN  /  MANILA COMMAND", color = Color(0xFFB8C9CC), fontSize = 10.sp)
                HorizontalDivider(color = Color(0xFF31545B))
                DossierValue("MENTOR", if (gameState.freyaContactOpened) "FREYA KNOX" else "BELUM DITETAPKAN")
                DossierValue("SISTEM PENDAMPING", if (gameState.nexusContactUnlocked) "NEXUS / AKTIF" else "BELUM DIAKTIFKAN")
                DossierValue("STATUS REKRUTMEN", if (gameState.recruitmentNotificationOpened) "UNDANGAN DIBUKA" else "MENUNGGU KONFIRMASI")
            }
        }

        Surface(
            color = Color(0xFF111B22),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF2B4149)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("KUALIFIKASI DAN PROGRES", color = Color(0xFF83C7D0),
                    fontSize = 9.sp, letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
                DossierValue("RANK", gameState.rank.replace('_', ' '))
                DossierValue("PENGALAMAN", "${gameState.experience} XP")
                DossierValue("CLEARANCE", gameState.clearance.toString())
                DossierValue("TECHNICAL COMPETENCY", "${gameState.technicalCompetencyXp} XP")
                DossierValue("INTELLIGENCE COMPETENCY", "${gameState.intelligenceCompetencyXp} XP")
                DossierValue("KAMPANYE", "FIRST CONTACT  /  $storyProgress OF 14 BEATS")
                DossierValue("LATIHAN WAJIB TERVERIFIKASI", "$completedGuidedTraining / ${guidedTraining.size}")
                DossierValue("LATIHAN TAMBAHAN TERVERIFIKASI", completedLegacyMissions.toString())
                DossierValue("REKAMAN BUKTI", gameState.evidence.size.toString())
                DossierValue("KASUS RX-14", casePhase.name.replace('_', ' '))
                if (nextRank != null) {
                    val currentFloor = ProgressionRules.rankThreshold(gameState.rank)
                    val remaining = (nextRank - gameState.experience).coerceAtLeast(0)
                    Text("PROGRES RANK BERIKUTNYA  /  $remaining XP TERSISA",
                        color = Color(0xFF8AA8AE), fontSize = 9.sp)
                    LinearProgressIndicator(
                        progress = { ((gameState.experience - currentFloor).coerceAtLeast(0).toFloat() /
                            (nextRank - currentFloor).coerceAtLeast(1)).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(5.dp),
                        color = Color(0xFFB85B70), trackColor = Color(0xFF263A40)
                    )
                } else {
                    Text("RANK TERTINGGI TERCAPAI", color = Color(0xFF8CD5AC), fontSize = 9.sp)
                }
            }
        }

        Text("RIWAYAT KUALIFIKASI", color = Color(0xFF83C7D0), fontSize = 9.sp,
            letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
        if (completed.isEmpty()) {
            DossierNote("Belum ada misi yang terverifikasi. Hasil operasi akan dicatat di sini setelah evaluator menyetujuinya.")
        } else {
            completed.forEachIndexed { index, mission ->
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text("%02d".format(index + 1), color = Color(0xFF8CD5AC),
                        fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                        Text(mission.title, color = Color.White, fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold)
                        Text("+${mission.rewardXp} XP  ·  ${mission.id}",
                            color = Color(0xFF8AA8AE), fontSize = 8.sp)
                    }
                    Text("LULUS", color = Color(0xFF8CD5AC), fontSize = 8.sp,
                        fontWeight = FontWeight.Bold)
                }
            }
        }
        Text("RIWAYAT KASUS", color = Color(0xFF83C7D0), fontSize = 9.sp,
            letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
        Surface(
            color = Color(0xFF111B22), shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF2B4149)), modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(caseDefinition.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                DossierValue("STATUS", casePhase.name.replace('_', ' '))
                DossierValue("SUMBER DIPERIKSA", "${caseProgress.discoveredEvidenceIds.intersect(caseDefinition.requiredEvidenceIds).size} / ${caseDefinition.requiredEvidenceIds.size}")
                DossierValue("SUMBER DIANALISIS", "${caseProgress.analyzedEvidenceIds.intersect(caseDefinition.requiredEvidenceIds).size} / ${caseDefinition.requiredEvidenceIds.size}")
                Text("KESIMPULAN", color = Color(0xFF83C7D0), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                DossierNote(analysisConclusion?.title ?: "Belum ada kesimpulan tersimpan.")
                DossierValue("KEPUTUSAN", caseDecision?.title ?: "Belum dipilih")
                Text("ALASAN KEPUTUSAN", color = Color(0xFF83C7D0), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                DossierNote(caseProgress.decisionRationale.ifBlank { "Belum ada alasan tersimpan." })
                DossierValue("DEBRIEF", if (caseProgress.debriefViewed) "SELESAI" else "BELUM SELESAI")
                DossierValue("REWARD", if (caseDefinition.id in gameState.completedMissionIds) "+100 XP" else "BELUM DITERIMA")
            }
        }
        DossierNote("Berkas ini merupakan catatan lokal simulasi A.E.G.I.S. Identitas pribadi pemain tidak dikumpulkan atau dikirim ke server.")
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
