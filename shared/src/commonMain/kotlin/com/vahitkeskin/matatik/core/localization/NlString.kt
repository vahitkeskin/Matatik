package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** Nederlands (Flemenkçe) metinler. */
object NlString : LocalizedStrings {
    override val language = Language.NL

    override val appName = "Matatik"
    override val solverTitle = "Stap-voor-stap oplosser"
    override val inputPlaceholder = "Voer een vergelijking in, bijv. 2x + 3 = 7"
    override val solveButton = "Oplossen"
    override val clearButton = "Wissen"
    override val stepLabel = "Stap"
    override val finalAnswerLabel = "Antwoord"
    override val examplesTitle = "Voorbeelden"
    override val emptyStateMessage = "Voer een vergelijking in om op te lossen"
    override val languageSelectorTitle = "Taal"
    override val versionPrefix = "Versie"

    override val errorParse = "Invoer kon niet worden geanalyseerd. Controleer de vergelijking."
    override val errorUnsupported = "Dit probleem wordt nog niet ondersteund."
    override val errorEmpty = "Voer een vergelijking in."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "Basisalgebra"
        MathTopic.LOGARITHM -> "Logaritme"
        MathTopic.CALCULUS_DERIVATIVE -> "Afgeleide"
        MathTopic.CALCULUS_INTEGRAL -> "Integraal"
        MathTopic.MATRIX_OPERATIONS -> "Matrixbewerkingen"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "De gegeven vergelijking opschrijven."
        RuleKeys.LINEAR_MOVE_TERMS -> "Variabele termen naar links, constanten naar rechts verplaatsen."
        RuleKeys.LINEAR_COMBINE -> "Gelijksoortige termen samenvoegen."
        RuleKeys.LINEAR_DIVIDE -> "Beide zijden delen door de coëfficiënt."
        RuleKeys.LINEAR_SOLUTION -> "De oplossing van de vergelijking verkrijgen."
        RuleKeys.LOG_ORIGINAL -> "De gegeven logaritmische uitdrukking opschrijven."
        RuleKeys.LOG_CHANGE_OF_BASE -> "De grondtalveranderingsregel toepassen."
        RuleKeys.LOG_EVALUATE -> "De uitdrukking numeriek berekenen."
        RuleKeys.LOG_EXP_DEFINITION -> "De exponentiële definitie van de logaritme gebruiken."
        RuleKeys.LOG_SOLUTION -> "De waarde van de variabele berekenen."
        RuleKeys.DERIVATIVE_ORIGINAL -> "De gegeven afgeleide uitdrukking opschrijven."
        RuleKeys.DERIVATIVE_POWER_RULE -> "Machtsregel toepassen: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "De afgeleide van een constante is nul."
        RuleKeys.DERIVATIVE_SUM_RULE -> "Somregel: elke term apart differentiëren."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "De constante coëfficiënt eruit halen."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "De verkregen uitdrukking vereenvoudigen."
        RuleKeys.DERIVATIVE_SOLUTION -> "Het resultaat van de afgeleide verkrijgen."
        else -> key
    }
}
