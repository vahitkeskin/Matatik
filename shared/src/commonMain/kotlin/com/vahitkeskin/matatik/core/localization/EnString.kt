package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** English strings. */
object EnString : LocalizedStrings {
    override val language = Language.EN

    override val appName = "Matatik"
    override val solverTitle = "Step-by-Step Solver"
    override val inputPlaceholder = "Enter an equation, e.g. 2x + 3 = 7"
    override val solveButton = "Solve"
    override val clearButton = "Clear"
    override val stepLabel = "Step"
    override val finalAnswerLabel = "Answer"
    override val examplesTitle = "Examples"
    override val emptyStateMessage = "Enter an equation to solve"
    override val languageSelectorTitle = "Language"
    override val versionPrefix = "Version"

    override val errorParse = "Could not parse the input. Please check the equation."
    override val errorUnsupported = "This problem is not supported yet."
    override val errorEmpty = "Please enter an equation."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "Basic Algebra"
        MathTopic.LOGARITHM -> "Logarithm"
        MathTopic.CALCULUS_DERIVATIVE -> "Derivative"
        MathTopic.CALCULUS_INTEGRAL -> "Integral"
        MathTopic.MATRIX_OPERATIONS -> "Matrix Operations"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "Write down the given equation."
        RuleKeys.LINEAR_MOVE_TERMS -> "Move variable terms to the left, constants to the right."
        RuleKeys.LINEAR_COMBINE -> "Combine like terms to simplify."
        RuleKeys.LINEAR_DIVIDE -> "Divide both sides by the coefficient to isolate the variable."
        RuleKeys.LINEAR_SOLUTION -> "Obtain the solution of the equation."
        RuleKeys.LOG_ORIGINAL -> "Write down the given logarithmic expression."
        RuleKeys.LOG_CHANGE_OF_BASE -> "Apply the change-of-base rule."
        RuleKeys.LOG_EVALUATE -> "Evaluate the expression numerically."
        RuleKeys.LOG_EXP_DEFINITION -> "Use the exponential definition of the logarithm."
        RuleKeys.LOG_SOLUTION -> "Compute the value of the variable."
        RuleKeys.DERIVATIVE_ORIGINAL -> "Write down the given derivative expression."
        RuleKeys.DERIVATIVE_POWER_RULE -> "Apply the power rule: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "The derivative of a constant is zero."
        RuleKeys.DERIVATIVE_SUM_RULE -> "Sum/difference rule: differentiate each term."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "Factor out the constant coefficient."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "Simplify the resulting expression."
        RuleKeys.DERIVATIVE_SOLUTION -> "Obtain the final derivative."
        else -> key
    }
}
