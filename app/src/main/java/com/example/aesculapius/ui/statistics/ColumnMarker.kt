package com.example.aesculapius.ui.statistics

import android.graphics.RectF
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.core.chart.values.ChartValuesProvider
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.context.DrawContext
import com.patrykandpatrick.vico.core.extension.set
import com.patrykandpatrick.vico.core.marker.Marker

@Composable
internal fun rememberColumnMarker(): Marker {
    val label = textComponent(color = Color.Transparent, textSize = 0.sp)
    val indicator = null
    val guideline = LineComponent(
        strokeColor = Color(0xFF7FDACC).toArgb(),
        color = Color(0xFF00BB9A).toArgb(),
        thicknessDp = 9f,
        strokeWidthDp = 1f,
        shape = Shapes.roundedCornerShape(
            topLeftPercent = 35,
            topRightPercent = 35,
            bottomLeftPercent = 0,
            bottomRightPercent = 0
        )
    )
    return remember(label, indicator, guideline) {
        object : MarkerComponent(label, indicator, guideline) {
            override fun draw(
                context: DrawContext,
                bounds: RectF,
                markedEntries: List<Marker.EntryModel>,
                chartValuesProvider: ChartValuesProvider,
            ) {
                bounds.set(top = markedEntries.first().location.y, bottom = bounds.bottom, left = bounds.left, right = bounds.right)

                val value = markedEntries.first().entry.y
                onApplyEntryColor = { entryColor ->
                    if (value <= 19) {
                        guideline.strokeColor = Color(0xFFFD9AB4).toArgb()
                        guideline.color = Color(0xFFFC3B69).toArgb()
                    }
                    else if (value in 20f .. 24f) {
                        guideline.strokeColor = Color(0xFFF9DC80).toArgb()
                        guideline.color = Color(0xFFF3BE00).toArgb()
                    }
                    else {
                        guideline.strokeColor = Color(0xFF7FDACC).toArgb()
                        guideline.color = Color(0xFF00BB9A).toArgb()
                    }
                }
                super.draw(context, bounds, markedEntries, chartValuesProvider)
            }
        }
    }
}