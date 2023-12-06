package com.example.aesculapius.ui.statistics

import androidx.lifecycle.ViewModel
import com.example.aesculapius.ui.home.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StatisticsViewModel() : ViewModel() {
    private val _staticsUiState = MutableStateFlow(GraphicTypeContent())
    val staticsUiState: StateFlow<GraphicTypeContent> = _staticsUiState

    fun updateCurrentNavType(typeContent: GraphicTypeContent) {
        _staticsUiState.update { typeContent }
    }
}