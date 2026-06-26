package com.vahitkeskin.matatik.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** Uygulamanın tema modu. CLAUDE.md: Sistem Varsayılan / Açık / Koyu. */
enum class ThemeMode { SYSTEM, LIGHT, DARK }

/** Cam efektleri için temaya bağlı yardımcı renkler. */
data class GlassPalette(
    val fill: Color,
    val border: Color,
    val isDark: Boolean
)

val LocalGlassPalette = staticCompositionLocalOf {
    GlassPalette(MatatikColors.GlassDark, MatatikColors.GlassBorderDark, isDark = true)
}

private val DarkScheme = darkColorScheme(
    primary = MatatikColors.Violet,
    secondary = MatatikColors.Teal,
    tertiary = MatatikColors.Orange,
    background = MatatikColors.DarkBackground,
    surface = MatatikColors.DarkSurface,
    onBackground = MatatikColors.DarkOnBackground,
    onSurface = MatatikColors.DarkOnBackground
)

private val LightScheme = lightColorScheme(
    primary = MatatikColors.Violet,
    secondary = MatatikColors.Teal,
    tertiary = MatatikColors.Orange,
    background = MatatikColors.LightBackground,
    surface = MatatikColors.LightSurface,
    onBackground = MatatikColors.LightOnBackground,
    onSurface = MatatikColors.LightOnBackground
)

/**
 * Ana tema. Renk şeması, cam paleti ve SystemBarController üzerinden sistem
 * barlarını arka planla anlık senkronize eder.
 */
@Composable
fun MatatikTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val dark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    val scheme = if (dark) DarkScheme else LightScheme
    val glass = if (dark) {
        GlassPalette(MatatikColors.GlassDark, MatatikColors.GlassBorderDark, isDark = true)
    } else {
        GlassPalette(MatatikColors.GlassLight, MatatikColors.GlassBorderLight, isDark = false)
    }

    // Sistem barlarını (StatusBar / NavigationBar) arka plan rengiyle eşitle.
    SyncSystemBars(backgroundColor = scheme.background, darkIcons = !dark)

    CompositionLocalProvider(LocalGlassPalette provides glass) {
        MaterialTheme(colorScheme = scheme, content = content)
    }
}

/** Arka plan için dikey degrade üreten yardımcı. */
fun backgroundBrush(dark: Boolean): Brush = if (dark) {
    Brush.verticalGradient(
        listOf(Color(0xFF0B0E1A), Color(0xFF161A2E), Color(0xFF0B0E1A))
    )
} else {
    Brush.verticalGradient(
        listOf(Color(0xFFF1F3FF), Color(0xFFE3E8FF), Color(0xFFF1F3FF))
    )
}
