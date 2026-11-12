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
import com.zeroday.cyberprotocol.core.model.GameLanguage
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.data.Campaign01Catalog
import com.zeroday.cyberprotocol.domain.usecase.ProgressionRules

internal data class RankProgressSnapshot(
    val currentRankXp: Int,
    val nextRankXp: Int?,
    val progress: Float,
    val remainingXp: Int
)

internal object OperativeProfilePolicy {
    fun rankProgress(experience: Int): RankProgressSnapshot {
        val safeExperience = experience.coerceAtLeast(0)
        val currentRank = ProgressionRules.rankForExperience(safeExperience)
        val currentThreshold = ProgressionRules.rankThreshold(currentRank)
        val nextThreshold = ProgressionRules.nextRankThreshold(safeExperience)
        if (nextThreshold == null) {
            return RankProgressSnapshot(currentThreshold, null, 1f, 0)
        }
        val span = (nextThreshold - currentThreshold).coerceAtLeast(1)
        val earned = (safeExperience - currentThreshold).coerceIn(0, span)
        return RankProgressSnapshot(
            currentRankXp = currentThreshold,
            nextRankXp = nextThreshold,
            progress = earned.toFloat() / span.toFloat(),
            remainingXp = (nextThreshold - safeExperience).coerceAtLeast(0)
        )
    }
}

@Composable
internal fun OperativeProfileScreen(
    gameState: GameState,
    selectedLanguage: GameLanguage,
    caseDefinition: CaseDefinition,
    caseProgress: CaseProgress,
    onBack: () -> Unit
) {
    val rankProgress = OperativeProfilePolicy.rankProgress(gameState.experience)
    val phase = CasePhaseEvaluator.phase(caseDefinition, caseProgress)
    val completedMissionCount = Campaign01Catalog.missions.count {
        it.id in gameState.completedMissionIds
    }
    val reviewedSources = caseProgress.discoveredEvidenceIds.intersect(caseDefinition.requiredEvidenceIds).size
    val analyzedSources = caseProgress.analyzedEvidenceIds.intersect(caseDefinition.requiredEvidenceIds).size

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0A1D25), Color(0xFF090D13))))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(profileText(selectedLanguage, "A.E.G.I.S. / PERSONNEL RECORD", "A.E.G.I.S. / CATATAN PERSONEL"),
                    color = Color(0xFF83C7D0), fontSize = 9.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
                Text(profileText(selectedLanguage, "OPERATIVE PROFILE", "PROFIL AGEN"),
                    color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Black)
            }
            TextButton(onClick = onBack) {
                Text(profileText(selectedLanguage, "BACK", "KEMBALI"), color = Color(0xFFBCE8EA))
            }
        }

        Surface(
            color = Color(0xFF102A32), shape = RoundedCornerShape(9.dp),
            border = BorderStroke(1.dp, Color(0xFF31545B)), modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(profileText(selectedLanguage, "AGENT ID", "ID AGEN"),
                    color = Color(0xFF8AA8AE), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                Text("RECRUIT-014", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Black)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(profileText(selectedLanguage, "CURRENT RANK", "PANGKAT SAAT INI"),
                            color = Color(0xFF8AA8AE), fontSize = 8.sp)
                        Text(localizedRankName(gameState.rank, selectedLanguage),
                            color = Color(0xFFBCE8EA), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(profileText(selectedLanguage, "CLEARANCE", "TINGKAT AKSES"),
                            color = Color(0xFF8AA8AE), fontSize = 8.sp)
                        Text("LEVEL ${gameState.clearance}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                HorizontalDivider(color = Color(0xFF31545B))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(profileText(selectedLanguage, "TOTAL EXPERIENCE", "TOTAL PENGALAMAN"),
                        color = Color(0xFF8AA8AE), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    Text("${gameState.experience} XP", color = Color(0xFF8CD5AC), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(
                    progress = { rankProgress.progress },
                    modifier = Modifier.fillMaxWidth().height(7.dp),
                    color = Color(0xFF66C6A0),
                    trackColor = Color(0xFF263C42)
                )
                Text(
                    if (rankProgress.nextRankXp == null) {
                        profileText(selectedLanguage, "MAXIMUM RANK REACHED", "PANGKAT MAKSIMUM TERCAPAI")
                    } else {
                        profileText(
                            selectedLanguage,
                            "${rankProgress.remainingXp} XP remaining to the next rank",
                            "Sisa ${rankProgress.remainingXp} XP menuju pangkat berikutnya"
                        )
                    },
                    color = Color(0xFF9AB4B8), fontSize = 10.sp
                )
            }
        }

        ProfileSection(profileText(selectedLanguage, "COMPETENCY TRACKS", "JALUR KOMPETENSI")) {
            ProfileMetric(
                profileText(selectedLanguage, "INTELLIGENCE", "INTELIJEN"),
                "${gameState.intelligenceCompetencyXp} XP",
                profileText(selectedLanguage, "Evidence review and analytical work", "Pemeriksaan bukti dan pekerjaan analitis")
            )
            ProfileMetric(
                profileText(selectedLanguage, "TECHNICAL", "TEKNIS"),
                "${gameState.technicalCompetencyXp} XP",
                profileText(selectedLanguage, "Programming and technical practice", "Pemrograman dan latihan teknis")
            )
        }

        ProfileSection(profileText(selectedLanguage, "OPERATIONAL STATISTICS", "STATISTIK OPERASIONAL")) {
            ProfileMetric(profileText(selectedLanguage, "MISSIONS VERIFIED", "MISI TERVERIFIKASI"),
                completedMissionCount.toString(),
                profileText(selectedLanguage, "Completed authored mission records", "Catatan misi yang telah diselesaikan"))
            ProfileMetric(profileText(selectedLanguage, "EVIDENCE STORED", "BUKTI TERSIMPAN"),
                gameState.evidence.size.toString(),
                profileText(selectedLanguage, "Records retained in the local archive", "Catatan yang tersimpan di arsip lokal"))
            ProfileMetric(profileText(selectedLanguage, "AUTHORIZED APPLICATIONS", "APLIKASI TERBUKA"),
                gameState.unlockedApps.size.toString(),
                profileText(selectedLanguage, "Tools currently unlocked for this operative", "Perangkat yang saat ini terbuka untuk agen"))
        }

        ProfileSection(profileText(selectedLanguage, "FIRST CONTACT / RX-14 CASE", "FIRST CONTACT / KASUS RX-14")) {
            ProfileMetric(profileText(selectedLanguage, "CASE PHASE", "FASE KASUS"),
                localizedPhaseName(phase, selectedLanguage),
                profileText(selectedLanguage, "Current persisted investigation phase", "Tahap investigasi yang tersimpan saat ini"))
            ProfileMetric(profileText(selectedLanguage, "SOURCES REVIEWED", "SUMBER DIPERIKSA"),
                "$reviewedSources / ${caseDefinition.requiredEvidenceIds.size}",
                profileText(selectedLanguage, "Original records opened and registered", "Catatan asli yang dibuka dan didaftarkan"))
            ProfileMetric(profileText(selectedLanguage, "SOURCES ANALYZED", "SUMBER DIANALISIS"),
                "$analyzedSources / ${caseDefinition.requiredEvidenceIds.size}",
                profileText(selectedLanguage, "Required records with a saved analysis", "Catatan wajib dengan analisis tersimpan"))
            ProfileMetric(profileText(selectedLanguage, "DEBRIEF", "DEBRIEFING"),
                if (caseProgress.debriefViewed) profileText(selectedLanguage, "ACKNOWLEDGED", "DITINJAU")
                else profileText(selectedLanguage, "PENDING", "MENUNGGU"),
                profileText(selectedLanguage, "Final case report acknowledgement", "Konfirmasi laporan akhir kasus"))
        }

        Text(
            profileText(selectedLanguage,
                "This is a fictional A.E.G.I.S. personnel record. No personal identity or account data is collected.",
                "Ini adalah catatan personel fiktif A.E.G.I.S. Tidak ada identitas pribadi atau data akun yang dikumpulkan."),
            color = Color(0xFF71858B), fontSize = 9.sp, lineHeight = 15.sp
        )
    }
}

@Composable
private fun ProfileSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        color = Color(0xFF111B22), shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFF263940)), modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, color = Color(0xFF83C7D0), fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            content()
        }
    }
}

@Composable
private fun ProfileMetric(label: String, value: String, detail: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(label, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            Text(detail, color = Color(0xFF8AA8AE), fontSize = 9.sp, lineHeight = 13.sp)
        }
        Text(value, color = Color(0xFFBCE8EA), fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

private fun profileText(language: GameLanguage, english: String, indonesian: String): String =
    localizedUiText(language, english, indonesian)
