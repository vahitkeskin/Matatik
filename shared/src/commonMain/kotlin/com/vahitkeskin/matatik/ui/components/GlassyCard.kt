package com.vahitkeskin.matatik.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vahitkeskin.matatik.ui.theme.LocalIsDarkTheme
import com.vahitkeskin.matatik.ui.theme.AppColors

/** Yeniden kullanılabilir glassmorphic kart: koyu temada blur + parlak kenarlık katmanları. */
@Composable
fun GlassyCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val cardModifier = modifier.clip(RoundedCornerShape(cornerRadius))

    Box(
        modifier = cardModifier
    ) {
        // Arka plan katmanı
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        if (isDark) {
                            listOf(
                                Color.White.copy(alpha = 0.16f), // Koyu temada kontrast için biraz daha yüksek alpha
                                Color.White.copy(alpha = 0.06f)
                            )
                        } else {
                            listOf(
                                Color.White,
                                Color.White
                            )
                        }
                    )
                )
                .then(if (isDark) Modifier.blur(20.dp) else Modifier)
        )

        // Parlak kenarlık katmanı (yalnızca koyu temada; açık temada iç gölge sarmasını önler)
        if (isDark) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .drawWithContent {
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.35f),
                                    Color.White.copy(alpha = 0.05f)
                                )
                            ),
                            cornerRadius = CornerRadius(cornerRadius.toPx()),
                            style = Stroke(width = 1.dp.toPx())
                        )
                    }
            )
        }

        // Okunabilir içerik sarmalayıcı (blur uygulanmaz)
        Box(
            modifier = Modifier
                .padding(16.dp),
            contentAlignment = Alignment.Center,
            content = content
        )
    }
}

/** Yeniden kullanılabilir glassmorphic buton: degrade kenarlıklı, hap (pill) biçimli yüzey. */
@Composable
fun GlassyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable { onClick() },
        color = if (isDark) Color.White.copy(alpha = 0.1f) else AppColors.Slate100.copy(alpha = 0.85f),
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(
                if (isDark) {
                    listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.05f)
                    )
                } else {
                    listOf(
                        AppColors.Slate300.copy(alpha = 0.6f),
                        AppColors.Slate400.copy(alpha = 0.2f)
                    )
                }
            )
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            content = content
        )
    }
}
