package com.zeroday.cyberprotocol.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroday.cyberprotocol.ui.theme.CyberPalette

@Composable
fun CPPanel(modifier: Modifier = Modifier, contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = modifier, color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
    }
}

@Composable
fun CPSectionHeader(title: String, eyebrow: String? = null, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(3.dp)) {
        if (!eyebrow.isNullOrBlank()) Text(eyebrow.uppercase(),
            color = CyberPalette.SteelGray, fontSize = 10.sp,
            fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
        Text(title, color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CPStatusBadge(label: String, modifier: Modifier = Modifier,
    color: Color = CyberPalette.Crimson, textColor: Color = Color.White) {
    Surface(modifier = modifier, color = color.copy(alpha = 0.16f),
        contentColor = textColor, shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, color.copy(alpha = 0.55f))) {
        Text(label.uppercase(), Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
    }
}

@Composable
fun CPPrimaryButton(text: String, onClick: () -> Unit,
    modifier: Modifier = Modifier, enabled: Boolean = true) {
    Button(onClick = onClick, modifier = modifier.fillMaxWidth(), enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = CyberPalette.Crimson,
            contentColor = Color.White,
            disabledContainerColor = CyberPalette.BloodRed.copy(alpha = 0.35f),
            disabledContentColor = Color.White.copy(alpha = 0.55f))) {
        Text(text, fontWeight = FontWeight.Bold, letterSpacing = 0.4.sp)
    }
}

@Composable
fun CPMetric(label: String, value: String, modifier: Modifier = Modifier,
    detail: String? = null) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(label.uppercase(), color = CyberPalette.SteelGray, fontSize = 10.sp,
            fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Text(value, color = MaterialTheme.colorScheme.onSurface,
            fontSize = 20.sp, fontWeight = FontWeight.Black)
        if (!detail.isNullOrBlank()) Text(detail,
            color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
    }
}

@Composable
fun CPDivider(modifier: Modifier = Modifier) {
    Spacer(modifier.fillMaxWidth().height(1.dp).background(CyberPalette.Divider))
}
