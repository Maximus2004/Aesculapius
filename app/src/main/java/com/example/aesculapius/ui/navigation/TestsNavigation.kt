package com.example.aesculapius.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.aesculapius.R
import com.example.aesculapius.data.TestType
import com.example.aesculapius.data.astTest
import com.example.aesculapius.data.recTest
import com.example.aesculapius.ui.profile.ProfileEvent
import com.example.aesculapius.ui.signup.SignUpUiState
import com.example.aesculapius.ui.tests.AstTestOnboardingScreen
import com.example.aesculapius.ui.tests.AstTestResult
import com.example.aesculapius.ui.tests.ASTTestResultScreen
import com.example.aesculapius.ui.tests.MetricsOnboardingScreen
import com.example.aesculapius.ui.tests.MetricsTestScreen
import com.example.aesculapius.ui.tests.RecommendationsOnboardingScreen
import com.example.aesculapius.ui.tests.TestScreen
import com.example.aesculapius.ui.tests.TestsEvent
import com.example.aesculapius.ui.tests.TestsScreen
import com.example.aesculapius.ui.tests.TestsViewModel
import java.time.LocalDate
import java.time.LocalDateTime

fun NavGraphBuilder.testsNavGraph(
    onProfileEvent: (ProfileEvent) -> Unit,
    turnOffBars: () -> Unit,
    turnOnBars: () -> Unit,
    testsViewModel: TestsViewModel,
    userUiState: SignUpUiState,
    navController: NavHostController
) {
    composable(route = TestsScreen.route) {
        TestsScreen(
            modifier = Modifier.padding(horizontal = 16.dp),
            morningReminder = userUiState.morningReminder,
            eveningReminder = userUiState.eveningReminder,
            recommendationTestDate = userUiState.recommendationTestDate,
            astTestDate = userUiState.astTestDate,
            onProfileEvent = onProfileEvent,
            onNavigate = navController::navigate
        )
        turnOnBars()
    }
    // не делаем несколько отдельных composables, так как экран для двух первых тестов один
    composable(
        route = TestScreen.routeWithArgs,
        arguments = listOf(navArgument(name = TestScreen.depart) { type = NavType.StringType })
    ) { backStackEntry ->
        val context = LocalContext.current
        val arg = TestType.valueOf(
            backStackEntry.arguments?.getString(TestScreen.depart) ?: TestType.AST.toString()
        )
        when (arg) {
            TestType.AST -> TestScreen(
                testName = context.getString(R.string.ast_test_name),
                questionsList = astTest.listOfQuestion,
                onNavigateBack = { navController.navigateUp() },
                onClickSummary = {
                    onProfileEvent(ProfileEvent.OnSaveAstTestDate(LocalDate.now().plusMonths(1)))
                    testsViewModel.onTestsEvent(TestsEvent.OnUpdateSummaryScore(it))
                    navController.navigate(AstTestResult.route) {
                        popUpTo(TestsScreen.route) { inclusive = false }
                    }
                }
            )

            TestType.Recommendations -> TestScreen(
                testName = context.getString(R.string.rec_test_name),
                questionsList = recTest.listOfQuestion,
                onNavigateBack = { navController.navigateUp() },
                onClickSummary = {
                    onProfileEvent(ProfileEvent.OnSaveRecommendationTestDate(LocalDate.now().plusMonths(1)))
                    navController.navigate(AstTestResult.route) {
                        popUpTo(TestsScreen.route) { inclusive = false }
                    }
                }
            )

            TestType.Metrics -> MetricsTestScreen(
                onNavigateBack = { navController.navigateUp() },
                onProfileEvent = onProfileEvent,
                onTestsEvent = testsViewModel::onTestsEvent,
                navigate = navController::navigate,
                userUiState = userUiState
            )
        }
        turnOffBars()
    }
    composable(route = MetricsOnboardingScreen.route) {
        MetricsOnboardingScreen(
            onNavigateBack = { navController.navigateUp() },
            onClickBeginButton = { navController.navigate("${TestScreen.route}/${TestType.Metrics}") }
        )
        turnOffBars()
    }
    composable(route = AstTestOnboardingScreen.route) {
        AstTestOnboardingScreen(
            onNavigateBack = { navController.navigateUp() },
            onClickBeginButton = { navController.navigate("${TestScreen.route}/${TestType.AST}") }
        )
        turnOffBars()
    }
    composable(route = RecommendationsOnboardingScreen.route) {
        RecommendationsOnboardingScreen(
            onNavigateBack = { navController.navigateUp() },
            onClickBeginButton = { navController.navigate("${TestScreen.route}/${TestType.Recommendations}") }
        )
        turnOffBars()
    }
    composable(route = AstTestResult.route) {
        val summaryScore = testsViewModel.summaryScore.collectAsState().value
        ASTTestResultScreen(
            onClickReturnButton = { navController.navigate(TestsScreen.route) },
            onNavigateBack = { navController.navigateUp() },
            summaryScore = summaryScore
        )
        turnOffBars()
    }
}