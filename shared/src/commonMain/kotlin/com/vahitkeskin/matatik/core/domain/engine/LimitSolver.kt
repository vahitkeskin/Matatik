package com.vahitkeskin.matatik.core.domain.engine

import com.vahitkeskin.matatik.core.domain.ast.Equation
import com.vahitkeskin.matatik.core.domain.ast.Expr
import com.vahitkeskin.matatik.core.domain.ast.Exprs
import com.vahitkeskin.matatik.core.domain.ast.LatexRenderer
import com.vahitkeskin.matatik.core.domain.ast.formatNumber
import com.vahitkeskin.matatik.core.domain.model.DifficultyLevel
import com.vahitkeskin.matatik.core.domain.model.HighlightedCoordinate
import com.vahitkeskin.matatik.core.domain.model.MathTopic
import com.vahitkeskin.matatik.core.domain.model.MathematicalSolution
import com.vahitkeskin.matatik.core.domain.model.SolutionStep
import kotlin.math.abs
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.tan
import kotlin.math.pow
import kotlin.math.PI

/**
 * Limit problemlerini çözmek için geliştirilmiş sembolik ve sayısal çözücü.
 *
 * Doğrudan yerine koyma ve 0/0 belirsizliklerinde L'Hôpital kurallarını destekler.
 */
class LimitSolver : Solver {
    override val topic: MathTopic = MathTopic.CALCULUS_DERIVATIVE

    override fun canSolve(equation: Equation): Boolean {
        val rhsSentinel = equation.rhs is Expr.Num && (equation.rhs as Expr.Num).value == 0.0
        return rhsSentinel && equation.lhs is Expr.Limit
    }

    override fun solve(equation: Equation): MathematicalSolution {
        val limitNode = equation.lhs as? Expr.Limit
            ?: throw UnsupportedProblemException("Limit ifadesi bekleniyor")
        val varName = limitNode.variable
        val expr = limitNode.expr
        val target = limitNode.target
        val rawLatex = LatexRenderer.render(limitNode)
        val steps = mutableListOf<SolutionStep>()

        // 1. Adım: Verilen Limit İfadesi
        steps += SolutionStep(
            stepNumber = 1,
            ruleApplied = "Verilen Limit",
            descriptionLocalizationKey = RuleKeys.LIMIT_ORIGINAL,
            formulaUsedLatex = "",
            currentExpressionLatex = rawLatex,
            highlightedParts = listOf(
                HighlightedCoordinate(varName, HighlightColors.VARIABLE, AnimationTypes.GLOW),
                HighlightedCoordinate(LatexRenderer.render(target), HighlightColors.CONSTANT, AnimationTypes.FADE)
            )
        )

        // Doğrudan yerine koymayı dene
        val subbedExpr = substitute(expr, varName, target)
        val subbedVal = evaluate(subbedExpr)

        if (subbedVal != null && !subbedVal.isNaN() && !subbedVal.isInfinite()) {
            val finalValStr = formatNumber(subbedVal)
            steps += SolutionStep(
                stepNumber = 2,
                ruleApplied = "Doğrudan Yerine Koyma",
                descriptionLocalizationKey = RuleKeys.LIMIT_SUBSTITUTION,
                formulaUsedLatex = "",
                currentExpressionLatex = "${LatexRenderer.render(subbedExpr)} = $finalValStr",
                highlightedParts = listOf(
                    HighlightedCoordinate(finalValStr, HighlightColors.RESULT, AnimationTypes.SCALE)
                )
            )
            steps += SolutionStep(
                stepNumber = 3,
                ruleApplied = "Sonuç",
                descriptionLocalizationKey = RuleKeys.LIMIT_SOLUTION,
                formulaUsedLatex = "",
                currentExpressionLatex = finalValStr,
                highlightedParts = listOf(
                    HighlightedCoordinate(finalValStr, HighlightColors.RESULT, AnimationTypes.GLOW)
                )
            )

            return MathematicalSolution(
                id = "limit_${abs(rawLatex.hashCode())}",
                topic = topic,
                baseDifficulty = DifficultyLevel.EASY,
                rawEquationLatex = rawLatex,
                steps = steps,
                finalAnswerLatex = finalValStr
            )
        }

        // 0/0 Belirsizliği tespiti
        if (expr is Expr.Div) {
            val payVal = evaluate(substitute(expr.left, varName, target))
            val paydaVal = evaluate(substitute(expr.right, varName, target))
            if (payVal != null && paydaVal != null && abs(payVal) < 1e-9 && abs(paydaVal) < 1e-9) {
                steps += SolutionStep(
                    stepNumber = steps.size + 1,
                    ruleApplied = "Belirsizlik Tespiti",
                    descriptionLocalizationKey = RuleKeys.LIMIT_SIMPLIFY,
                    formulaUsedLatex = "",
                    currentExpressionLatex = "\\frac{0}{0} \\text{ belirsizliği var.}",
                    highlightedParts = emptyList()
                )

                // L'Hôpital Kuralı uygula
                val derivSolver = DerivativeSolver()
                val payDeriv = derivSolver.simplify(derivSolver.differentiate(expr.left, varName))
                val paydaDeriv = derivSolver.simplify(derivSolver.differentiate(expr.right, varName))
                val lHopitalExpr = Expr.Div(payDeriv, paydaDeriv)
                val lHopitalLatex = "\\lim_{${varName} \\to ${LatexRenderer.render(target)}}\\left(${LatexRenderer.render(lHopitalExpr)}\\right)"

                steps += SolutionStep(
                    stepNumber = steps.size + 1,
                    ruleApplied = "L'Hôpital Kuralı",
                    descriptionLocalizationKey = RuleKeys.LIMIT_L_HOPITAL,
                    formulaUsedLatex = "\\lim \\frac{f(x)}{g(x)} = \\lim \\frac{f'(x)}{g'(x)}",
                    currentExpressionLatex = lHopitalLatex,
                    highlightedParts = listOf(
                        HighlightedCoordinate(LatexRenderer.render(payDeriv), HighlightColors.OPERATION, AnimationTypes.TRANSLATE),
                        HighlightedCoordinate(LatexRenderer.render(paydaDeriv), HighlightColors.OPERATION, AnimationTypes.TRANSLATE)
                    )
                )

                val finalSubbed = substitute(lHopitalExpr, varName, target)
                val finalVal = evaluate(finalSubbed)
                if (finalVal != null && !finalVal.isNaN() && !finalVal.isInfinite()) {
                    val finalValStr = formatNumber(finalVal)
                    steps += SolutionStep(
                        stepNumber = steps.size + 1,
                        ruleApplied = "Limiti Hesapla",
                        descriptionLocalizationKey = RuleKeys.LIMIT_SUBSTITUTION,
                        formulaUsedLatex = "",
                        currentExpressionLatex = "${LatexRenderer.render(finalSubbed)} = $finalValStr",
                        highlightedParts = listOf(
                            HighlightedCoordinate(finalValStr, HighlightColors.RESULT, AnimationTypes.SCALE)
                        )
                    )
                    steps += SolutionStep(
                        stepNumber = steps.size + 1,
                        ruleApplied = "Sonuç",
                        descriptionLocalizationKey = RuleKeys.LIMIT_SOLUTION,
                        formulaUsedLatex = "",
                        currentExpressionLatex = finalValStr,
                        highlightedParts = listOf(
                            HighlightedCoordinate(finalValStr, HighlightColors.RESULT, AnimationTypes.GLOW)
                        )
                    )

                    return MathematicalSolution(
                        id = "limit_${abs(rawLatex.hashCode())}",
                        topic = topic,
                        baseDifficulty = DifficultyLevel.MEDIUM,
                        rawEquationLatex = rawLatex,
                        steps = steps,
                        finalAnswerLatex = finalValStr
                    )
                }
            }
        }

        throw UnsupportedProblemException("Bu limit çözülemedi veya tanımsız.")
    }

    private fun substitute(expr: Expr, varName: String, valueExpr: Expr): Expr = when (expr) {
        is Expr.Num -> expr
        is Expr.Variable -> if (expr.name == varName) valueExpr else expr
        is Expr.Add -> Expr.Add(substitute(expr.left, varName, valueExpr), substitute(expr.right, varName, valueExpr))
        is Expr.Sub -> Expr.Sub(substitute(expr.left, varName, valueExpr), substitute(expr.right, varName, valueExpr))
        is Expr.Mul -> Expr.Mul(substitute(expr.left, varName, valueExpr), substitute(expr.right, varName, valueExpr))
        is Expr.Div -> Expr.Div(substitute(expr.left, varName, valueExpr), substitute(expr.right, varName, valueExpr))
        is Expr.Pow -> Expr.Pow(substitute(expr.base, varName, valueExpr), substitute(expr.exponent, varName, valueExpr))
        is Expr.Neg -> Expr.Neg(substitute(expr.arg, varName, valueExpr))
        is Expr.Log -> Expr.Log(substitute(expr.base, varName, valueExpr), substitute(expr.arg, varName, valueExpr))
        is Expr.Ln -> Expr.Ln(substitute(expr.arg, varName, valueExpr))
        is Expr.Derivative -> Expr.Derivative(substitute(expr.expr, varName, valueExpr), expr.variable)
        is Expr.Limit -> Expr.Limit(substitute(expr.expr, varName, valueExpr), expr.variable, substitute(expr.target, varName, valueExpr))
        is Expr.Integral -> Expr.Integral(
            substitute(expr.expr, varName, valueExpr),
            expr.variable,
            expr.lowerBound?.let { substitute(it, varName, valueExpr) },
            expr.upperBound?.let { substitute(it, varName, valueExpr) }
        )
        is Expr.Trig -> Expr.Trig(expr.func, substitute(expr.arg, varName, valueExpr))
    }

    private fun evaluate(expr: Expr): Double? = when (expr) {
        is Expr.Num -> expr.value
        is Expr.Variable -> {
            if (expr.name.lowercase() == "pi") PI else null
        }
        is Expr.Add -> {
            val l = evaluate(expr.left)
            val r = evaluate(expr.right)
            if (l != null && r != null) l + r else null
        }
        is Expr.Sub -> {
            val l = evaluate(expr.left)
            val r = evaluate(expr.right)
            if (l != null && r != null) l - r else null
        }
        is Expr.Mul -> {
            val l = evaluate(expr.left)
            val r = evaluate(expr.right)
            if (l != null && r != null) l * r else null
        }
        is Expr.Div -> {
            val l = evaluate(expr.left)
            val r = evaluate(expr.right)
            if (l != null && r != null) {
                if (abs(r) < 1e-15) null else l / r
            } else null
        }
        is Expr.Pow -> {
            val b = evaluate(expr.base)
            val e = evaluate(expr.exponent)
            if (b != null && e != null) b.pow(e) else null
        }
        is Expr.Neg -> {
            val a = evaluate(expr.arg)
            if (a != null) -a else null
        }
        is Expr.Trig -> {
            val a = evaluate(expr.arg)
            if (a != null) {
                when (expr.func.lowercase()) {
                    "sin" -> sin(a)
                    "cos" -> cos(a)
                    "tan" -> tan(a)
                    "cot" -> {
                        val t = tan(a)
                        if (abs(t) < 1e-15) null else 1.0 / t
                    }
                    else -> null
                }
            } else null
        }
        else -> null
    }
}
