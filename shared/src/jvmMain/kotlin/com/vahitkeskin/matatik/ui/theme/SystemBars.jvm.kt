package com.vahitkeskin.matatik.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Desktop (JVM): sistem barı kavramı yoktur; no-op. */
@Composable
actual fun SyncSystemBars(backgroundColor: Color, darkIcons: Boolean) {
    // Masaüstünde pencere dekorasyonu işletim sistemine aittir.
}
