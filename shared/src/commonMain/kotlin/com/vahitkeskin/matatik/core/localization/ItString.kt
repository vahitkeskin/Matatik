package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** Italiano (İtalyanca) metinler. */
object ItString : LocalizedStrings {
    override val language = Language.IT

    override val appName = "Matatik"
    override val solverTitle = "Risolutore passo passo"
    override val inputPlaceholder = "Inserisci un'equazione, es. 2x + 3 = 7"
    override val solveButton = "Risolvi"
    override val clearButton = "Cancella"
    override val stepLabel = "Passaggio"
    override val finalAnswerLabel = "Risultato"
    override val examplesTitle = "Esempi"
    override val emptyStateMessage = "Inserisci un'equazione da risolvere"
    override val languageSelectorTitle = "Lingua"
    override val versionPrefix = "Versione"

    override val errorParse = "Impossibile analizzare l'input. Controlla l'equazione."
    override val errorUnsupported = "Questo problema non è ancora supportato."
    override val errorEmpty = "Inserisci un'equazione."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "Algebra di base"
        MathTopic.LOGARITHM -> "Logaritmo"
        MathTopic.CALCULUS_DERIVATIVE -> "Derivata"
        MathTopic.CALCULUS_INTEGRAL -> "Integrale"
        MathTopic.MATRIX_OPERATIONS -> "Operazioni con matrici"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "Scrivere l'equazione data."
        RuleKeys.LINEAR_MOVE_TERMS -> "Spostare i termini variabili a sinistra, le costanti a destra."
        RuleKeys.LINEAR_COMBINE -> "Combinare i termini simili."
        RuleKeys.LINEAR_DIVIDE -> "Dividere entrambi i lati per il coefficiente."
        RuleKeys.LINEAR_SOLUTION -> "Ottenere la soluzione dell'equazione."
        RuleKeys.LOG_ORIGINAL -> "Scrivere l'espressione logaritmica data."
        RuleKeys.LOG_CHANGE_OF_BASE -> "Applicare la formula del cambio di base."
        RuleKeys.LOG_EVALUATE -> "Valutare l'espressione numericamente."
        RuleKeys.LOG_EXP_DEFINITION -> "Usare la definizione esponenziale del logaritmo."
        RuleKeys.LOG_SOLUTION -> "Calcolare il valore della variabile."
        RuleKeys.DERIVATIVE_ORIGINAL -> "Scrivere l'espressione di derivata data."
        RuleKeys.DERIVATIVE_POWER_RULE -> "Applicare la regola della potenza: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "La derivata di una costante è zero."
        RuleKeys.DERIVATIVE_SUM_RULE -> "Regola della somma: derivare ogni termine."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "Fattorizzare il coefficiente costante."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "Semplificare l'espressione ottenuta."
        RuleKeys.DERIVATIVE_SOLUTION -> "Ottenere il risultato della derivata."
        else -> key
    }
}
