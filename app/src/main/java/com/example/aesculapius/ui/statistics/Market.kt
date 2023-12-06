package com.example.aesculapius.ui.statistics

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
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import java.util.Locale

@Composable
internal fun rememberMarker(): Marker {
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
                onApplyEntryColor = { entryColor ->
                    indicatorOuterComponent.color = Color(0xFF7FDACC).toArgb()
                    indicatorInnerComponent.color = Color.White.toArgb()
                    indicatorCenterComponent.color = Color(0xFF00BB9A).toArgb()
                }
            }
        }
    }
}

private val labelBackgroundShape = MarkerCorneredShape(Corner.FullyRounded)
private val labelHorizontalPaddingValue = 4.dp
private val labelVerticalPaddingValue = 4.dp
private val labelPadding = dimensionsOf(labelHorizontalPaddingValue, labelVerticalPaddingValue)
private val indicatorInnerAndCenterComponentPaddingValue = 4.dp
private val indicatorCenterAndOuterComponentPaddingValue = 2.dp
