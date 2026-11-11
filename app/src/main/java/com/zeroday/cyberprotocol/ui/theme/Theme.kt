package com.zeroday.cyberprotocol.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CyberColors = darkColorScheme(
    primary = Color(0xFF52F2A5), secondary = Color(0xFF55C8E8),
    background = Color(0xFF080D12), surface = Color(0xFF111B23),
    onPrimary = Color(0xFF080D12), onBackground = Color(0xFFEAF2F7),
    onSurface = Color(0xFFEAF2F7)
)

@Composable
fun CyberProtocolTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = CyberColors, content = content)
}
