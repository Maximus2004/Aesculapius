package com.example.aesculapius.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aesculapius.data.Hours
import com.example.aesculapius.data.TestType
import com.example.aesculapius.data.astTest
import com.example.aesculapius.data.recTest
import com.example.aesculapius.ui.start.OnboardingScreen
import com.example.aesculapius.ui.start.SetReminderTime
import com.example.aesculapius.ui.start.SignUpScreen
import com.example.aesculapius.ui.tests.ASTTestResult
import com.example.aesculapius.ui.tests.MetricsScreen
import com.example.aesculapius.ui.tests.TestScreen
import com.example.aesculapius.ui.tests.TestsScreen
import com.example.aesculapius.ui.tests.TestsViewModel
import java.time.format.DateTimeFormatter

@Composable
fun TestsNavigation(
    turnOffBars: () -> Unit,
    modifier: Modifier = Modifier,
    turnOnBars: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val testsViewModel: TestsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = TestsScreen.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(route = TestsScreen.route) {
            TestsScreen(
                onClickASTTest = {
                    turnOffBars()
                    navController.navigate("${TestScreen.route}/${TestType.AST}")
                },
                onClickMetricsTest = {
                    turnOffBars()
                    navController.navigate("${TestScreen.route}/${TestType.Metrics}")
                },
                onClickRecTest = {
                    turnOffBars()
                    navController.navigate("${TestScreen.route}/${TestType.Recommendations}")
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        composable(
            route = TestScreen.routeWithArgs,
            arguments = listOf(navArgument(name = TestScreen.depart) { type = NavType.StringType })
        ) { backStackEntry ->
            val arg = TestType.valueOf(
                backStackEntry.arguments?.getString(TestScreen.depart) ?: TestType.AST.toString()
            )
            when (arg) {
                TestType.AST -> TestScreen(
                    testName = "АСТ тестирование",
                    questionsList = astTest.listOfQuestion,
                    onNavigateBack = {
                        navController.navigateUp()
                        turnOnBars()
                    },
                    onClickSummary = { navController.navigate(ASTTestResult.route) }
                )

                TestType.Recommendations -> TestScreen(
                    testName = "Тест приверженности",
                    questionsList = recTest.listOfQuestion,
                    onNavigateBack = {
                        navController.navigateUp()
                        turnOnBars()
                    },
                    onClickSummary = { navController.navigate(ASTTestResult.route) }
                )

                TestType.Metrics -> MetricsScreen()
            }
        }
        composable(route = ASTTestResult.route) {
            ASTTestResult(onClickReturnButton = {
                navController.navigate(TestsScreen.route)
                turnOnBars()
            })
        }
    }
}