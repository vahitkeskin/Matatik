package com.vahitkeskin.matatik.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vahitkeskin.matatik.ui.theme.LocalGlassPalette
import com.vahitkeskin.matatik.ui.theme.MatatikTheme
import com.vahitkeskin.matatik.ui.theme.ThemeMode

/**
 * iOS tarzı yarı saydam "cam" yüzey. Canvas üzerinde degrade dolgu, üst kenar
 * parlaması (specular highlight) ve ince alfa kenarlığı çizilir.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    contentPadding: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    val glass = LocalGlassPalette.current
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .clip(shape)
            .drawWithCache {
                val fill = Brush.verticalGradient(
                    listOf(
                        glass.fill.copy(alpha = if (glass.isDark) 0.18f else 0.55f),
                        glass.fill.copy(alpha = if (glass.isDark) 0.06f else 0.25f)
                    )
                )
                val highlight = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0.35f), Color.Transparent),
                    startY = 0f,
                    endY = size.height * 0.4f
                )
                onDrawBehind {
                    drawRoundRect(brush = fill, cornerRadius = cornerRadiusPx(cornerRadius))
                    drawRoundRect(brush = highlight, cornerRadius = cornerRadiusPx(cornerRadius))
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                glass.border.copy(alpha = 0.9f),
                                glass.border.copy(alpha = 0.2f)
                            ),
                            start = Offset.Zero,
                            end = Offset(size.width, size.height)
                        ),
                        cornerRadius = cornerRadiusPx(cornerRadius),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.2.dp.toPx())
                    )
                }
            }
            .padding(contentPadding)
    ) {
        content()
    }
}

private fun androidx.compose.ui.unit.Density.cornerRadiusPx(dp: Dp) =
    androidx.compose.ui.geometry.CornerRadius(dp.toPx(), dp.toPx())

@Preview
@Composable
private fun GlassCardLightPreview() {
    MatatikTheme(ThemeMode.LIGHT) {
        Box(Modifier.padding(24.dp)) {
            GlassCard { androidx.compose.material3.Text("Cam Kart — Açık") }
        }
    }
}

@Preview
@Composable
private fun GlassCardDarkPreview() {
    MatatikTheme(ThemeMode.DARK) {
        Box(Modifier.padding(24.dp)) {
            GlassCard { androidx.compose.material3.Text("Cam Kart — Koyu") }
        }
    }
}
