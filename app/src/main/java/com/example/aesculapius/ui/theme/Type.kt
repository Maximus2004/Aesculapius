package com.example.aesculapius.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.aesculapius.R

val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_extra_bold, FontWeight.ExtraBold),
    Font(R.font.inter_light, FontWeight.Light),
    Font(R.font.inter_black, FontWeight.Black)
)

val Roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.W400),
    Font(R.font.roboto_bold, FontWeight.W600),
    Font(R.font.roboto_medium, FontWeight.W500)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp,
        color = Color.Black
    ),
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 27.sp,
        lineHeight = 30.26.sp,
        letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 18.sp,
        lineHeight = 22.78.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 18.sp,
        lineHeight = 21.78.sp,
        color = Color.Black,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W500,
        fontSize = 20.sp,
        lineHeight = 23.44.sp,
        color = Color.Black
    ),
    displaySmall = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = Color.White,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W500,
        fontSize = 24.sp,
        lineHeight = 28.13.sp,
        color = Color.Black
    ),
    titleSmall = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 18.75.sp,
        color = Color(0xFF79747E)
    ),
    titleLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W400,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        color = Color.Black
    ),
    labelSmall = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp,
        color = Color(0xFF49454F)
    ),
    displayLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W400,
        fontSize = 45.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        color = Color.Black
    ),
    headlineLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        color = onSecondary
    )
)