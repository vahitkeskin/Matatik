package com.vahitkeskin.matatik.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * iOS: status bar stili UIViewController düzeyinde yönetilir.
 * Compose tarafında ek bir işlem gerekmez; no-op.
 */
@Composable
actual fun SyncSystemBars(backgroundColor: Color, darkIcons: Boolean) {
    // iOS status bar görünümü Info.plist / UIViewController üzerinden kontrol edilir.
}
