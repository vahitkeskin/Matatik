package com.vahitkeskin.matatik.ui.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Android: edge-to-edge altında bar ikonlarının açık/koyu kontrastını arka plana göre ayarlar.
 * Deprecated `window.statusBarColor` API'leri yerine WindowInsetsController kullanılır.
 */
@Composable
actual fun SyncSystemBars(backgroundColor: Color, darkIcons: Boolean) {
    val view = LocalView.current
    if (view.isInEditMode) return
    SideEffect {
        val activity = view.context as? Activity ?: return@SideEffect
        val controller = WindowCompat.getInsetsController(activity.window, view)
        controller.isAppearanceLightStatusBars = darkIcons
        controller.isAppearanceLightNavigationBars = darkIcons
    }
}
