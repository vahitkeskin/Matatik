package com.vahitkeskin.matatik.core.domain.engine

import com.vahitkeskin.matatik.core.domain.ast.Equation
import com.vahitkeskin.matatik.core.domain.ast.Expr
import com.vahitkeskin.matatik.core.domain.ast.LatexRenderer
import com.vahitkeskin.matatik.core.domain.ast.formatNumber
import com.vahitkeskin.matatik.core.domain.model.DifficultyLevel
import com.vahitkeskin.matatik.core.domain.model.HighlightedCoordinate
import com.vahitkeskin.matatik.core.domain.model.MathTopic
import com.vahitkeskin.matatik.core.domain.model.MathematicalSolution
import com.vahitkeskin.matatik.core.domain.model.SolutionStep
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.pow

/**
 * Logaritma çözücü. İki kalıbı destekler:
 *  - **Hesaplama:** `log_b(n)` (n, b sayısal) → taban değiştirme + sayısal değer.
 *  - **Denklem:** `log_b(x) = c` → üstel tanım ile `x = b^c`.
 */
class LogarithmSolver : Solver {

    override val topic: MathTopic = MathTopic.LOGARITHM

    override fun canSolve(equation: Equation): Boolean =
        asEvaluation(equation) != null || asEquation(equation) != null

    /** `log_b(n)` sayısal hesaplama kalıbı (rhs = 0 sentinel). */
    private fun asEvaluation(eq: Equation): Pair<Double, Double>? {
        val lhs = eq.lhs
        val rhsIsSentinel = eq.rhs is Expr.Num && eq.rhs.value == 0.0
        if (!rhsIsSentinel) return null
        return when (lhs) {
            is Expr.Log -> {
                val base = (lhs.base as? Expr.Num)?.value ?: return null
                val arg = (lhs.arg as? Expr.Num)?.value ?: return null
                if (isValidLog(base, arg)) base to arg else null
            }
            is Expr.Ln -> {
                val arg = (lhs.arg as? Expr.Num)?.value ?: return null
                if (arg > 0) kotlin.math.E to arg else null
            }
            else -> null
        }
    }

    /** `log_b(x) = c` denklem kalıbı → (base, c, varName). */
    private fun asEquation(eq: Equation): Triple<Double, Double, String>? {
        val lhs = eq.lhs as? Expr.Log ?: return null
        val base = (lhs.base as? Expr.Num)?.value ?: return null
        val variable = lhs.arg as? Expr.Variable ?: return null
        val c = (eq.rhs as? Expr.Num)?.value ?: return null
        // rhs == 0 sentinel'i hesaplama ile çakışmasın diye değişken şart
        return if (isValidBase(base)) Triple(base, c, variable.name) else null
    }

    private fun isValidBase(base: Double) = base > 0 && abs(base - 1.0) > 1e-12
    private fun isValidLog(base: Double, arg: Double) = isValidBase(base) && arg > 0

    override fun solve(equation: Equation): MathematicalSolution {
        asEvaluation(equation)?.let { (base, arg) -> return solveEvaluation(base, arg, equation) }
        asEquation(equation)?.let { (base, c, name) -> return solveEquation(base, c, name) }
        throw UnsupportedProblemException("Logaritma olarak çözülemiyor")
    }

    private fun solveEvaluation(base: Double, arg: Double, eq: Equation): MathematicalSolution {
        val rawLatex = LatexRenderer.render(eq.lhs)
        val value = ln(arg) / ln(base)
        val steps = listOf(
            SolutionStep(
                stepNumber = 1,
                ruleApplied = "Verilen İfade",
                descriptionLocalizationKey = RuleKeys.LOG_ORIGINAL,
                formulaUsedLatex = "",
                currentExpressionLatex = rawLatex,
                highlightedParts = listOf(
                    HighlightedCoordinate(rawLatex, HighlightColors.VARIABLE, AnimationTypes.GLOW)
                )
            ),
            SolutionStep(
                stepNumber = 2,
                ruleApplied = "Taban Değiştirme Kuralı",
                descriptionLocalizationKey = RuleKeys.LOG_CHANGE_OF_BASE,
                formulaUsedLatex = "\\log_{b}(a) = \\frac{\\ln(a)}{\\ln(b)}",
                currentExpressionLatex =
                    "\\frac{\\ln(${formatNumber(arg)})}{\\ln(${formatNumber(base)})}",
                highlightedParts = listOf(
                    HighlightedCoordinate(formatNumber(base), HighlightColors.OPERATION, AnimationTypes.SCALE)
                )
            ),
            SolutionStep(
                stepNumber = 3,
                ruleApplied = "Sonuç",
                descriptionLocalizationKey = RuleKeys.LOG_EVALUATE,
                formulaUsedLatex = "",
                currentExpressionLatex = formatNumber(value),
                highlightedParts = listOf(
                    HighlightedCoordinate(formatNumber(value), HighlightColors.RESULT, AnimationTypes.SCALE)
                )
            )
        )
        return MathematicalSolution(
            id = "sol_${abs(rawLatex.hashCode())}",
            topic = topic,
            baseDifficulty = DifficultyLevel.EASY,
            rawEquationLatex = rawLatex,
            steps = steps,
            finalAnswerLatex = formatNumber(value)
        )
    }

    private fun solveEquation(base: Double, c: Double, name: String): MathematicalSolution {
        val rawLatex = "\\log_{${formatNumber(base)}}\\left($name\\right) = ${formatNumber(c)}"
        val value = base.pow(c)
        val finalLatex = "$name = ${formatNumber(value)}"
        val steps = listOf(
            SolutionStep(
                stepNumber = 1,
                ruleApplied = "Verilen Denklem",
                descriptionLocalizationKey = RuleKeys.LOG_ORIGINAL,
                formulaUsedLatex = "",
                currentExpressionLatex = rawLatex,
                highlightedParts = listOf(
                    HighlightedCoordinate(name, HighlightColors.VARIABLE, AnimationTypes.GLOW)
                )
            ),
            SolutionStep(
                stepNumber = 2,
                ruleApplied = "Üstel Tanım",
                descriptionLocalizationKey = RuleKeys.LOG_EXP_DEFINITION,
                formulaUsedLatex = "\\log_{b}(x) = c \\iff x = b^{c}",
                currentExpressionLatex = "$name = ${formatNumber(base)}^{${formatNumber(c)}}",
                highlightedParts = listOf(
                    HighlightedCoordinate(formatNumber(base), HighlightColors.OPERATION, AnimationTypes.GLOW)
                )
            ),
            SolutionStep(
                stepNumber = 3,
                ruleApplied = "Sonuç",
                descriptionLocalizationKey = RuleKeys.LOG_SOLUTION,
                formulaUsedLatex = "",
                currentExpressionLatex = finalLatex,
                highlightedParts = listOf(
                    HighlightedCoordinate(formatNumber(value), HighlightColors.RESULT, AnimationTypes.SCALE)
                )
            )
        )
        return MathematicalSolution(
            id = "sol_${abs(rawLatex.hashCode())}",
            topic = topic,
            baseDifficulty = DifficultyLevel.MEDIUM,
            rawEquationLatex = rawLatex,
            steps = steps,
            finalAnswerLatex = finalLatex
        )
    }
}
