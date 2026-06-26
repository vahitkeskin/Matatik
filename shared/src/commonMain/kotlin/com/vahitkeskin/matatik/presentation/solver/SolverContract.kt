package com.vahitkeskin.matatik.presentation.solver

import com.vahitkeskin.matatik.core.localization.Language
import com.vahitkeskin.matatik.core.localization.LocalizedStrings
import com.vahitkeskin.matatik.core.localization.Localization
import com.vahitkeskin.matatik.core.domain.model.MathematicalSolution

/**
 * MVI durum modeli. Ekranın tek doğruluk kaynağı (single source of truth).
 */
data class SolverState(
    val inputText: String = "",
    val language: Language = Language.DEFAULT,
    val isLoading: Boolean = false,
    val solution: MathematicalSolution? = null,
    val errorMessage: String? = null,
    val examples: List<String> = DEFAULT_EXAMPLES
) {
    /** Aktif dile ait metin sözleşmesi. */
    val strings: LocalizedStrings get() = Localization[language]

    val canSolve: Boolean get() = inputText.isNotBlank() && !isLoading

    companion object {
        val DEFAULT_EXAMPLES = listOf(
            "2x + 3 = 7",
            "5x - 4 = 2x + 11",
            "log_2(8)",
            "log_3(x) = 4"
        )
    }
}

/** Kullanıcı niyetleri (MVI Intent). */
sealed interface SolverIntent {
    data class InputChanged(val value: String) : SolverIntent
    data object Solve : SolverIntent
    data object Clear : SolverIntent
    data class ChangeLanguage(val language: Language) : SolverIntent
    data class LoadExample(val expression: String) : SolverIntent
}

/** Tek seferlik yan etkiler (MVI Effect). */
sealed interface SolverEffect {
    data class ShowError(val message: String) : SolverEffect
    data object Solved : SolverEffect
}
