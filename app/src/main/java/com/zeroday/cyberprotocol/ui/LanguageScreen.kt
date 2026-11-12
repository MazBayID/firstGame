package com.zeroday.cyberprotocol.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.zeroday.cyberprotocol.core.model.GameLanguage

@Composable
internal fun LanguageScreen(
    selectedLanguage: GameLanguage,
    onLanguageChange: (GameLanguage) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0A1D25), Color(0xFF090D13))))
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(localizedUiText(selectedLanguage, "GAME LANGUAGE", "BAHASA GAME"), Modifier.weight(1f),
                color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
            TextButton(onClick = onBack) {
                Text(localizedUiText(selectedLanguage, "BACK", "KEMBALI"), color = Color(0xFFBCE8EA))
            }
        }
        Text(localizedUiText(selectedLanguage, "Choose the preferred display language. Campaign progress and saved work are stored separately.", "Pilih bahasa tampilan. Progres kampanye dan pekerjaan tersimpan dikelola secara terpisah."), color = Color(0xFF9AB4B8),
            fontSize = 12.sp, lineHeight = 18.sp)
        LanguageOption("English",
            localizedUiText(selectedLanguage, "Default for new installations", "Bahasa bawaan untuk instalasi baru"),
            selectedLanguage == GameLanguage.ENGLISH) { onLanguageChange(GameLanguage.ENGLISH) }
        LanguageOption("Bahasa Indonesia",
            localizedUiText(selectedLanguage, "Indonesian language preference", "Pilihan bahasa Indonesia"),
            selectedLanguage == GameLanguage.INDONESIAN) { onLanguageChange(GameLanguage.INDONESIAN) }
        Surface(color = Color(0xFF2A2118), shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF80633A)), modifier = Modifier.fillMaxWidth()) {
            Text(localizedUiText(selectedLanguage, "Localization is still in progress. Some player-facing content may remain in the other language.", "Lokalisasi masih berlangsung. Sebagian konten yang tampil kepada pemain mungkin masih menggunakan bahasa lain."),
                Modifier.padding(14.dp), color = Color(0xFFE3C17B), fontSize = 11.sp, lineHeight = 17.sp)
        }
    }
}

@Composable
private fun LanguageOption(title: String, description: String, selected: Boolean, onClick: () -> Unit) {
    Surface(onClick = onClick,
        color = if (selected) Color(0xFF17333B) else Color(0xFF111B22),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, if (selected) Color(0xFF83C7D0) else Color(0xFF2B4149)),
        modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = selected, onClick = onClick)
            Column(Modifier.weight(1f).padding(start = 8.dp)) {
                Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(description, color = Color(0xFF9AB4B8), fontSize = 10.sp)
            }
        }
    }
}
