package com.routinealarm.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Cyan8, // button background
    onPrimary = White, // button text
    onSecondary = White, // all text
    onTertiary = Cyan3, // text variant
    primaryContainer = Grey8, // card and floating button background
    inversePrimary = Grey5, // disabled Text
    secondaryContainer = Grey7, // options background
    tertiaryContainer = Grey8, // edit box background
    onSurface = White, // topBar and card and all text
    inverseOnSurface = White, // Floating button and dialog text
    outlineVariant = White, // Edit text
)

private val LightColorScheme = lightColorScheme(
    primary = Magenta5, // button background
    onPrimary = White, // button text
    onSecondary = Black, // all text
    onTertiary = Magenta5, // text variant
    primaryContainer = Grey1, // card and floating button background
    inversePrimary = Grey3, // disabled Text
    secondaryContainer = Grey0, // options background
    tertiaryContainer = Grey2, // edit box background
    onSurface = Black, // topBar and card and all text
    inverseOnSurface = Magenta5, // Floating button and dialog text
    outlineVariant = Black, // Edit text
)

@Composable
fun RoutineAlarmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}