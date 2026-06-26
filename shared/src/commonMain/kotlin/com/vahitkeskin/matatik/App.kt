package com.vahitkeskin.matatik

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vahitkeskin.matatik.core.localization.Language
import com.vahitkeskin.matatik.ui.solver.SolverScreen
import com.vahitkeskin.matatik.ui.splash.SplashScreen
import com.vahitkeskin.matatik.ui.theme.MatatikTheme
import com.vahitkeskin.matatik.ui.theme.ThemeMode

/**
 * Uygulamanın kök composable'ı. Tema sarmalayıcısını uygular, özel Canvas
 * splash'ından ana çözücü ekranına geçişi yönetir.
 */
@Composable
@Preview
fun App() {
    val themeMode = ThemeMode.SYSTEM
    MatatikTheme(themeMode) {
        val dark = when (themeMode) {
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
        }
        var showSplash by remember { mutableStateOf(true) }

        AnimatedVisibility(
            visible = !showSplash,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            SolverScreen(darkTheme = dark)
        }

        AnimatedVisibility(
            visible = showSplash,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            SplashScreen(
                language = Language.DEFAULT,
                onFinished = { showSplash = false }
            )
        }
    }
}
