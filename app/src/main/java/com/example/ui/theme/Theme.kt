package com.example.ui.theme

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
    primary = RosePrimaryDark,
    onPrimary = RoseOnPrimaryDark,
    primaryContainer = RosePrimaryContainerDark,
    onPrimaryContainer = RoseOnPrimaryContainerDark,
    secondary = RoseSecondaryDark,
    onSecondary = RoseOnSecondaryDark,
    secondaryContainer = RoseSecondaryContainerDark,
    onSecondaryContainer = RoseOnSecondaryContainerDark,
    tertiary = MauveTertiaryDark,
    onTertiary = MauveOnTertiaryDark,
    tertiaryContainer = MauveTertiaryContainerDark,
    onTertiaryContainer = MauveOnTertiaryContainerDark,
    background = NeutralBackgroundDark,
    onBackground = NeutralOnBackgroundDark,
    surface = NeutralSurfaceDark,
    onSurface = NeutralOnSurfaceDark,
    surfaceVariant = NeutralSurfaceVariantDark,
    onSurfaceVariant = NeutralOnSurfaceVariantDark,
    outline = NeutralOutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = RosePrimary,
    onPrimary = RoseOnPrimary,
    primaryContainer = RosePrimaryContainer,
    onPrimaryContainer = RoseOnPrimaryContainer,
    secondary = RoseSecondary,
    onSecondary = RoseOnSecondary,
    secondaryContainer = RoseSecondaryContainer,
    onSecondaryContainer = RoseOnSecondaryContainer,
    tertiary = MauveTertiary,
    onTertiary = MauveOnTertiary,
    tertiaryContainer = MauveTertiaryContainer,
    onTertiaryContainer = MauveOnTertiaryContainer,
    background = NeutralBackground,
    onBackground = NeutralOnBackground,
    surface = NeutralSurface,
    onSurface = NeutralOnSurface,
    surfaceVariant = NeutralSurfaceVariant,
    onSurfaceVariant = NeutralOnSurfaceVariant,
    outline = NeutralOutline
)

@Composable
fun LunaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use intentional wellness palette by default
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
