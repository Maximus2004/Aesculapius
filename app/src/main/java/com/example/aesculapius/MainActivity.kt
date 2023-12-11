package com.example.aesculapius

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.aesculapius.data.testsList
import com.example.aesculapius.ui.home.HomeScreen
import com.example.aesculapius.ui.statistics.StatisticsScreen
import com.example.aesculapius.ui.tests.ASTTestScreen
import com.example.aesculapius.ui.tests.RecommendationsTest
import com.example.aesculapius.ui.tests.Test
import com.example.aesculapius.ui.theme.AesculapiusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AesculapiusTheme {
//                val navController: NavHostController = rememberNavController()
//                StartNavigation(navController = navController)
                HomeScreen()
            }
        }
    }
}