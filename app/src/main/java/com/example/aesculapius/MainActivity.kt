package com.example.aesculapius

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.aesculapius.ui.home.HomeScreen
import com.example.aesculapius.ui.navigation.MainScreen
import com.example.aesculapius.ui.theme.AesculapiusTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContent {
            AesculapiusTheme {
                HomeScreen()
            }
        }
    }
}