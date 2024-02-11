package com.example.aesculapius.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aesculapius.data.TestType
import com.example.aesculapius.data.astTest
import com.example.aesculapius.data.recTest
import com.example.aesculapius.ui.tests.ASTTestOnboardingScreen
import com.example.aesculapius.ui.tests.ASTTestResult
import com.example.aesculapius.ui.tests.ASTTestResultScreen
import com.example.aesculapius.ui.tests.MetricsOnboardingScreen
import com.example.aesculapius.ui.tests.MetricsTestScreen
import com.example.aesculapius.ui.tests.RecommendationsOnboardingScreen
import com.example.aesculapius.ui.tests.TestScreen
import com.example.aesculapius.ui.tests.TestsScreen
import com.example.aesculapius.ui.tests.TestsViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun TestsNavigation(
    saveMorningReminder: (LocalDateTime) -> Unit,
    saveEveningReminder:  (LocalDateTime) -> Unit,
    saveASTDate: (LocalDate) -> Unit,
    saveRecommendationDate:  (LocalDate) -> Unit,
    recommendationTestDate: String,
    ASTTestDate: String,
    morningReminder: LocalDateTime,
    eveningReminder: LocalDateTime,
    turnOffBars: () -> Unit,
    modifier: Modifier = Modifier,
    turnOnBars: () -> Unit,
    testsViewModel: TestsViewModel,
    navController: NavHostController
) {
    val summaryScore = testsViewModel.summaryScore.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = TestsScreen.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(route = TestsScreen.route) {
            TestsScreen(
                onClickASTTest = { navController.navigate(ASTTestOnboardingScreen.route) },
                onClickMetricsTest = { navController.navigate(MetricsOnboardingScreen.route) },
                onClickRecTest = { navController.navigate(RecommendationsOnboardingScreen.route) },
                turnOnBars = { turnOnBars() },
                modifier = Modifier.padding(horizontal = 16.dp),
                morningReminder = morningReminder,
                eveningReminder = eveningReminder,
                recommendationTestDate = recommendationTestDate,
                ASTTestDate = ASTTestDate,
                saveEveningReminder = { saveEveningReminder(it) },
                saveMorningReminder = { saveMorningReminder(it) },
                saveASTDate = { saveASTDate(it) },
                saveRecommendationDate = { saveRecommendationDate(it) }
            )
        }
        // не делаем несколько отдельных compopsables, так как экран для двух первых тестов один
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
                    onNavigateBack = { navController.navigateUp() },
                    onClickSummary = {
                        saveASTDate(LocalDate.now().plusMonths(1))
                        testsViewModel.updateSummaryScore(it)
                        navController.navigate(ASTTestResult.route) {
                            popUpTo(TestsScreen.route) { inclusive = false }
                        }
                    },
                    turnOffBars = { turnOffBars() }
                )

                TestType.Recommendations -> TestScreen(
                    testName = "Тест приверженности",
                    questionsList = recTest.listOfQuestion,
                    onNavigateBack = { navController.navigateUp() },
                    onClickSummary = {
                        saveRecommendationDate(LocalDate.now().plusMonths(1))
                        navController.navigate(ASTTestResult.route) {
                            popUpTo(TestsScreen.route) { inclusive = false }
                        }
                    },
                    turnOffBars = { turnOffBars() }
                )

                TestType.Metrics -> MetricsTestScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onClickDoneButton = { first, second, third ->
                        val now = LocalDateTime.now()
                        if (now.isAfter(morningReminder) && now.isBefore(morningReminder.plusMinutes(6))) {
                            saveMorningReminder(now.plusDays(1))
                            testsViewModel.insertNewMetrics(first, second, third)
                        }
                        else {
                            saveEveningReminder(now.plusDays(1))
                            testsViewModel.updateNewMetrics(first, second, third)
                        }
                        navController.navigate(TestsScreen.route) {
                            popUpTo(TestsScreen.route) { inclusive = false }
                        }
                    },
                    turnOffBars = { turnOffBars() }
                )
            }
        }
        composable(route = MetricsOnboardingScreen.route) {
            MetricsOnboardingScreen(
                onNavigateBack = { navController.navigateUp() },
                turnOffBars = { turnOffBars() },
                onClickBeginButton = { navController.navigate("${TestScreen.route}/${TestType.Metrics}") }
            )
        }
        composable(route = ASTTestOnboardingScreen.route) {
            ASTTestOnboardingScreen(
                onNavigateBack = { navController.navigateUp() },
                turnOffBars = { turnOffBars() },
                onClickBeginButton = { navController.navigate("${TestScreen.route}/${TestType.AST}") }
            )
        }
        composable(route = RecommendationsOnboardingScreen.route) {
            RecommendationsOnboardingScreen(
                onNavigateBack = { navController.navigateUp() },
                turnOffBars = { turnOffBars() },
                onClickBeginButton = { navController.navigate("${TestScreen.route}/${TestType.Recommendations}") }
            )
        }
        composable(route = ASTTestResult.route) {
            ASTTestResultScreen(
                onClickReturnButton = { navController.navigate(TestsScreen.route) },
                onNavigateBack = { navController.navigateUp() },
                turnOffBars = { turnOffBars() },
                summaryScore = summaryScore
            )
        }
    }
}