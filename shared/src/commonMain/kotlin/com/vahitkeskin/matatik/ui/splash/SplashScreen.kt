package com.vahitkeskin.matatik.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vahitkeskin.matatik.core.localization.Localization
import com.vahitkeskin.matatik.core.localization.Language
import com.vahitkeskin.matatik.ui.theme.MatatikColors
import com.vahitkeskin.matatik.ui.theme.MatatikTheme
import com.vahitkeskin.matatik.ui.theme.ThemeMode
import com.vahitkeskin.matatik.ui.theme.backgroundBrush
import kotlin.math.cos
import kotlin.math.sin

/**
 * Tamamen Canvas ile çizilmiş, launcher-ikon içermeyen özel açılış ekranı.
 * Dönen yörünge halkaları, atan çekirdek ve sırayla beliren operatör sembolleri
 * çizer; animasyon bitince [onFinished] çağrılır.
 */
@Composable
fun SplashScreen(
    language: Language = Language.DEFAULT,
    onFinished: () -> Unit = {}
) {
    val sweep = remember { Animatable(0f) }
    val pulse = remember { Animatable(0f) }
    val reveal = remember { Animatable(0f) }
    val strings = Localization[language]

    LaunchedEffect(Unit) {
        reveal.animateTo(1f, tween(700, easing = LinearEasing))
    }
    LaunchedEffect(Unit) {
        sweep.animateTo(1f, tween(1600, easing = LinearEasing))
    }
    LaunchedEffect(Unit) {
        pulse.animateTo(1f, tween(1800))
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush(dark = true)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Canvas(modifier = Modifier.size(160.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.minDimension / 2f

                // Dış dönen halka
                rotate(degrees = sweep.value * 360f) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(MatatikColors.Violet, MatatikColors.Blue, MatatikColors.Teal, MatatikColors.Violet)
                        ),
                        startAngle = 0f,
                        sweepAngle = 300f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius * 0.92f, center.y - radius * 0.92f),
                        size = androidx.compose.ui.geometry.Size(radius * 1.84f, radius * 1.84f),
                        style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // İç ters dönen halka
                rotate(degrees = -sweep.value * 270f) {
                    drawArc(
                        color = MatatikColors.Teal.copy(alpha = 0.7f),
                        startAngle = 40f,
                        sweepAngle = 200f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius * 0.62f, center.y - radius * 0.62f),
                        size = androidx.compose.ui.geometry.Size(radius * 1.24f, radius * 1.24f),
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Atan çekirdek
                val coreRadius = radius * (0.20f + 0.06f * sin(pulse.value * 6.283f))
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(MatatikColors.Violet, MatatikColors.Blue.copy(alpha = 0.2f)),
                        center = center,
                        radius = coreRadius * 2f
                    ),
                    radius = coreRadius,
                    center = center
                )

                // Yörüngedeki operatör noktaları (+, −, ×, ÷ konumları)
                val orbit = radius * 0.78f
                repeat(4) { i ->
                    val angle = (sweep.value * 360f + i * 90f) * 3.14159f / 180f
                    val px = center.x + orbit * cos(angle)
                    val py = center.y + orbit * sin(angle)
                    drawCircle(
                        color = Color.White.copy(alpha = 0.85f),
                        radius = 4.dp.toPx(),
                        center = Offset(px, py)
                    )
                }
            }

            Spacer(Modifier.height(28.dp))
            Text(
                text = strings.appName,
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(reveal.value)
            )
            Text(
                text = strings.solverTitle,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier.alpha(reveal.value)
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    MatatikTheme(ThemeMode.DARK) {
        SplashScreen()
    }
}
