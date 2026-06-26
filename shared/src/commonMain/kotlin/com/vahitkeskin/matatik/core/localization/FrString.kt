package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** Français (Fransızca) metinler. */
object FrString : LocalizedStrings {
    override val language = Language.FR

    override val appName = "Matatik"
    override val solverTitle = "Résolveur étape par étape"
    override val inputPlaceholder = "Entrez une équation, ex. 2x + 3 = 7"
    override val solveButton = "Résoudre"
    override val clearButton = "Effacer"
    override val stepLabel = "Étape"
    override val finalAnswerLabel = "Résultat"
    override val examplesTitle = "Exemples"
    override val emptyStateMessage = "Entrez une équation à résoudre"
    override val languageSelectorTitle = "Langue"
    override val versionPrefix = "Version"

    override val errorParse = "Impossible d'analyser l'entrée. Vérifiez l'équation."
    override val errorUnsupported = "Ce problème n'est pas encore pris en charge."
    override val errorEmpty = "Veuillez entrer une équation."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "Algèbre de base"
        MathTopic.LOGARITHM -> "Logarithme"
        MathTopic.CALCULUS_DERIVATIVE -> "Dérivée"
        MathTopic.CALCULUS_INTEGRAL -> "Intégrale"
        MathTopic.MATRIX_OPERATIONS -> "Opérations matricielles"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "Écrire l'équation donnée."
        RuleKeys.LINEAR_MOVE_TERMS -> "Déplacer les termes variables à gauche, les constantes à droite."
        RuleKeys.LINEAR_COMBINE -> "Regrouper les termes semblables."
        RuleKeys.LINEAR_DIVIDE -> "Diviser les deux côtés par le coefficient."
        RuleKeys.LINEAR_SOLUTION -> "Obtenir la solution de l'équation."
        RuleKeys.LOG_ORIGINAL -> "Écrire l'expression logarithmique donnée."
        RuleKeys.LOG_CHANGE_OF_BASE -> "Appliquer la formule de changement de base."
        RuleKeys.LOG_EVALUATE -> "Évaluer l'expression numériquement."
        RuleKeys.LOG_EXP_DEFINITION -> "Utiliser la définition exponentielle du logarithme."
        RuleKeys.LOG_SOLUTION -> "Calculer la valeur de la variable."
        RuleKeys.DERIVATIVE_ORIGINAL -> "Écrire l'expression de dérivée donnée."
        RuleKeys.DERIVATIVE_POWER_RULE -> "Appliquer la règle de puissance : d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "La dérivée d'une constante est zéro."
        RuleKeys.DERIVATIVE_SUM_RULE -> "Règle de la somme : dériver chaque terme."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "Factoriser le coefficient constant."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "Simplifier l'expression obtenue."
        RuleKeys.DERIVATIVE_SOLUTION -> "Obtenir le résultat de la dérivée."
        else -> key
    }
}
