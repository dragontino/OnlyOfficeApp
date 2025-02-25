package com.onlyoffice.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontSize = 22.sp,
        fontFamily = FontFamily.Default,
        lineHeight = 27.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily.Default,
        lineHeight = 22.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Default,
        lineHeight = 17.sp,
    ),

    titleLarge = TextStyle(
        fontFamily = FontFamily(RobotoFont),
        fontSize = 24.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(RobotoFont),
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.2.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(RobotoFont),
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp
    ),

    labelLarge = TextStyle(
        fontFamily = FontFamily(RobotoFont),
        fontSize = 26.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(RobotoFont),
        fontSize = 22.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily(RobotoFont),
        fontSize = 18.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = FontFamily(RobotoFont),
        fontSize = 28.sp
    )
)