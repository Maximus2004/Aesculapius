package com.example.aesculapius.ui.statistics

import android.graphics.RectF
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aesculapius.ui.theme.inverseOnSurface
import com.example.aesculapius.ui.theme.inverseSurface
import com.example.aesculapius.ui.theme.onErrorContainer
import com.example.aesculapius.ui.theme.onSurface
import com.example.aesculapius.ui.theme.outlineVariant
import com.example.aesculapius.ui.theme.surfaceTint
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
        strokeColor = MaterialTheme.colorScheme.primary.toArgb(),
        color = MaterialTheme.colorScheme.primary.toArgb(),
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
                        guideline.strokeColor = outlineVariant.toArgb()
                        guideline.color = onErrorContainer.toArgb()
                    }
                    else if (value in 20f .. 24f) {
                        guideline.strokeColor = onSurface.toArgb()
                        guideline.color = surfaceTint.toArgb()
                    }
                    else {
                        guideline.strokeColor = inverseOnSurface.toArgb()
                        guideline.color = inverseSurface.toArgb()
                    }
                }
                super.draw(context, bounds, markedEntries, chartValuesProvider)
            }
        }
    }
}