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
import kotlin.math.asin
import kotlin.math.acos
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.PI

/**
 * Trigonometrik sadeleştirmeleri ve temel trigonometrik denklemleri (örn. sin(x) = 0.5) çözen motor.
 */
class TrigSolver : Solver {
    override val topic: MathTopic = MathTopic.BASIC_ALGEBRA // or generic math topic

    override fun canSolve(equation: Equation): Boolean {
        // Durum 1: lhs sin(x) = rhs 0.5 gibi bir denklem
        if (equation.lhs is Expr.Trig && equation.lhs.arg is Expr.Variable && equation.rhs is Expr.Num) {
            return true
        }
        // Durum 2: sin(pi/2) gibi doğrudan hesaplanabilir bir ifade
        return containsTrig(equation.lhs) && equation.rhs is Expr.Num && (equation.rhs as Expr.Num).value == 0.0
    }

    override fun solve(equation: Equation): MathematicalSolution {
        val rawLatex = LatexRenderer.render(equation)
        val steps = mutableListOf<SolutionStep>()

        // 1. Adım: Başlangıç ifadesi
        steps += SolutionStep(
            stepNumber = 1,
            ruleApplied = "Verilen İfade",
            descriptionLocalizationKey = RuleKeys.TRIG_ORIGINAL,
            formulaUsedLatex = "",
            currentExpressionLatex = rawLatex,
            highlightedParts = emptyList()
        )

        // Durum 1: sin(x) = C şeklinde bir denklem çözümü
        if (equation.lhs is Expr.Trig && equation.lhs.arg is Expr.Variable && equation.rhs is Expr.Num) {
            val func = equation.lhs.func.lowercase()
            val varName = (equation.lhs.arg as Expr.Variable).name
            val value = equation.rhs.value

            val stepsList = solveTrigEquation(func, varName, value)
            steps.addAll(stepsList.mapIndexed { index, step ->
                SolutionStep(
                    stepNumber = index + 2,
                    ruleApplied = step.ruleApplied,
                    descriptionLocalizationKey = step.descriptionLocalizationKey,
                    formulaUsedLatex = step.formulaUsedLatex,
                    currentExpressionLatex = step.currentExpressionLatex,
                    highlightedParts = step.highlightedParts
                )
            })

            val finalAns = steps.last().currentExpressionLatex

            return MathematicalSolution(
                id = "trig_${abs(rawLatex.hashCode())}",
                topic = topic,
                baseDifficulty = DifficultyLevel.MEDIUM,
                rawEquationLatex = rawLatex,
                steps = steps,
                finalAnswerLatex = finalAns
            )
        }

        // Durum 2: İfade sadeleştirme/hesaplama (Örn: sin(pi/2) = 1)
        val evaluated = simplifyTrig(equation.lhs)
        val resultLatex = LatexRenderer.render(evaluated)

        steps += SolutionStep(
            stepNumber = 2,
            ruleApplied = "Trigonometrik Hesaplama",
            descriptionLocalizationKey = RuleKeys.TRIG_EVALUATE,
            formulaUsedLatex = "",
            currentExpressionLatex = resultLatex,
            highlightedParts = listOf(
                HighlightedCoordinate(resultLatex, HighlightColors.RESULT, AnimationTypes.SCALE)
            )
        )

        steps += SolutionStep(
            stepNumber = 3,
            ruleApplied = "Sonuç",
            descriptionLocalizationKey = RuleKeys.TRIG_SOLUTION,
            formulaUsedLatex = "",
            currentExpressionLatex = resultLatex,
            highlightedParts = listOf(
                HighlightedCoordinate(resultLatex, HighlightColors.RESULT, AnimationTypes.GLOW)
            )
        )

        return MathematicalSolution(
            id = "trig_${abs(rawLatex.hashCode())}",
            topic = topic,
            baseDifficulty = DifficultyLevel.EASY,
            rawEquationLatex = rawLatex,
            steps = steps,
            finalAnswerLatex = resultLatex
        )
    }

    private fun containsTrig(expr: Expr): Boolean = when (expr) {
        is Expr.Trig -> true
        is Expr.Num, is Expr.Variable -> false
        is Expr.Add -> containsTrig(expr.left) || containsTrig(expr.right)
        is Expr.Sub -> containsTrig(expr.left) || containsTrig(expr.right)
        is Expr.Mul -> containsTrig(expr.left) || containsTrig(expr.right)
        is Expr.Div -> containsTrig(expr.left) || containsTrig(expr.right)
        is Expr.Pow -> containsTrig(expr.base) || containsTrig(expr.exponent)
        is Expr.Neg -> containsTrig(expr.arg)
        is Expr.Log -> containsTrig(expr.base) || containsTrig(expr.arg)
        is Expr.Ln -> containsTrig(expr.arg)
        is Expr.Derivative -> containsTrig(expr.expr)
        is Expr.Limit -> containsTrig(expr.expr) || containsTrig(expr.target)
        is Expr.Integral -> containsTrig(expr.expr) || (expr.lowerBound?.let { containsTrig(it) } ?: false) || (expr.upperBound?.let { containsTrig(it) } ?: false)
    }

    private fun solveTrigEquation(func: String, varName: String, value: Double): List<SolutionStep> {
        val steps = mutableListOf<SolutionStep>()
        val rad: Double
        val radStr: String

        // Açı çözüm değerlerini tam sembolik yapalım (0.5 -> pi/6)
        when (func) {
            "sin" -> {
                if (value < -1.0 || value > 1.0) {
                    throw UnsupportedProblemException("Sinüs değeri -1 ile 1 arasında olmalıdır.")
                }
                rad = asin(value)
                radStr = getAngleLatex(rad)

                val sol1 = "$varName = $radStr + 2k\\pi"
                val sol2 = "$varName = \\pi - $radStr + 2k\\pi"
                steps += SolutionStep(
                    stepNumber = 1,
                    ruleApplied = "Genel Çözüm Kümeleri",
                    descriptionLocalizationKey = RuleKeys.TRIG_IDENTITY,
                    formulaUsedLatex = "\\sin(x) = a \\Rightarrow x = \\theta + 2k\\pi \\text{ veya } x = \\pi - \\theta + 2k\\pi",
                    currentExpressionLatex = "$sol1 \\quad \\text{veya} \\quad $sol2",
                    highlightedParts = emptyList()
                )
            }
            "cos" -> {
                if (value < -1.0 || value > 1.0) {
                    throw UnsupportedProblemException("Kosinüs değeri -1 ile 1 arasında olmalıdır.")
                }
                rad = acos(value)
                radStr = getAngleLatex(rad)

                val sol = "$varName = \\pm $radStr + 2k\\pi"
                steps += SolutionStep(
                    stepNumber = 1,
                    ruleApplied = "Genel Çözüm Kümesi",
                    descriptionLocalizationKey = RuleKeys.TRIG_IDENTITY,
                    formulaUsedLatex = "\\cos(x) = a \\Rightarrow x = \\pm\\theta + 2k\\pi",
                    currentExpressionLatex = sol,
                    highlightedParts = emptyList()
                )
            }
            "tan" -> {
                rad = atan(value)
                radStr = getAngleLatex(rad)

                val sol = "$varName = $radStr + k\\pi"
                steps += SolutionStep(
                    stepNumber = 1,
                    ruleApplied = "Genel Çözüm Kümesi",
                    descriptionLocalizationKey = RuleKeys.TRIG_IDENTITY,
                    formulaUsedLatex = "\\tan(x) = a \\Rightarrow x = \\theta + k\\pi",
                    currentExpressionLatex = sol,
                    highlightedParts = emptyList()
                )
            }
            "cot" -> {
                if (abs(value) < 1e-15) {
                    radStr = "\\frac{\\pi}{2}"
                } else {
                    rad = atan(1.0 / value)
                    radStr = getAngleLatex(rad)
                }

                val sol = "$varName = $radStr + k\\pi"
                steps += SolutionStep(
                    stepNumber = 1,
                    ruleApplied = "Genel Çözüm Kümesi",
                    descriptionLocalizationKey = RuleKeys.TRIG_IDENTITY,
                    formulaUsedLatex = "\\cot(x) = a \\Rightarrow x = \\theta + k\\pi",
                    currentExpressionLatex = sol,
                    highlightedParts = emptyList()
                )
            }
            else -> throw UnsupportedProblemException("Desteklenmeyen fonksiyon: $func")
        }

        return steps
    }

    private fun getAngleLatex(rad: Double): String {
        val deg = Math.round(Math.toDegrees(rad)).toInt()
        return when (deg) {
            0 -> "0"
            30 -> "\\frac{\\pi}{6}"
            45 -> "\\frac{\\pi}{4}"
            60 -> "\\frac{\\pi}{3}"
            90 -> "\\frac{\\pi}{2}"
            120 -> "\\frac{2\\pi}{3}"
            135 -> "\\frac{3\\pi}{4}"
            150 -> "\\frac{5\\pi}{6}"
            180 -> "\\pi"
            -30 -> "-\\frac{\\pi}{6}"
            -45 -> "-\\frac{\\pi}{4}"
            -60 -> "-\\frac{\\pi}{3}"
            -90 -> "-\\frac{\\pi}{2}"
            else -> formatNumber(rad)
        }
    }

    private fun simplifyTrig(expr: Expr): Expr = when (expr) {
        is Expr.Trig -> {
            val evaluatedArg = evaluate(expr.arg)
            if (evaluatedArg != null) {
                val value = when (expr.func.lowercase()) {
                    "sin" -> sin(evaluatedArg)
                    "cos" -> cos(evaluatedArg)
                    "tan" -> tan(evaluatedArg)
                    "cot" -> {
                        val t = tan(evaluatedArg)
                        if (abs(t) < 1e-15) Double.NaN else 1.0 / t
                    }
                    else -> Double.NaN
                }
                if (!value.isNaN()) Expr.Num(value) else expr
            } else {
                Expr.Trig(expr.func, simplifyTrig(expr.arg))
            }
        }
        is Expr.Add -> {
            // sin(x)^2 + cos(x)^2 -> 1.0 kimlik sadeleştirmesi
            val l = simplifyTrig(expr.left)
            val r = simplifyTrig(expr.right)
            if (isSinSquared(l) && isCosSquared(r) && sameArg(l, r)) {
                Expr.Num(1.0)
            } else if (isCosSquared(l) && isSinSquared(r) && sameArg(l, r)) {
                Expr.Num(1.0)
            } else {
                Expr.Add(l, r)
            }
        }
        is Expr.Sub -> Expr.Sub(simplifyTrig(expr.left), simplifyTrig(expr.right))
        is Expr.Mul -> Expr.Mul(simplifyTrig(expr.left), simplifyTrig(expr.right))
        is Expr.Div -> Expr.Div(simplifyTrig(expr.left), simplifyTrig(expr.right))
        is Expr.Pow -> Expr.Pow(simplifyTrig(expr.base), simplifyTrig(expr.exponent))
        is Expr.Neg -> Expr.Neg(simplifyTrig(expr.arg))
        else -> expr
    }

    private fun isSinSquared(expr: Expr): Boolean {
        return expr is Expr.Pow && expr.base is Expr.Trig && expr.base.func == "sin" && expr.exponent is Expr.Num && expr.exponent.value == 2.0
    }

    private fun isCosSquared(expr: Expr): Boolean {
        return expr is Expr.Pow && expr.base is Expr.Trig && expr.base.func == "cos" && expr.exponent is Expr.Num && expr.exponent.value == 2.0
    }

    private fun sameArg(e1: Expr, e2: Expr): Boolean {
        val a1 = ((e1 as Expr.Pow).base as Expr.Trig).arg
        val a2 = ((e2 as Expr.Pow).base as Expr.Trig).arg
        return a1 == a2
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
