package com.zeroday.cyberprotocol.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.data.Campaign01Catalog

/**
 * Operations is the player's mission-control surface, not a second progression
 * engine. It renders persisted campaign facts and only resumes the mission
 * already authorized by the story flow.
 */
@Composable
internal fun OperationsScreen(
    gameState: GameState,
    onBack: () -> Unit,
    onResumeActiveMission: () -> Unit
) {
    val missions = Campaign01Catalog.missions
    val completedCount = missions.count { it.id in gameState.completedMissionIds }
    val activeMission = missions.firstOrNull { it.id == gameState.activeMissionId }

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0A1D25), Color(0xFF090D13))))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("PUSAT OPERASI", color = Color(0xFFBCE8EA), fontSize = 10.sp,
                    letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
                Text("FIRST CONTACT", color = Color.White, fontSize = 25.sp,
                    fontWeight = FontWeight.Black)
                Text("A.E.G.I.S.  /  MANILA COMMAND", color = Color(0xFF8AA8AE),
                    fontSize = 9.sp, letterSpacing = 1.2.sp)
            }
            TextButton(onClick = onBack) { Text("KEMBALI", color = Color(0xFFBCE8EA)) }
        }

        Surface(
            color = Color(0xFF102A32),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF31545B)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Text("STATUS OPERASI", color = Color(0xFF83C7D0), fontSize = 9.sp,
                    letterSpacing = 1.4.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("$completedCount", color = Color.White, fontSize = 30.sp,
                        fontWeight = FontWeight.Black)
                    Text(" / ${missions.size} MISI TERVERIFIKASI", color = Color(0xFF9AB4B8),
                        fontSize = 10.sp, modifier = Modifier.padding(start = 7.dp, bottom = 5.dp))
                    Spacer(Modifier.weight(1f))
                    Text("${(completedCount * 100 / missions.size)}%",
                        color = Color(0xFFE0A2AD), fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(
                    progress = { completedCount.toFloat() / missions.size.coerceAtLeast(1) },
                    modifier = Modifier.fillMaxWidth().height(5.dp),
                    color = Color(0xFFB85B70),
                    trackColor = Color(0xFF263A40)
                )
                Text("RANK ${gameState.rank.replace('_', ' ')}   ·   ${gameState.experience} XP   ·   CLEARANCE ${gameState.clearance}",
                    color = Color(0xFFB8C9CC), fontSize = 10.sp)
            }
        }

        if (activeMission != null) {
            Surface(
                color = Color(0xFF182B27),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFF3D715D)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("OPERASI AKTIF", color = Color(0xFF8CD5AC), fontSize = 9.sp,
                        letterSpacing = 1.4.sp, fontWeight = FontWeight.Bold)
                    Text(activeMission.title, color = Color.White, fontSize = 18.sp,
                        fontWeight = FontWeight.Bold)
                    Text(activeMission.briefing, color = Color(0xFFC5D3D0),
                        fontSize = 12.sp, lineHeight = 18.sp)
                    Button(
                        onClick = onResumeActiveMission,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28634D))
                    ) { Text("LANJUTKAN OPERASI") }
                }
            }
        } else {
            Surface(
                color = Color(0xFF18232A),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFF34464D)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (gameState.storyBeat >= 16) "First Contact selesai. Semua temuan tetap tersimpan di Analisis dan Arsip."
                    else "Belum ada misi aktif. Lanjutkan alur cerita untuk menerima briefing dan otorisasi operasi berikutnya.",
                    Modifier.padding(15.dp), color = Color(0xFFB7C6CB), fontSize = 12.sp, lineHeight = 18.sp
                )
            }
        }

        Text("DAFTAR MISI", color = Color(0xFF83C7D0), fontSize = 10.sp,
            letterSpacing = 1.5.sp, fontWeight = FontWeight.Bold)
        missions.forEachIndexed { index, mission ->
            val completed = mission.id in gameState.completedMissionIds
            val active = mission.id == gameState.activeMissionId
            val status = when {
                completed -> "TERVERIFIKASI"
                active -> "AKTIF"
                else -> "MENUNGGU ALUR CERITA"
            }
            Surface(
                color = if (active) Color(0xFF182B27) else Color(0xFF111B22),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, when {
                    active -> Color(0xFF3D715D)
                    completed -> Color(0xFF315348)
                    else -> Color(0xFF263940)
                }),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.padding(horizontal = 13.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("%02d".format(index + 1), color = Color(0xFF83C7D0),
                        fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Column(Modifier.weight(1f).padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(mission.title, color = Color.White, fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold)
                        Text("XP ${mission.rewardXp}  ·  CLEARANCE ${mission.requiredClearance}",
                            color = Color(0xFF8AA8AE), fontSize = 9.sp)
                    }
                    Text(status, color = when {
                        completed -> Color(0xFF8CD5AC)
                        active -> Color(0xFFE0A2AD)
                        else -> Color(0xFF73878D)
                    }, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Text(
            "Otorisasi misi mengikuti checkpoint cerita. Pusat Operasi tidak dapat membuka misi yang belum diberikan oleh alur kampanye.",
            color = Color(0xFF71858B), fontSize = 10.sp, lineHeight = 16.sp
        )
    }
}
