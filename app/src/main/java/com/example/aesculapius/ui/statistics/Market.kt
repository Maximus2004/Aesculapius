package com.example.aesculapius.ui.statistics

import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
internal fun rememberMarker(age: Int, height: Int): Marker {
    val labelBackgroundColor = Color(0x99E7E0F1)
    val labelBackground = remember(labelBackgroundColor) {
        ShapeComponent(labelBackgroundShape, labelBackgroundColor.toArgb())
    }
    val assets = LocalContext.current
    val customTypeface = Typeface.createFromAsset(assets.assets, "inter_regular.ttf")
    val label = textComponent(
        color = Color.Black,
        textSize = 12.sp,
        typeface = customTypeface,
        background = labelBackground,
        padding = dimensionsOf(7.dp)
    )
    val indicatorInnerComponent = shapeComponent(Shapes.pillShape, Color.White)
    val indicatorCenterComponent = shapeComponent(Shapes.pillShape, Color(0xFF00BB9A))
    val indicatorOuterComponent = shapeComponent(Shapes.pillShape, Color(0xFF7FDACC))
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
        strokeColor = Color(0xFFB0A3D1).toArgb(),
        color = Color(0xFFB0A3D1).toArgb(),
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
                    if (height in 100 .. 110 && value in 24f .. 77f ||
                        height in 110 .. 120 && value in 77f .. 130f ||
                        height in 120 .. 130 && value in 130f ..183f ||
                        height in 130 .. 140 && value in 183f .. 236f ||
                        height in 140 .. 150 && value in 236f .. 289f ||
                        height in 150 .. 160 && value in 289f .. 488f ||
                        height in 160 .. 170 && value in 394f .. 488f ||
                        height in 170 .. 190 && value in 394f .. 450f) {
                        indicatorOuterComponent.color = Color(0xFF7FDACC).toArgb()
                        indicatorInnerComponent.color = Color.White.toArgb()
                        indicatorCenterComponent.color = Color(0xFF00BB9A).toArgb()
                    }
                    else if (height in 100 .. 110 && value in 14f .. 87f ||
                        height in 110 .. 120 && value in 67f .. 140f ||
                        height in 120 .. 130 && value in 120f ..193f ||
                        height in 130 .. 140 && value in 173f .. 246f ||
                        height in 140 .. 150 && value in 226f .. 299f ||
                        height in 150 .. 160 && value in 279f .. 498f ||
                        height in 160 .. 170 && value in 384f .. 498f ||
                        height in 170 .. 190 && value in 384f .. 460f) {
                        indicatorOuterComponent.color = Color(0xFFF9DC80).toArgb()
                        indicatorInnerComponent.color = Color.White.toArgb()
                        indicatorCenterComponent.color = Color(0xFFF3BE00).toArgb()
                    }
                    else {
                        indicatorOuterComponent.color = Color(0xFFFD9AB4).toArgb()
                        indicatorInnerComponent.color = Color.White.toArgb()
                        indicatorCenterComponent.color = Color(0xFFFC3B69).toArgb()
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
