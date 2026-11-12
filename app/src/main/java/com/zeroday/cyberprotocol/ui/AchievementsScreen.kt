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
import com.zeroday.cyberprotocol.core.model.CaseProgress
import com.zeroday.cyberprotocol.core.model.GameLanguage
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.data.Campaign01Catalog

internal data class AchievementProgress(
    val id: String,
    val englishTitle: String,
    val indonesianTitle: String,
    val englishDescription: String,
    val indonesianDescription: String,
    val progress: Int,
    val target: Int
) {
    val completed: Boolean get() = progress >= target
}

internal object AchievementPolicy {
    fun evaluate(
        gameState: GameState,
        caseDefinition: CaseDefinition,
        caseProgress: CaseProgress
    ): List<AchievementProgress> {
        val required = caseDefinition.requiredEvidenceIds
        val discovered = caseProgress.discoveredEvidenceIds.intersect(required).size
        val analyzed = caseProgress.analyzedEvidenceIds.intersect(required).size
        val optionalCompleted = Campaign01Catalog.retiredLegacyStoryMissionIds.count {
            it in gameState.completedMissionIds
        }
        return listOf(
            AchievementProgress(
                "first_signal", "First Signal", "Sinyal Pertama",
                "Complete the first A.E.G.I.S. orientation task.", "Selesaikan tugas orientasi A.E.G.I.S. pertama.",
                if ("first_contact.signal" in gameState.completedMissionIds) 1 else 0, 1
            ),
            AchievementProgress(
                "identity_verified", "Identity Verified", "Identitas Terverifikasi",
                "Complete the identity verification task.", "Selesaikan tugas verifikasi identitas.",
                if ("first_contact.identity" in gameState.completedMissionIds) 1 else 0, 1
            ),
            AchievementProgress(
                "evidence_collector", "Evidence Collector", "Pengumpul Bukti",
                "Open every required source in the RX-14 case.", "Buka seluruh sumber wajib dalam kasus RX-14.",
                discovered, required.size.coerceAtLeast(1)
            ),
            AchievementProgress(
                "evidence_analyst", "Evidence Analyst", "Analis Bukti",
                "Record an analysis for every required source.", "Catat analisis untuk setiap sumber wajib.",
                analyzed, required.size.coerceAtLeast(1)
            ),
            AchievementProgress(
                "operational_judgment", "Operational Judgment", "Pertimbangan Operasional",
                "Save an operational decision for the RX-14 case.", "Simpan keputusan operasional untuk kasus RX-14.",
                if (caseProgress.selectedDecisionId != null) 1 else 0, 1
            ),
            AchievementProgress(
                "first_contact_report", "First Contact Report", "Laporan First Contact",
                "Review the final case debrief.", "Tinjau debrief akhir kasus.",
                if (caseProgress.debriefViewed) 1 else 0, 1
            ),
            AchievementProgress(
                "practice_archive", "Practice Archivist", "Pengarsip Latihan",
                "Complete all six optional legacy Python exercises.", "Selesaikan keenam latihan Python opsional.",
                optionalCompleted, Campaign01Catalog.retiredLegacyStoryMissionIds.size.coerceAtLeast(1)
            ),
            AchievementProgress(
                "technical_track", "Technical Apprentice", "Magang Teknis",
                "Earn 100 Technical competency XP.", "Raih 100 XP kompetensi Teknis.",
                gameState.technicalCompetencyXp.coerceIn(0, 100), 100
            ),
            AchievementProgress(
                "intelligence_track", "Intelligence Analyst", "Analis Intelijen",
                "Earn 100 Intelligence competency XP.", "Raih 100 XP kompetensi Intelijen.",
                gameState.intelligenceCompetencyXp.coerceIn(0, 100), 100
            ),
            AchievementProgress(
                "junior_analyst", "Junior Analyst", "Analis Junior",
                "Reach the Junior Analyst rank.", "Capai pangkat Analis Junior.",
                if (gameState.experience >= 250) 1 else 0, 1
            )
        )
    }
}

@Composable
internal fun AchievementsScreen(
    gameState: GameState,
    selectedLanguage: GameLanguage,
    caseDefinition: CaseDefinition,
    caseProgress: CaseProgress,
    onBack: () -> Unit
) {
    val achievements = AchievementPolicy.evaluate(gameState, caseDefinition, caseProgress)
    val earned = achievements.count { it.completed }

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0A1D25), Color(0xFF090D13))))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(achievementText(selectedLanguage, "A.E.G.I.S. / HONORS DIVISION", "A.E.G.I.S. / DIVISI PENGHARGAAN"),
                    color = Color(0xFF83C7D0), fontSize = 9.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
                Text(achievementText(selectedLanguage, "ACHIEVEMENTS", "PENCAPAIAN"),
                    color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Black)
            }
            TextButton(onClick = onBack) {
                Text(achievementText(selectedLanguage, "BACK", "KEMBALI"), color = Color(0xFFBCE8EA))
            }
        }

        Surface(
            color = Color(0xFF102A32), shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF31545B)), modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(achievementText(selectedLanguage, "OPERATIVE MILESTONES", "PENCAPAIAN AGEN"),
                    color = Color(0xFF8AA8AE), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                Text("$earned / ${achievements.size}",
                    color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Black)
                LinearProgressIndicator(
                    progress = { earned.toFloat() / achievements.size.coerceAtLeast(1) },
                    modifier = Modifier.fillMaxWidth().height(7.dp),
                    color = Color(0xFF66C6A0), trackColor = Color(0xFF263C42)
                )
                Text(achievementText(selectedLanguage, "Achievements are calculated from saved campaign progress.",
                    "Pencapaian dihitung dari progres kampanye yang tersimpan."),
                    color = Color(0xFF9AB4B8), fontSize = 10.sp)
            }
        }

        achievements.forEach { achievement ->
            val complete = achievement.completed
            Surface(
                color = if (complete) Color(0xFF132B29) else Color(0xFF111B22),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, if (complete) Color(0xFF3E7766) else Color(0xFF263940)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.padding(13.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(11.dp)
                ) {
                    Surface(
                        color = if (complete) Color(0xFF285C4B) else Color(0xFF26343B),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (complete) "✓" else "·", Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                            color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            achievementText(selectedLanguage, achievement.englishTitle, achievement.indonesianTitle),
                            color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold
                        )
                        Text(
                            achievementText(selectedLanguage, achievement.englishDescription, achievement.indonesianDescription),
                            color = Color(0xFF9AB4B8), fontSize = 10.sp, lineHeight = 15.sp
                        )
                        LinearProgressIndicator(
                            progress = { achievement.progress.toFloat() / achievement.target.coerceAtLeast(1) },
                            modifier = Modifier.fillMaxWidth().height(4.dp),
                            color = if (complete) Color(0xFF66C6A0) else Color(0xFF5C9DA5),
                            trackColor = Color(0xFF263C42)
                        )
                    }
                    Text(
                        "${achievement.progress}/${achievement.target}",
                        color = if (complete) Color(0xFF8CD5AC) else Color(0xFF8AA8AE),
                        fontSize = 9.sp, fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Text(
            achievementText(selectedLanguage,
                "Optional practice achievements never block the First Contact campaign or its case completion.",
                "Pencapaian latihan opsional tidak pernah menghambat kampanye First Contact atau penyelesaian kasusnya."),
            color = Color(0xFF71858B), fontSize = 9.sp, lineHeight = 15.sp
        )
    }
}

private fun achievementText(language: GameLanguage, english: String, indonesian: String): String =
    localizedUiText(language, english, indonesian)
