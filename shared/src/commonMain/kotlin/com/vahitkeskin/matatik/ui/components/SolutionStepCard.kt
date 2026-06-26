package com.vahitkeskin.matatik.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vahitkeskin.matatik.core.domain.model.HighlightedCoordinate
import com.vahitkeskin.matatik.core.domain.model.SolutionStep
import com.vahitkeskin.matatik.ui.math.LatexDisplay
import com.vahitkeskin.matatik.ui.theme.MatatikColors
import com.vahitkeskin.matatik.ui.theme.MatatikTheme
import com.vahitkeskin.matatik.ui.theme.ThemeMode

/**
 * Tek bir çözüm adımını cam kart içinde gösterir: numara rozeti, kural başlığı,
 * lokalize açıklama, kullanılan formül ve adım sonu ifadesi (LatexDisplay ile).
 * Kademeli (staggered) giriş animasyonuna sahiptir.
 */
@Composable
fun SolutionStepCard(
    step: SolutionStep,
    ruleDescription: String,
    indexInList: Int,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 420)
    )
    LaunchedEffect(step.stepNumber) { visible = true }

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .alpha(progress)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                StepBadge(step.stepNumber)
                Spacer(Modifier.size(12.dp))
                Text(
                    text = step.ruleApplied,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (ruleDescription.isNotBlank()) {
                Text(
                    text = ruleDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            if (step.formulaUsedLatex.isNotBlank()) {
                FormulaChip(LatexDisplay.render(step.formulaUsedLatex))
            }

            // Adım sonu ifadesi — vurgulu renkle
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .drawBehind {
                        drawRoundRect(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    primaryHighlight(step.highlightedParts).copy(alpha = 0.18f),
                                    Color.Transparent
                                )
                            )
                        )
                    }
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = LatexDisplay.render(step.currentExpressionLatex),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun StepBadge(number: Int) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .drawBehind {
                drawCircle(
                    brush = Brush.linearGradient(
                        listOf(MatatikColors.Violet, MatatikColors.Blue)
                    )
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$number",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun FormulaChip(text: String) {
    Box(
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .drawBehind {
                drawRoundRect(color = MatatikColors.Teal.copy(alpha = 0.14f))
            }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontFamily = FontFamily.Monospace,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

private fun primaryHighlight(parts: List<HighlightedCoordinate>): Color {
    val hex = parts.firstOrNull()?.hexColor ?: "#7C4DFF"
    return parseHexColor(hex)
}

/** "#RRGGBB" veya "#AARRGGBB" → Color. */
fun parseHexColor(hex: String): Color {
    val clean = hex.removePrefix("#")
    val value = clean.toLong(16)
    return when (clean.length) {
        6 -> Color(0xFF000000L or value)
        8 -> Color(value)
        else -> Color(0xFF7C4DFF)
    }
}

@Preview
@Composable
private fun SolutionStepCardLightPreview() {
    MatatikTheme(ThemeMode.LIGHT) {
        Box(Modifier.padding(16.dp)) {
            SolutionStepCard(
                step = sampleStep(),
                ruleDescription = "İki taraf da katsayıya bölünür.",
                indexInList = 0
            )
        }
    }
}

@Preview
@Composable
private fun SolutionStepCardDarkPreview() {
    MatatikTheme(ThemeMode.DARK) {
        Box(Modifier.padding(16.dp)) {
            SolutionStepCard(
                step = sampleStep(),
                ruleDescription = "İki taraf da katsayıya bölünür.",
                indexInList = 0
            )
        }
    }
}

private fun sampleStep() = SolutionStep(
    stepNumber = 3,
    ruleApplied = "İki Tarafı Böl",
    descriptionLocalizationKey = "rules.linear.isolate_variable",
    formulaUsedLatex = "ax = C \\Rightarrow x = \\frac{C}{a}",
    currentExpressionLatex = "x = \\frac{4}{2}",
    highlightedParts = listOf(HighlightedCoordinate("2", "#2979FF", "GLOW"))
)
