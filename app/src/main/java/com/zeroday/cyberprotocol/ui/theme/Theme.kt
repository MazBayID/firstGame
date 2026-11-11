package com.zeroday.cyberprotocol.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Cyber Protocol visual system: Obsidian / Charcoal / Crimson. */
object CyberPalette {
    val Obsidian = Color(0xFF090A0E)
    val Charcoal = Color(0xFF15171D)
    val Panel = Color(0xFF1C1F27)
    val Crimson = Color(0xFFD62839)
    val BloodRed = Color(0xFF8E1525)
    val SoftWhite = Color(0xFFF1F1F3)
    val SteelGray = Color(0xFF858A96)
    val MutedText = Color(0xFFB2B5BE)
    val Divider = Color(0xFF292B33)
    val Success = Color(0xFF55B887)
    val Warning = Color(0xFFE3A344)
}
private val CyberColors = darkColorScheme(
    primary = CyberPalette.Crimson, onPrimary = Color.White,
    primaryContainer = Color(0xFF4A101A), onPrimaryContainer = Color(0xFFFFD9DE),
    secondary = CyberPalette.SteelGray, onSecondary = Color(0xFF101116),
    secondaryContainer = Color(0xFF292C35), onSecondaryContainer = CyberPalette.SoftWhite,
    tertiary = Color(0xFFE3A0A7), background = CyberPalette.Obsidian,
    onBackground = CyberPalette.SoftWhite, surface = Color(0xFF12141A),
    onSurface = CyberPalette.SoftWhite, surfaceVariant = CyberPalette.Panel,
    onSurfaceVariant = CyberPalette.MutedText, outline = Color(0xFF41434D),
    outlineVariant = CyberPalette.Divider, error = Color(0xFFFF5C67), onError = Color.White
)
private val CyberShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp)
)
@Composable
fun CyberProtocolTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = CyberColors, shapes = CyberShapes, content = content)
}
