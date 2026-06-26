package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** Deutsch (Almanca) metinler. */
object DeString : LocalizedStrings {
    override val language = Language.DE

    override val appName = "Matatik"
    override val solverTitle = "Schritt-für-Schritt-Löser"
    override val inputPlaceholder = "Gleichung eingeben, z.B. 2x + 3 = 7"
    override val solveButton = "Lösen"
    override val clearButton = "Löschen"
    override val stepLabel = "Schritt"
    override val finalAnswerLabel = "Ergebnis"
    override val examplesTitle = "Beispiele"
    override val emptyStateMessage = "Geben Sie eine Gleichung zum Lösen ein"
    override val languageSelectorTitle = "Sprache"
    override val versionPrefix = "Version"

    override val errorParse = "Eingabe konnte nicht analysiert werden. Bitte überprüfen Sie die Gleichung."
    override val errorUnsupported = "Dieses Problem wird noch nicht unterstützt."
    override val errorEmpty = "Bitte geben Sie eine Gleichung ein."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "Grundlegende Algebra"
        MathTopic.LOGARITHM -> "Logarithmus"
        MathTopic.CALCULUS_DERIVATIVE -> "Ableitung"
        MathTopic.CALCULUS_INTEGRAL -> "Integral"
        MathTopic.MATRIX_OPERATIONS -> "Matrixoperationen"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "Die gegebene Gleichung aufschreiben."
        RuleKeys.LINEAR_MOVE_TERMS -> "Variable Terme nach links, Konstanten nach rechts verschieben."
        RuleKeys.LINEAR_COMBINE -> "Gleichartige Terme zusammenfassen."
        RuleKeys.LINEAR_DIVIDE -> "Beide Seiten durch den Koeffizienten teilen."
        RuleKeys.LINEAR_SOLUTION -> "Lösung der Gleichung bestimmen."
        RuleKeys.LOG_ORIGINAL -> "Den gegebenen logarithmischen Ausdruck aufschreiben."
        RuleKeys.LOG_CHANGE_OF_BASE -> "Basiswechselregel anwenden."
        RuleKeys.LOG_EVALUATE -> "Den Ausdruck numerisch auswerten."
        RuleKeys.LOG_EXP_DEFINITION -> "Die exponentielle Definition des Logarithmus verwenden."
        RuleKeys.LOG_SOLUTION -> "Den Wert der Variable berechnen."
        RuleKeys.DERIVATIVE_ORIGINAL -> "Den gegebenen Ableitungsausdruck aufschreiben."
        RuleKeys.DERIVATIVE_POWER_RULE -> "Potenzregel anwenden: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "Die Ableitung einer Konstante ist Null."
        RuleKeys.DERIVATIVE_SUM_RULE -> "Summenregel: jeden Term einzeln ableiten."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "Konstanten Faktor herausziehen."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "Den erhaltenen Ausdruck vereinfachen."
        RuleKeys.DERIVATIVE_SOLUTION -> "Das Ergebnis der Ableitung bestimmen."
        else -> key
    }
}
