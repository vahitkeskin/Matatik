package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** Polski (Polonyaca) metinler. */
object PlString : LocalizedStrings {
    override val language = Language.PL

    override val appName = "Matatik"
    override val solverTitle = "Rozwiązywacz krok po kroku"
    override val inputPlaceholder = "Wpisz równanie, np. 2x + 3 = 7"
    override val solveButton = "Rozwiąż"
    override val clearButton = "Wyczyść"
    override val stepLabel = "Krok"
    override val finalAnswerLabel = "Wynik"
    override val examplesTitle = "Przykłady"
    override val emptyStateMessage = "Wpisz równanie do rozwiązania"
    override val languageSelectorTitle = "Język"
    override val versionPrefix = "Wersja"

    override val errorParse = "Nie można przeanalizować danych. Sprawdź równanie."
    override val errorUnsupported = "Ten problem nie jest jeszcze obsługiwany."
    override val errorEmpty = "Proszę wpisać równanie."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "Algebra podstawowa"
        MathTopic.LOGARITHM -> "Logarytm"
        MathTopic.CALCULUS_DERIVATIVE -> "Pochodna"
        MathTopic.CALCULUS_INTEGRAL -> "Całka"
        MathTopic.MATRIX_OPERATIONS -> "Operacje macierzowe"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "Zapisać dane równanie."
        RuleKeys.LINEAR_MOVE_TERMS -> "Przenieść wyrazy ze zmienną na lewą stronę, stałe na prawą."
        RuleKeys.LINEAR_COMBINE -> "Zredukować wyrazy podobne."
        RuleKeys.LINEAR_DIVIDE -> "Podzielić obie strony przez współczynnik."
        RuleKeys.LINEAR_SOLUTION -> "Uzyskać rozwiązanie równania."
        RuleKeys.LOG_ORIGINAL -> "Zapisać dane wyrażenie logarytmiczne."
        RuleKeys.LOG_CHANGE_OF_BASE -> "Zastosować wzór na zmianę podstawy."
        RuleKeys.LOG_EVALUATE -> "Obliczyć wyrażenie numerycznie."
        RuleKeys.LOG_EXP_DEFINITION -> "Użyć wykładniczej definicji logarytmu."
        RuleKeys.LOG_SOLUTION -> "Obliczyć wartość zmiennej."
        RuleKeys.DERIVATIVE_ORIGINAL -> "Zapisać dane wyrażenie pochodnej."
        RuleKeys.DERIVATIVE_POWER_RULE -> "Zastosować regułę potęgową: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "Pochodna stałej wynosi zero."
        RuleKeys.DERIVATIVE_SUM_RULE -> "Reguła sumy: różniczkować każdy wyraz osobno."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "Wyciągnąć współczynnik stały przed pochodną."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "Uprościć otrzymane wyrażenie."
        RuleKeys.DERIVATIVE_SOLUTION -> "Uzyskać wynik pochodnej."
        else -> key
    }
}
