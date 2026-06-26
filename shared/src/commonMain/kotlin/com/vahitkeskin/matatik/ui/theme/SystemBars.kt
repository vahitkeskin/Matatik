package com.vahitkeskin.matatik.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Sistem barlarını (StatusBar / NavigationBar) uygulama arka planıyla senkronize eder.
 * Edge-to-edge modda barlar saydamdır; bu fonksiyon yalnızca ikon kontrastını ayarlar.
 */
@Composable
expect fun SyncSystemBars(backgroundColor: Color, darkIcons: Boolean)
