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
import com.zeroday.cyberprotocol.core.model.GameLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/** Entry-only cinematic loader. Frames are decoded one at a time to cap memory use. */
@Composable
fun EntryLoadingScreen(
    selectedLanguage: GameLanguage,
    ready: Boolean,
    error: String?,
    onRetry: () -> Unit,
    reducedMotion: Boolean = false
) {
    val context = LocalContext.current
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }
    var currentBitmap by bitmapState
    var frameIndex by remember { mutableIntStateOf(1) }

    LaunchedEffect(ready, reducedMotion) {
        var staticFrameLoaded = false
        while (!ready) {
            if (reducedMotion && staticFrameLoaded) {
                delay(120L)
                continue
            }
            val index = if (reducedMotion) 1 else frameIndex
            val nextBitmap = withContext(Dispatchers.IO) {
                val name = "loading/frame_${index.toString().padStart(3, '0')}.webp"
                runCatching {
                    context.assets.open(name).use { BitmapFactory.decodeStream(it) }
                }.getOrNull()
            }
            if (nextBitmap != null) {
                val previous = currentBitmap
                currentBitmap = nextBitmap
                if (!reducedMotion) frameIndex = if (index >= FRAME_COUNT) 1 else index + 1
                else staticFrameLoaded = true
                withFrameNanos { }
                previous?.recycle()
            } else {
                frameIndex = if (index >= FRAME_COUNT) 1 else index + 1
            }
            delay(if (reducedMotion) 120L else FRAME_DELAY_MS)
        }
    }

    DisposableEffect(Unit) {
        onDispose { bitmapState.value?.recycle() }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF05080C)),
        contentAlignment = Alignment.Center
    ) {
        currentBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = localizedUiText(selectedLanguage, "Cyber Protocol opening animation", "Animasi pembukaan Cyber Protocol"),
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
                if (error == null) localizedUiText(selectedLanguage, "PREPARING OPERATIONS ENVIRONMENT", "MENYIAPKAN LINGKUNGAN OPERASI")
                else localizedUiText(selectedLanguage, "INITIALIZATION FAILED", "INISIALISASI GAGAL"),
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
                Button(onClick = onRetry) { Text(localizedUiText(selectedLanguage, "RETRY", "COBA LAGI")) }
            }
        }
    }
}

private const val FRAME_COUNT = 240
private const val FRAME_DELAY_MS = 42L
