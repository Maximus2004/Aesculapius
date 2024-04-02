package com.example.aesculapius.ui.statistics

import androidx.annotation.StringRes
import com.example.aesculapius.R

data class GraphicTypeContent(
    val graphicTypes: GraphicTypes = GraphicTypes.Week,
    @StringRes
    val nameOfType: Int = R.string.week_chart
)
