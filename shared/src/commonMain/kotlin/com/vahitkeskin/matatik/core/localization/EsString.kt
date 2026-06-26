package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** Español (İspanyolca) metinler. */
object EsString : LocalizedStrings {
    override val language = Language.ES

    override val appName = "Matatik"
    override val solverTitle = "Resolvedor paso a paso"
    override val inputPlaceholder = "Ingrese una ecuación, ej. 2x + 3 = 7"
    override val solveButton = "Resolver"
    override val clearButton = "Limpiar"
    override val stepLabel = "Paso"
    override val finalAnswerLabel = "Resultado"
    override val examplesTitle = "Ejemplos"
    override val emptyStateMessage = "Ingrese una ecuación para resolver"
    override val languageSelectorTitle = "Idioma"
    override val versionPrefix = "Versión"

    override val errorParse = "No se pudo analizar la entrada. Verifique la ecuación."
    override val errorUnsupported = "Este problema aún no es compatible."
    override val errorEmpty = "Por favor, ingrese una ecuación."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "Álgebra básica"
        MathTopic.LOGARITHM -> "Logaritmo"
        MathTopic.CALCULUS_DERIVATIVE -> "Derivada"
        MathTopic.CALCULUS_INTEGRAL -> "Integral"
        MathTopic.MATRIX_OPERATIONS -> "Operaciones con matrices"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "Escribir la ecuación dada."
        RuleKeys.LINEAR_MOVE_TERMS -> "Mover los términos variables a la izquierda, las constantes a la derecha."
        RuleKeys.LINEAR_COMBINE -> "Combinar términos semejantes."
        RuleKeys.LINEAR_DIVIDE -> "Dividir ambos lados por el coeficiente."
        RuleKeys.LINEAR_SOLUTION -> "Obtener la solución de la ecuación."
        RuleKeys.LOG_ORIGINAL -> "Escribir la expresión logarítmica dada."
        RuleKeys.LOG_CHANGE_OF_BASE -> "Aplicar la regla de cambio de base."
        RuleKeys.LOG_EVALUATE -> "Evaluar la expresión numéricamente."
        RuleKeys.LOG_EXP_DEFINITION -> "Usar la definición exponencial del logaritmo."
        RuleKeys.LOG_SOLUTION -> "Calcular el valor de la variable."
        RuleKeys.DERIVATIVE_ORIGINAL -> "Escribir la expresión de derivada dada."
        RuleKeys.DERIVATIVE_POWER_RULE -> "Aplicar la regla de potencia: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "La derivada de una constante es cero."
        RuleKeys.DERIVATIVE_SUM_RULE -> "Regla de la suma: derivar cada término."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "Factorizar el coeficiente constante."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "Simplificar la expresión obtenida."
        RuleKeys.DERIVATIVE_SOLUTION -> "Obtener el resultado de la derivada."
        else -> key
    }
}
