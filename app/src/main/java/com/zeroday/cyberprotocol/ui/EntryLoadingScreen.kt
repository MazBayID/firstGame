package com.zeroday.cyberprotocol.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/** Entry-only cinematic loader. Frames are decoded one at a time to cap memory use. */
@Composable
fun EntryLoadingScreen(
    ready: Boolean,
    error: String?,
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    var currentBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var frameIndex by remember { mutableIntStateOf(1) }

    LaunchedEffect(ready) {
        while (!ready) {
            val index = frameIndex
            val nextBitmap = withContext(Dispatchers.IO) {
                val name = "loading/frame_${index.toString().padStart(3, '0')}.webp"
                runCatching {
                    context.assets.open(name).use { BitmapFactory.decodeStream(it) }
                }.getOrNull()
            }
            if (nextBitmap != null) {
                val previous = currentBitmap
                currentBitmap = nextBitmap
                frameIndex = if (index >= FRAME_COUNT) 1 else index + 1
                withFrameNanos { }
                previous?.recycle()
            } else {
                frameIndex = if (index >= FRAME_COUNT) 1 else index + 1
            }
            delay(FRAME_DELAY_MS)
        }
    }

    DisposableEffect(Unit) {
        onDispose { currentBitmap?.recycle() }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF05080C)),
        contentAlignment = Alignment.Center
    ) {
        currentBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Animasi pembukaan Cyber Protocol",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 42.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "A.E.G.I.S.  /  INITIALIZING",
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 2.sp
            )
            Text(
                if (error == null) "MENYIAPKAN LINGKUNGAN OPERASI"
                else "INISIALISASI GAGAL",
                color = Color(0xFFB7C4CC),
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                letterSpacing = 1.sp
            )
            if (error != null) {
                Text(
                    error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 11.sp
                )
                Button(onClick = onRetry) { Text("COBA LAGI") }
            }
        }
    }
}

private const val FRAME_COUNT = 240
private const val FRAME_DELAY_MS = 42L
