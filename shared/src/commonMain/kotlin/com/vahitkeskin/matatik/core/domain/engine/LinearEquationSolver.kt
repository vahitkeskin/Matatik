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

/**
 * Tek değişkenli birinci dereceden denklem çözücü: `a·x + b = c·x + d`.
 *
 * Üretilen adımlar:
 *  1. Verilen denklem
 *  2. Değişkenleri sola, sabitleri sağa taşı
 *  3. Benzer terimleri sadeleştir → `A·x = C`
 *  4. Her iki tarafı katsayıya böl → `x = C / A`
 *  5. Sonuç → `x = değer`
 */
class LinearEquationSolver : Solver {

    override val topic: MathTopic = MathTopic.BASIC_ALGEBRA

    private data class Reduced(val varName: String, val a: Double, val c: Double)

    override fun canSolve(equation: Equation): Boolean = reduce(equation) != null

    /** Denklemi `A·x = C` biçimine indirger; lineer/tekil değilse null. */
    private fun reduce(equation: Equation): Reduced? {
        val varName = AlgebraUtils.findVariableName(equation.lhs)
            ?: AlgebraUtils.findVariableName(equation.rhs)
            ?: return null
        return try {
            val left = AlgebraUtils.toLinearForm(equation.lhs)
            val right = AlgebraUtils.toLinearForm(equation.rhs)
            val a = left.a - right.a            // değişken katsayısı (sol)
            val c = right.b - left.b            // sabit (sağ)
            if (abs(a) < 1e-12) null else Reduced(varName, a, c)
        } catch (_: NotLinearException) {
            null
        }
    }

    override fun solve(equation: Equation): MathematicalSolution {
        val reduced = reduce(equation)
            ?: throw UnsupportedProblemException("Lineer denklem olarak çözülemiyor")
        val (name, a, c) = reduced
        val result = c / a
        val rawLatex = LatexRenderer.render(equation)
        val steps = mutableListOf<SolutionStep>()

        // 1) Verilen denklem
        steps += SolutionStep(
            stepNumber = 1,
            ruleApplied = "Verilen Denklem",
            descriptionLocalizationKey = RuleKeys.LINEAR_ORIGINAL,
            formulaUsedLatex = "",
            currentExpressionLatex = rawLatex,
            highlightedParts = listOf(
                HighlightedCoordinate(name, HighlightColors.VARIABLE, AnimationTypes.GLOW)
            )
        )

        // 2) Terimleri taşı (kavramsal adım)
        steps += SolutionStep(
            stepNumber = 2,
            ruleApplied = "Terimleri Düzenle",
            descriptionLocalizationKey = RuleKeys.LINEAR_MOVE_TERMS,
            formulaUsedLatex = "ax + b = cx + d \\Rightarrow (a-c)x = d-b",
            currentExpressionLatex = rawLatex,
            highlightedParts = listOf(
                HighlightedCoordinate(name, HighlightColors.VARIABLE, AnimationTypes.TRANSLATE)
            )
        )

        // 3) Benzer terimleri sadeleştir → A·x = C
        val coeffTerm = coefficientTerm(a, name)
        val combinedLatex = "${LatexRenderer.render(coeffTerm)} = ${formatNumber(c)}"
        steps += SolutionStep(
            stepNumber = 3,
            ruleApplied = "Benzer Terimleri Topla",
            descriptionLocalizationKey = RuleKeys.LINEAR_COMBINE,
            formulaUsedLatex = "",
            currentExpressionLatex = combinedLatex,
            highlightedParts = listOf(
                HighlightedCoordinate(formatNumber(a), HighlightColors.OPERATION, AnimationTypes.SCALE)
            )
        )

        // 4) Katsayıya böl → x = C / A  (katsayı 1 ise bu adım atlanmış sayılır)
        if (abs(a - 1.0) > 1e-12) {
            val divideLatex = "$name = \\frac{${formatNumber(c)}}{${formatNumber(a)}}"
            steps += SolutionStep(
                stepNumber = 4,
                ruleApplied = "İki Tarafı Böl",
                descriptionLocalizationKey = RuleKeys.LINEAR_DIVIDE,
                formulaUsedLatex = "ax = C \\Rightarrow x = \\frac{C}{a}",
                currentExpressionLatex = divideLatex,
                highlightedParts = listOf(
                    HighlightedCoordinate(formatNumber(a), HighlightColors.OPERATION, AnimationTypes.GLOW)
                )
            )
        }

        // 5) Sonuç
        val finalLatex = "$name = ${formatNumber(result)}"
        steps += SolutionStep(
            stepNumber = steps.size + 1,
            ruleApplied = "Sonuç",
            descriptionLocalizationKey = RuleKeys.LINEAR_SOLUTION,
            formulaUsedLatex = "",
            currentExpressionLatex = finalLatex,
            highlightedParts = listOf(
                HighlightedCoordinate(formatNumber(result), HighlightColors.RESULT, AnimationTypes.SCALE)
            )
        )

        return MathematicalSolution(
            id = "sol_${abs(rawLatex.hashCode())}",
            topic = topic,
            baseDifficulty = difficultyOf(a, c),
            rawEquationLatex = rawLatex,
            steps = steps,
            finalAnswerLatex = finalLatex
        )
    }

    private fun coefficientTerm(a: Double, name: String): Expr = when {
        abs(a - 1.0) < 1e-12 -> Expr.Variable(name)
        abs(a + 1.0) < 1e-12 -> Expr.Neg(Expr.Variable(name))
        else -> Expr.Mul(Expr.Num(a), Expr.Variable(name))
    }

    private fun difficultyOf(a: Double, c: Double): DifficultyLevel {
        val isWhole = abs(c % a) < 1e-9
        return if (isWhole && abs(a) <= 10) DifficultyLevel.EASY else DifficultyLevel.MEDIUM
    }
}
