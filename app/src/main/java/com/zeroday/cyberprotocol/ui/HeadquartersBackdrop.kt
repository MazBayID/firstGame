package com.zeroday.cyberprotocol.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

/**
 * Lightweight procedural A.E.G.I.S. headquarters environment.
 * The geometry is intentionally deterministic and uses no decoded bitmap assets.
 */
@Composable
internal fun HeadquartersBackdrop(modifier: Modifier = Modifier) {
    Canvas(modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val horizon = h * 0.55f

        drawRect(
            Brush.verticalGradient(
                listOf(Color(0xFF061722), Color(0xFF0A2D39), Color(0xFF07131C)),
                startY = 0f,
                endY = h
            ),
            size = size
        )

        // Distant city silhouettes.
        val skyline = listOf(
            Triple(0.00f, 0.25f, 0.22f),
            Triple(0.13f, 0.17f, 0.31f),
            Triple(0.28f, 0.21f, 0.25f),
            Triple(0.72f, 0.20f, 0.28f),
            Triple(0.86f, 0.14f, 0.33f)
        )
        skyline.forEachIndexed { index, building ->
            val left = w * building.first
            val width = w * building.second
            val top = horizon - h * building.third
            drawRect(
                color = if (index % 2 == 0) Color(0xFF0A202B) else Color(0xFF0B2632),
                topLeft = Offset(left, top),
                size = Size(width, h - top)
            )
            for (row in 0..4) {
                val windowY = top + h * (0.035f + row * 0.038f)
                for (column in 0..2) {
                    val windowX = left + width * (0.16f + column * 0.25f)
                    drawRect(
                        color = Color(0xFF3E8994).copy(alpha = if ((row + column + index) % 3 == 0) 0.48f else 0.18f),
                        topLeft = Offset(windowX, windowY),
                        size = Size(w * 0.008f, h * 0.012f)
                    )
                }
            }
        }

        // The closed central headquarters: a narrow command tower over a fortified base.
        val tower = Path().apply {
            moveTo(w * 0.405f, horizon)
            lineTo(w * 0.435f, h * 0.18f)
            lineTo(w * 0.565f, h * 0.18f)
            lineTo(w * 0.595f, horizon)
            close()
        }
        drawPath(tower, Color(0xFF0A202B))
        drawPath(tower, Color(0xFF2E6470).copy(alpha = 0.62f), style = Stroke(width = 1.4f))

        val centralBase = Path().apply {
            moveTo(w * 0.30f, h * 0.46f)
            lineTo(w * 0.70f, h * 0.46f)
            lineTo(w * 0.78f, h * 0.66f)
            lineTo(w * 0.22f, h * 0.66f)
            close()
        }
        drawPath(centralBase, Color(0xFF081B25))
        drawPath(centralBase, Color(0xFF285763).copy(alpha = 0.78f), style = Stroke(width = 1.6f))

        // Reinforced side wings and the recessed central entrance.
        drawRect(Color(0xFF0A202A), Offset(w * 0.19f, h * 0.52f), Size(w * 0.62f, h * 0.12f))
        drawRect(Color(0xFF07131C), Offset(w * 0.455f, h * 0.535f), Size(w * 0.09f, h * 0.125f))
        drawRect(Color(0xFF3B8792).copy(alpha = 0.72f), Offset(w * 0.475f, h * 0.565f), Size(w * 0.05f, h * 0.095f))

        // Narrow lit windows on the command tower.
        for (row in 0..7) {
            val y = h * (0.235f + row * 0.027f)
            drawRect(
                Color(0xFF4CA3AD).copy(alpha = if (row % 3 == 0) 0.68f else 0.34f),
                Offset(w * 0.487f, y),
                Size(w * 0.026f, h * 0.009f)
            )
        }

        // Perimeter lights and controlled access line.
        drawLine(
            Color(0xFF3A8994).copy(alpha = 0.48f),
            Offset(w * 0.08f, h * 0.69f),
            Offset(w * 0.92f, h * 0.69f),
            strokeWidth = 1.2f
        )
        for (index in 0..10) {
            val x = w * (0.11f + index * 0.078f)
            drawCircle(Color(0xFF65B6BF).copy(alpha = 0.58f), radius = w * 0.0028f, center = Offset(x, h * 0.69f))
        }

        // Approach road converging on the secured entrance.
        val road = Path().apply {
            moveTo(w * 0.38f, h)
            lineTo(w * 0.47f, h * 0.66f)
            lineTo(w * 0.53f, h * 0.66f)
            lineTo(w * 0.62f, h)
            close()
        }
        drawPath(road, Color(0xFF08131A).copy(alpha = 0.9f))
        drawLine(Color(0xFF356B75).copy(alpha = 0.34f), Offset(w * 0.38f, h), Offset(w * 0.47f, h * 0.66f), strokeWidth = 1.2f)
        drawLine(Color(0xFF356B75).copy(alpha = 0.34f), Offset(w * 0.62f, h), Offset(w * 0.53f, h * 0.66f), strokeWidth = 1.2f)

        // Subtle atmospheric scrim keeps the menu readable over the environment.
        drawRect(
            Brush.verticalGradient(
                listOf(Color(0xAA06121B), Color(0x33101E28), Color(0xCC050B10)),
                startY = 0f,
                endY = h
            ),
            size = size
        )
    }
}
