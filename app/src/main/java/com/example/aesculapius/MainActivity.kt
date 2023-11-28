package com.example.aesculapius

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.aesculapius.ui.OnboardingScreen
import com.example.aesculapius.ui.SetReminderTime
import com.example.aesculapius.ui.SignUpScreen
import com.example.aesculapius.ui.StartNavigation
import com.example.aesculapius.ui.theme.AesculapiusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AesculapiusTheme {
                val navController: NavHostController = rememberNavController()
                StartNavigation(navController = navController)
            }
        }
    }
}