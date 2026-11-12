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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroday.cyberprotocol.core.model.MissionDefinition

@Composable
internal fun PracticeScreen(
    missions: List<MissionDefinition>,
    completedMissionIds: Set<String>,
    selectedMission: MissionDefinition?,
    code: TextFieldValue,
    output: String,
    feedback: String,
    hintLevel: Int,
    canSubmit: Boolean,
    busy: Boolean,
    clearance: Int,
    onSelectMission: (String) -> Unit,
    onBackToList: () -> Unit,
    onBack: () -> Unit,
    onCodeChange: (TextFieldValue) -> Unit,
    onSave: () -> Unit,
    onRun: () -> Unit,
    onSubmit: () -> Unit,
    onRequestHint: () -> Unit
) {
    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0A1D25), Color(0xFF090D13))))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("A.E.G.I.S. / TRAINING SIMULATOR", color = Color(0xFF83C7D0),
                    fontSize = 9.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
                Text("LATIHAN TAMBAHAN", color = Color.White, fontSize = 22.sp,
                    fontWeight = FontWeight.Black)
            }
            TextButton(onClick = onBack) { Text("APLIKASI", color = Color(0xFFBCE8EA)) }
        }
        Surface(color = Color(0xFF102A32), shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF31545B)), modifier = Modifier.fillMaxWidth()) {
            Text(
                "SIMULASI OPSIONAL  ·  Latihan ini tidak menjadi syarat penyelesaian cerita atau kasus RX-14. Hasil RUN tidak otomatis menyelesaikan latihan; gunakan SUBMIT setelah kode berhasil dijalankan.",
                Modifier.padding(13.dp), color = Color(0xFFC2D0D4), fontSize = 10.sp, lineHeight = 16.sp
            )
        }

        if (selectedMission == null) {
            Text("ARSIP LATIHAN", color = Color(0xFF83C7D0), fontSize = 9.sp,
                letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
            Text("Pilih konsep yang ingin kamu latih kembali. Semua latihan memakai workspace dan evaluator yang sama dengan sistem misi.",
                color = Color(0xFF9AB4B8), fontSize = 11.sp, lineHeight = 17.sp)
            missions.forEach { mission ->
                val completed = mission.id in completedMissionIds
                Surface(
                    onClick = { if (clearance >= mission.requiredClearance) onSelectMission(mission.id) },
                    color = Color(0xFF111B22), shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (completed) Color(0xFF477D65) else Color(0xFF2B4149)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(mission.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(mission.objectives.joinToString("  ·  ") { it.title },
                                color = Color(0xFF9AB4B8), fontSize = 9.sp)
                            Text(
                                when {
                                    completed -> "TERVERIFIKASI  ·  BISA DIULANG"
                                    clearance < mission.requiredClearance -> "CLEARANCE ${mission.requiredClearance} DIPERLUKAN"
                                    else -> "BELUM DIVERIFIKASI  ·  +${mission.rewardXp} XP"
                                },
                                color = if (completed) Color(0xFF8CD5AC) else Color(0xFF83C7D0),
                                fontSize = 8.sp, fontWeight = FontWeight.Bold
                            )
                        }
                        Text("BUKA  ›", color = Color(0xFF83C7D0), fontSize = 9.sp)
                    }
                }
            }
        } else {
            TextButton(onClick = onBackToList) { Text("‹  DAFTAR LATIHAN", color = Color(0xFF83C7D0)) }
            Text(selectedMission.title, color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Black)
            Text(selectedMission.briefing, color = Color(0xFFC2D0D4), fontSize = 11.sp, lineHeight = 18.sp)
            Panel {
                Text("TUJUAN LATIHAN", color = Color(0xFF83C7D0), fontSize = 9.sp,
                    letterSpacing = 1.1.sp, fontWeight = FontWeight.Bold)
                selectedMission.objectives.forEachIndexed { index, objective ->
                    Text("${index + 1}. ${objective.title}", color = Color.White,
                        fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text(objective.description, color = Color(0xFF9AB4B8), fontSize = 10.sp, lineHeight = 15.sp)
                }
            }
            if (selectedMission.runtimeVariables.isNotEmpty()) {
                Panel {
                    Text("DATASET MISI  /  HANYA BACA", color = Color(0xFF83C7D0), fontSize = 9.sp,
                        letterSpacing = 1.1.sp, fontWeight = FontWeight.Bold)
                    selectedMission.runtimeVariables.forEach { (name, values) ->
                        Text("$name =", color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 10.sp)
                        Text(values.joinToString(prefix = "[", postfix = "]") { "\\"$it\\"" },
                            color = Color(0xFFB7D9C2), fontFamily = FontFamily.Monospace, fontSize = 10.sp)
                    }
                }
            }
            Panel {
                Text("RUANG KERJA", color = Color(0xFF83C7D0), fontSize = 9.sp,
                    letterSpacing = 1.1.sp, fontWeight = FontWeight.Bold)
                selectedMission.allowedFiles.sorted().forEach { file ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(file, color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 10.sp)
                        Text(if (file in selectedMission.writableFiles) "BACA / TULIS" else "HANYA BACA",
                            color = Color(0xFF8AA8AE), fontSize = 8.sp)
                    }
                }
            }
            if (hintLevel > 0) {
                selectedMission.hints.firstOrNull { it.level == hintLevel }?.let { hint ->
                    Text("PETUNJUK $hintLevel/3\n${hint.message}", color = Color(0xFF83C7D0),
                        fontSize = 11.sp, lineHeight = 17.sp)
                }
            }
            if (hintLevel < 3) OutlinedButton(onClick = onRequestHint) {
                Text("MINTA PETUNJUK ${hintLevel + 1}/3")
            }
            CodeEditor(code, onCodeChange, "EDITOR KODE PYTHON 3", 190)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onSave, modifier = Modifier.weight(1f)) { Text("SIMPAN DRAF") }
                Button(onClick = onRun, enabled = !busy, modifier = Modifier.weight(1f)) {
                    Text(if (busy) "MENJALANKAN…" else "JALANKAN")
                }
            }
            Button(onClick = onSubmit, enabled = canSubmit && !busy,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28634D))) {
                Text("SUBMIT LATIHAN")
            }
            Panel {
                Text("KELUARAN TERMINAL", color = Color(0xFF83C7D0), fontSize = 9.sp,
                    letterSpacing = 1.1.sp, fontWeight = FontWeight.Bold)
                Text(output, color = Color(0xFFB7D9C2), fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp, lineHeight = 15.sp)
            }
            if (feedback.isNotBlank()) Text(feedback, color = Color(0xFFE3C17B),
                fontSize = 10.sp, lineHeight = 16.sp)
        }
    }
}

@Composable
private fun Panel(content: @Composable ColumnScope.() -> Unit) {
    Surface(color = Color(0xFF111B22), shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFF2B4149)), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
    }
}
