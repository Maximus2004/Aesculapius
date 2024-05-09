package com.example.aesculapius.ui.tests

sealed interface TestsEvent {
    data class OnInsertNewMetrics(val first: Float, val second: Float, val third: Float): TestsEvent
    data class OnUpdateNewMetrics(val first: Float, val second: Float, val third: Float): TestsEvent
    data class OnUpdateSummaryScore(val score: Int, val isAstTest: Boolean): TestsEvent
}