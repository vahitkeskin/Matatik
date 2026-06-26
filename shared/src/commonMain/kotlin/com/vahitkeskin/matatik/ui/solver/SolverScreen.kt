package com.vahitkeskin.matatik.ui.solver

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vahitkeskin.matatik.core.AppVersion
import com.vahitkeskin.matatik.core.domain.model.MathematicalSolution
import com.vahitkeskin.matatik.core.localization.Localization
import com.vahitkeskin.matatik.presentation.solver.SolverIntent
import com.vahitkeskin.matatik.presentation.solver.SolverState
import com.vahitkeskin.matatik.presentation.solver.SolverViewModel
import com.vahitkeskin.matatik.ui.components.GlassButton
import com.vahitkeskin.matatik.ui.components.GlassCard
import com.vahitkeskin.matatik.ui.components.SolutionStepCard
import com.vahitkeskin.matatik.ui.math.LatexDisplay
import com.vahitkeskin.matatik.ui.theme.MatatikColors
import com.vahitkeskin.matatik.ui.theme.MatatikTheme
import com.vahitkeskin.matatik.ui.theme.ThemeMode
import com.vahitkeskin.matatik.ui.theme.backgroundBrush

/**
 * Çözücü ekranı (stateful sarmalayıcı). ViewModel'i oluşturur, durumu toplar
 * ve niyetleri (intent) iletir.
 */
@Composable
fun SolverScreen(
    darkTheme: Boolean,
    viewModel: SolverViewModel = viewModel { SolverViewModel() }
) {
    val state by viewModel.state.collectAsState()
    SolverContent(
        state = state,
        darkTheme = darkTheme,
        onIntent = viewModel::onIntent
    )
}

/** Saf (stateless) içerik — önizlenebilir. */
@Composable
fun SolverContent(
    state: SolverState,
    darkTheme: Boolean,
    onIntent: (SolverIntent) -> Unit
) {
    val strings = state.strings
    Box(
        Modifier
            .fillMaxSize()
            .background(backgroundBrush(darkTheme))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item { Header(state, onIntent) }

            item {
                GlassCard {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = state.inputText,
                            onValueChange = { onIntent(SolverIntent.InputChanged(it)) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(strings.inputPlaceholder) },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Monospace
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            )
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            GlassButton(
                                text = strings.solveButton,
                                onClick = { onIntent(SolverIntent.Solve) },
                                enabled = state.canSolve
                            )
                            GhostButton(
                                text = strings.clearButton,
                                onClick = { onIntent(SolverIntent.Clear) }
                            )
                        }
                    }
                }
            }

            item { ExamplesRow(state, onIntent) }

            state.errorMessage?.let { msg ->
                item { ErrorCard(msg) }
            }

            state.solution?.let { solution ->
                item { SolutionHeader(solution, strings.topicName(solution.topic)) }
                itemsIndexed(solution.steps, key = { _, s -> s.stepNumber }) { index, step ->
                    SolutionStepCard(
                        step = step,
                        ruleDescription = strings.rule(step.descriptionLocalizationKey),
                        indexInList = index
                    )
                }
                item { FinalAnswerCard(strings.finalAnswerLabel, solution.finalAnswerLatex) }
            } ?: item { EmptyState(strings.emptyStateMessage) }
        }
    }
}

@Composable
private fun Header(state: SolverState, onIntent: (SolverIntent) -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = state.strings.appName,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = state.strings.solverTitle,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
            Text(
                text = "${state.strings.versionPrefix} ${AppVersion.NAME}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }
        Spacer(Modifier.height(10.dp))
        LanguageRow(state, onIntent)
    }
}

@Composable
private fun LanguageRow(state: SolverState, onIntent: (SolverIntent) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Localization.translatedLanguages.forEach { lang ->
            val selected = lang == state.language
            Box(
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selected) MatatikColors.Violet.copy(alpha = 0.25f) else Color.Transparent
                    )
                    .clickable { onIntent(SolverIntent.ChangeLanguage(lang)) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text("${lang.flag} ${lang.nativeName}", fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}

@Composable
private fun ExamplesRow(state: SolverState, onIntent: (SolverIntent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = state.strings.examplesTitle,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            state.examples.take(4).forEach { example ->
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MatatikColors.Teal.copy(alpha = 0.16f))
                        .clickable { onIntent(SolverIntent.LoadExample(example)) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        LatexDisplay.render(example),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
private fun SolutionHeader(solution: MathematicalSolution, topicName: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Pill(topicName, MatatikColors.Violet)
        Pill(solution.baseDifficulty.name, MatatikColors.Orange)
    }
}

@Composable
private fun Pill(text: String, color: Color) {
    Box(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.18f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = color, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun FinalAnswerCard(label: String, finalLatex: String) {
    GlassCard {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Text(
                LatexDisplay.render(finalLatex),
                fontFamily = FontFamily.Monospace,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MatatikColors.Orange
            )
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFF5252).copy(alpha = 0.15f))
            .padding(16.dp)
    ) {
        Text(message, color = Color(0xFFFF5252), fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(
        Modifier.fillMaxWidth().padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            message,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            fontSize = 15.sp
        )
    }
}

@Composable
private fun GhostButton(text: String, onClick: () -> Unit) {
    Box(
        Modifier
            .clip(RoundedCornerShape(18.dp))
            .drawBehind {
                drawRoundRect(
                    brush = Brush.linearGradient(
                        listOf(Color.White.copy(alpha = 0.10f), Color.White.copy(alpha = 0.04f))
                    )
                )
            }
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}

// — Önizlemeler —

private fun previewState() = SolverState(
    inputText = "2x + 3 = 7",
    solution = com.vahitkeskin.matatik.core.domain.engine.MathSolverEngine().solve("2x + 3 = 7")
)

@Preview
@Composable
private fun SolverContentLightPreview() {
    MatatikTheme(ThemeMode.LIGHT) {
        SolverContent(previewState(), darkTheme = false, onIntent = {})
    }
}

@Preview
@Composable
private fun SolverContentDarkPreview() {
    MatatikTheme(ThemeMode.DARK) {
        SolverContent(previewState(), darkTheme = true, onIntent = {})
    }
}
