package com.zeroday.cyberprotocol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.zeroday.cyberprotocol.ui.CyberProtocolApp
import com.zeroday.cyberprotocol.ui.theme.CyberProtocolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CyberProtocolTheme { CyberProtocolApp() } }
    }
}
