package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** Português - Brasil (Portekizce) metinler. */
object PtString : LocalizedStrings {
    override val language = Language.PT

    override val appName = "Matatik"
    override val solverTitle = "Resolvedor passo a passo"
    override val inputPlaceholder = "Insira uma equação, ex. 2x + 3 = 7"
    override val solveButton = "Resolver"
    override val clearButton = "Limpar"
    override val stepLabel = "Passo"
    override val finalAnswerLabel = "Resultado"
    override val examplesTitle = "Exemplos"
    override val emptyStateMessage = "Insira uma equação para resolver"
    override val languageSelectorTitle = "Idioma"
    override val versionPrefix = "Versão"

    override val errorParse = "Não foi possível analisar a entrada. Verifique a equação."
    override val errorUnsupported = "Este problema ainda não é suportado."
    override val errorEmpty = "Por favor, insira uma equação."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "Álgebra básica"
        MathTopic.LOGARITHM -> "Logaritmo"
        MathTopic.CALCULUS_DERIVATIVE -> "Derivada"
        MathTopic.CALCULUS_INTEGRAL -> "Integral"
        MathTopic.MATRIX_OPERATIONS -> "Operações com matrizes"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "Escrever a equação dada."
        RuleKeys.LINEAR_MOVE_TERMS -> "Mover os termos variáveis para a esquerda, constantes para a direita."
        RuleKeys.LINEAR_COMBINE -> "Combinar termos semelhantes."
        RuleKeys.LINEAR_DIVIDE -> "Dividir ambos os lados pelo coeficiente."
        RuleKeys.LINEAR_SOLUTION -> "Obter a solução da equação."
        RuleKeys.LOG_ORIGINAL -> "Escrever a expressão logarítmica dada."
        RuleKeys.LOG_CHANGE_OF_BASE -> "Aplicar a regra de mudança de base."
        RuleKeys.LOG_EVALUATE -> "Avaliar a expressão numericamente."
        RuleKeys.LOG_EXP_DEFINITION -> "Usar a definição exponencial do logaritmo."
        RuleKeys.LOG_SOLUTION -> "Calcular o valor da variável."
        RuleKeys.DERIVATIVE_ORIGINAL -> "Escrever a expressão de derivada dada."
        RuleKeys.DERIVATIVE_POWER_RULE -> "Aplicar a regra da potência: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "A derivada de uma constante é zero."
        RuleKeys.DERIVATIVE_SUM_RULE -> "Regra da soma: derivar cada termo."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "Fatorar o coeficiente constante."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "Simplificar a expressão obtida."
        RuleKeys.DERIVATIVE_SOLUTION -> "Obter o resultado da derivada."
        else -> key
    }
}
