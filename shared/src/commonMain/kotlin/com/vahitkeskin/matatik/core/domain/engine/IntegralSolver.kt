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
 * Belirli ve belirsiz integral problemlerini sembolik ve sayısal olarak çözen motor.
 */
class IntegralSolver : Solver {
    override val topic: MathTopic = MathTopic.CALCULUS_INTEGRAL

    override fun canSolve(equation: Equation): Boolean {
        val rhsSentinel = equation.rhs is Expr.Num && (equation.rhs as Expr.Num).value == 0.0
        return rhsSentinel && equation.lhs is Expr.Integral
    }

    override fun solve(equation: Equation): MathematicalSolution {
        val integralNode = equation.lhs as? Expr.Integral
            ?: throw UnsupportedProblemException("İntegral ifadesi bekleniyor")
        val varName = integralNode.variable
        val innerExpr = integralNode.expr
        val isDefinite = integralNode.lowerBound != null && integralNode.upperBound != null
        val rawLatex = LatexRenderer.render(integralNode)
        val steps = mutableListOf<SolutionStep>()

        // 1. Adım: Orijinal İntegral İfadesi
        steps += SolutionStep(
            stepNumber = 1,
            ruleApplied = "Verilen İntegral",
            descriptionLocalizationKey = RuleKeys.INTEGRAL_ORIGINAL,
            formulaUsedLatex = "",
            currentExpressionLatex = rawLatex,
            highlightedParts = listOf(
                HighlightedCoordinate(varName, HighlightColors.VARIABLE, AnimationTypes.GLOW)
            )
        )

        // Belirsiz integrali hesapla
        val indefiniteIntegral = integrate(innerExpr, varName)
        val simplifiedF = DerivativeSolver().simplify(indefiniteIntegral)
        val F_latex = LatexRenderer.render(simplifiedF)

        if (!isDefinite) {
            // Belirsiz İntegral Çözümü
            val finalAnsLatex = "$F_latex + C"
            steps += SolutionStep(
                stepNumber = 2,
                ruleApplied = "İntegral Kuralları",
                descriptionLocalizationKey = RuleKeys.INTEGRAL_POWER_RULE,
                formulaUsedLatex = "\\int x^n \\, dx = \\frac{x^{n+1}}{n+1}",
                currentExpressionLatex = finalAnsLatex,
                highlightedParts = listOf(
                    HighlightedCoordinate(F_latex, HighlightColors.OPERATION, AnimationTypes.SCALE)
                )
            )
            steps += SolutionStep(
                stepNumber = 3,
                ruleApplied = "Sonuç",
                descriptionLocalizationKey = RuleKeys.INTEGRAL_SOLUTION,
                formulaUsedLatex = "",
                currentExpressionLatex = finalAnsLatex,
                highlightedParts = listOf(
                    HighlightedCoordinate("C", HighlightColors.RESULT, AnimationTypes.GLOW)
                )
            )

            return MathematicalSolution(
                id = "int_${abs(rawLatex.hashCode())}",
                topic = topic,
                baseDifficulty = DifficultyLevel.MEDIUM,
                rawEquationLatex = rawLatex,
                steps = steps,
                finalAnswerLatex = finalAnsLatex
            )
        } else {
            // Belirli İntegral Çözümü
            val lower = integralNode.lowerBound!!
            val upper = integralNode.upperBound!!
            val lowerLatex = LatexRenderer.render(lower)
            val upperLatex = LatexRenderer.render(upper)

            steps += SolutionStep(
                stepNumber = 2,
                ruleApplied = "Belirsiz İntegral Bulma",
                descriptionLocalizationKey = RuleKeys.INTEGRAL_POWER_RULE,
                formulaUsedLatex = "\\int f(x)\\,dx = F(x)",
                currentExpressionLatex = "F($varName) = $F_latex",
                highlightedParts = listOf(
                    HighlightedCoordinate(F_latex, HighlightColors.OPERATION, AnimationTypes.SCALE)
                )
            )

            // Newton-Leibniz Gösterimi
            val newtonLeibnizLatex = "\\left[ $F_latex \\right]_{$lowerLatex}^{$upperLatex}"
            steps += SolutionStep(
                stepNumber = 3,
                ruleApplied = "Newton-Leibniz Kuralı",
                descriptionLocalizationKey = RuleKeys.INTEGRAL_DEFINITE_EVAL,
                formulaUsedLatex = "\\int_a^b f(x)\\,dx = F(b) - F(a)",
                currentExpressionLatex = newtonLeibnizLatex,
                highlightedParts = emptyList()
            )

            // Üst sınır ve alt sınır değerlerini yerine koy
            val F_upper = substitute(simplifiedF, varName, upper)
            val F_lower = substitute(simplifiedF, varName, lower)
            val valUpper = evaluate(F_upper)
            val valLower = evaluate(F_lower)

            if (valUpper != null && valLower != null) {
                val resultVal = valUpper - valLower
                val resultStr = formatNumber(resultVal)
                val evaluationStepsLatex = "F($upperLatex) - F($lowerLatex) = ${formatNumber(valUpper)} - ${formatNumber(valLower)} = $resultStr"

                steps += SolutionStep(
                    stepNumber = 4,
                    ruleApplied = "Sınırları Yerine Koyma",
                    descriptionLocalizationKey = RuleKeys.INTEGRAL_SIMPLIFY,
                    formulaUsedLatex = "",
                    currentExpressionLatex = evaluationStepsLatex,
                    highlightedParts = listOf(
                        HighlightedCoordinate(resultStr, HighlightColors.RESULT, AnimationTypes.SCALE)
                    )
                )

                steps += SolutionStep(
                    stepNumber = 5,
                    ruleApplied = "Sonuç",
                    descriptionLocalizationKey = RuleKeys.INTEGRAL_SOLUTION,
                    formulaUsedLatex = "",
                    currentExpressionLatex = resultStr,
                    highlightedParts = listOf(
                        HighlightedCoordinate(resultStr, HighlightColors.RESULT, AnimationTypes.GLOW)
                    )
                )

                return MathematicalSolution(
                    id = "int_${abs(rawLatex.hashCode())}",
                    topic = topic,
                    baseDifficulty = DifficultyLevel.HARD,
                    rawEquationLatex = rawLatex,
                    steps = steps,
                    finalAnswerLatex = resultStr
                )
            }
        }

        throw UnsupportedProblemException("Bu integral çözülemedi.")
    }

    private fun integrate(expr: Expr, varName: String): Expr = when (expr) {
        // int(c, x) = c * x
        is Expr.Num -> Expr.Mul(expr, Expr.Variable(varName))

        is Expr.Variable -> {
            if (expr.name == varName) {
                // int(x, x) = x^2 / 2
                Expr.Div(Expr.Pow(expr, Exprs.num(2)), Exprs.num(2))
            } else {
                // int(y, x) = y * x
                Expr.Mul(expr, Expr.Variable(varName))
            }
        }

        // int(f + g) = int(f) + int(g)
        is Expr.Add -> Expr.Add(integrate(expr.left, varName), integrate(expr.right, varName))

        // int(f - g) = int(f) - int(g)
        is Expr.Sub -> Expr.Sub(integrate(expr.left, varName), integrate(expr.right, varName))

        // int(-f) = -int(f)
        is Expr.Neg -> Expr.Neg(integrate(expr.arg, varName))

        // int(c * f) = c * int(f)
        is Expr.Mul -> {
            val leftConst = isConstant(expr.left, varName)
            val rightConst = isConstant(expr.right, varName)
            when {
                leftConst -> Expr.Mul(expr.left, integrate(expr.right, varName))
                rightConst -> Expr.Mul(integrate(expr.left, varName), expr.right)
                else -> throw UnsupportedProblemException("Kısmi integral şimdilik desteklenmiyor.")
            }
        }

        // int(f / c) = int(f) / c
        is Expr.Div -> {
            if (isConstant(expr.right, varName)) {
                Expr.Div(integrate(expr.left, varName), expr.right)
            } else {
                throw UnsupportedProblemException("Karmaşık integral bölümü desteklenmiyor.")
            }
        }

        // int(x^n) = x^(n+1) / (n+1)
        is Expr.Pow -> {
            val base = expr.base
            val exponent = expr.exponent
            if (base is Expr.Variable && base.name == varName && exponent is Expr.Num) {
                val n = exponent.value
                if (n == -1.0) {
                    Expr.Ln(base) // int(x^-1) = ln(x)
                } else {
                    Expr.Div(Expr.Pow(base, Exprs.num(n + 1)), Exprs.num(n + 1))
                }
            } else if (isConstant(base, varName) && isConstant(exponent, varName)) {
                // sabit^sabit -> c * x
                Expr.Mul(expr, Expr.Variable(varName))
            } else {
                throw UnsupportedProblemException("Karmaşık integral üssü desteklenmiyor.")
            }
        }

        is Expr.Trig -> {
            val arg = expr.arg
            if (arg is Expr.Variable && arg.name == varName) {
                when (expr.func.lowercase()) {
                    "sin" -> Expr.Neg(Expr.Trig("cos", arg)) // int(sin(x)) = -cos(x)
                    "cos" -> Expr.Trig("sin", arg)          // int(cos(x)) = sin(x)
                    else -> throw UnsupportedProblemException("Bu trigonometrik integral şimdilik desteklenmiyor.")
                }
            } else {
                throw UnsupportedProblemException("Sadece lineer açılı trigonometrik integral destekleniyor.")
            }
        }

        else -> throw UnsupportedProblemException("Bu ifadenin integrali şimdilik desteklenmiyor.")
    }

    private fun isConstant(expr: Expr, varName: String): Boolean = when (expr) {
        is Expr.Num -> true
        is Expr.Variable -> expr.name != varName
        is Expr.Add -> isConstant(expr.left, varName) && isConstant(expr.right, varName)
        is Expr.Sub -> isConstant(expr.left, varName) && isConstant(expr.right, varName)
        is Expr.Mul -> isConstant(expr.left, varName) && isConstant(expr.right, varName)
        is Expr.Div -> isConstant(expr.left, varName) && isConstant(expr.right, varName)
        is Expr.Pow -> isConstant(expr.base, varName) && isConstant(expr.exponent, varName)
        is Expr.Neg -> isConstant(expr.arg, varName)
        is Expr.Log -> isConstant(expr.base, varName) && isConstant(expr.arg, varName)
        is Expr.Ln -> isConstant(expr.arg, varName)
        is Expr.Derivative -> false
        is Expr.Limit -> isConstant(expr.expr, varName) && isConstant(expr.target, varName)
        is Expr.Integral -> isConstant(expr.expr, varName) && (expr.lowerBound == null || isConstant(expr.lowerBound, varName)) && (expr.upperBound == null || isConstant(expr.upperBound, varName))
        is Expr.Trig -> isConstant(expr.arg, varName)
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
