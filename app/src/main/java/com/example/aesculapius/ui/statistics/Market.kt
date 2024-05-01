package com.example.aesculapius.ui.statistics

import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aesculapius.ui.theme.inverseOnSurface
import com.example.aesculapius.ui.theme.inverseSurface
import com.example.aesculapius.ui.theme.onErrorContainer
import com.example.aesculapius.ui.theme.mediumResultColor
import com.example.aesculapius.ui.theme.outlineVariant
import com.example.aesculapius.ui.theme.surfaceTint
import com.example.aesculapius.ui.theme.tertiaryContainer
import com.patrykandpatrick.vico.compose.component.overlayingComponent
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.core.chart.values.ChartValuesProvider
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.context.DrawContext
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import java.util.Locale

@Composable
internal fun rememberMarker(age: Int, height: Float): Marker {
    val labelBackgroundColor = MaterialTheme.colorScheme.onSecondaryContainer
    val labelBackground = remember(labelBackgroundColor) {
        ShapeComponent(labelBackgroundShape, labelBackgroundColor.toArgb())
    }
    val assets = LocalContext.current
    val customTypeface = Typeface.createFromAsset(assets.assets, "inter_regular.ttf")
    val label = textComponent(
        color = MaterialTheme.colorScheme.onSecondary,
        textSize = 12.sp,
        typeface = customTypeface,
        background = labelBackground,
        padding = dimensionsOf(7.dp)
    )
    val indicatorInnerComponent = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.tertiaryContainer)
    val indicatorCenterComponent = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.inverseSurface)
    val indicatorOuterComponent = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.inverseOnSurface)
    val indicator = overlayingComponent(
        outer = indicatorOuterComponent,
        inner = overlayingComponent(
            outer = indicatorCenterComponent,
            inner = indicatorInnerComponent,
            innerPaddingAll = indicatorInnerAndCenterComponentPaddingValue,
        ),
        innerPaddingAll = indicatorCenterAndOuterComponentPaddingValue,
    )
    val guideline = LineComponent(
        strokeColor = MaterialTheme.colorScheme.secondary.toArgb(),
        color = MaterialTheme.colorScheme.secondary.toArgb(),
        thicknessDp = 2f
    )
    return remember(label, indicator, guideline) {
        object : MarkerComponent(label, indicator, guideline) {
            init {
                labelFormatter = MarkerLabelFormatter { markedEntries, chartValues ->
                    "${"%.1f".format(Locale.US, markedEntries.first().entry.y).toFloat()} л/мин"
                }
                indicatorSizeDp = 15f

            }
            override fun draw(
                context: DrawContext,
                bounds: RectF,
                markedEntries: List<Marker.EntryModel>,
                chartValuesProvider: ChartValuesProvider,
            ) {
                val value = markedEntries.first().entry.y
                onApplyEntryColor = { entryColor ->
                    if (height in 100f .. 110f && value in 24f .. 77f ||
                        height in 110f .. 120f && value in 77f .. 130f ||
                        height in 120f .. 130f && value in 130f ..183f ||
                        height in 130f .. 140f && value in 183f .. 236f ||
                        height in 140f .. 150f && value in 236f .. 289f ||
                        height in 150f .. 160f && value in 289f .. 488f ||
                        height in 160f .. 170f && value in 394f .. 488f ||
                        height in 170f .. 190f && value in 394f .. 450f) {
                        indicatorOuterComponent.color = inverseOnSurface.toArgb()
                        indicatorInnerComponent.color = tertiaryContainer.toArgb()
                        indicatorCenterComponent.color = inverseSurface.toArgb()
                    }
                    else if (height in 100f .. 110f && value in 14f .. 87f ||
                        height in 110f .. 120f && value in 67f .. 140f ||
                        height in 120f .. 130f && value in 120f ..193f ||
                        height in 130f .. 140f && value in 173f .. 246f ||
                        height in 140f .. 150f && value in 226f .. 299f ||
                        height in 150f .. 160f && value in 279f .. 498f ||
                        height in 160f .. 170f && value in 384f .. 498f ||
                        height in 170f .. 190f && value in 384f .. 460f) {
                        indicatorOuterComponent.color = mediumResultColor.toArgb()
                        indicatorInnerComponent.color = tertiaryContainer.toArgb()
                        indicatorCenterComponent.color = surfaceTint.toArgb()
                    }
                    else {
                        indicatorOuterComponent.color = outlineVariant.toArgb()
                        indicatorInnerComponent.color = tertiaryContainer.toArgb()
                        indicatorCenterComponent.color = onErrorContainer.toArgb()
                    }
                }
                super.draw(context, bounds, markedEntries, chartValuesProvider)
            }
        }
    }
}

private val labelBackgroundShape = MarkerCorneredShape(Corner.FullyRounded)
private val indicatorInnerAndCenterComponentPaddingValue = 4.dp
private val indicatorCenterAndOuterComponentPaddingValue = 2.dp
