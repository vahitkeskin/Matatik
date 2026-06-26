package com.vahitkeskin.matatik.core.domain.engine

import com.vahitkeskin.matatik.core.domain.ast.Equation
import com.vahitkeskin.matatik.core.domain.ast.Expr
import com.vahitkeskin.matatik.core.domain.ast.Exprs
import com.vahitkeskin.matatik.core.domain.ast.LatexRenderer
import com.vahitkeskin.matatik.core.domain.model.DifficultyLevel
import com.vahitkeskin.matatik.core.domain.model.HighlightedCoordinate
import com.vahitkeskin.matatik.core.domain.model.MathTopic
import com.vahitkeskin.matatik.core.domain.model.MathematicalSolution
import com.vahitkeskin.matatik.core.domain.model.SolutionStep
import kotlin.math.abs

/**
 * Polinom ifadelerinin türevini adım adım çözen motor.
 *
 * Desteklenen kurallar:
 *  - **Kuvvet kuralı:** d/dx[x^n] = n·x^(n-1)
 *  - **Sabit kuralı:** d/dx[c] = 0
 *  - **Sabit çarpan kuralı:** d/dx[c·f(x)] = c·f'(x)
 *  - **Toplam/fark kuralı:** d/dx[f ± g] = f' ± g'
 *  - **Lineer terim:** d/dx[x] = 1
 */
class DerivativeSolver : Solver {

    override val topic: MathTopic = MathTopic.CALCULUS_DERIVATIVE

    override fun canSolve(equation: Equation): Boolean {
        // rhs = 0 sentinel + lhs Derivative düğümü
        val rhsSentinel = equation.rhs is Expr.Num && (equation.rhs as Expr.Num).value == 0.0
        if (!rhsSentinel) return false
        return equation.lhs is Expr.Derivative && isDifferentiable(
            (equation.lhs as Expr.Derivative).expr
        )
    }

    override fun solve(equation: Equation): MathematicalSolution {
        val deriv = equation.lhs as? Expr.Derivative
            ?: throw UnsupportedProblemException("Türev ifadesi bekleniyor")
        val varName = deriv.variable
        val innerExpr = deriv.expr
        val rawLatex = LatexRenderer.render(deriv)
        val steps = mutableListOf<SolutionStep>()

        // 1) Verilen ifade
        steps += SolutionStep(
            stepNumber = 1,
            ruleApplied = "Verilen İfade",
            descriptionLocalizationKey = RuleKeys.DERIVATIVE_ORIGINAL,
            formulaUsedLatex = "",
            currentExpressionLatex = rawLatex,
            highlightedParts = listOf(
                HighlightedCoordinate(varName, HighlightColors.VARIABLE, AnimationTypes.GLOW)
            )
        )

        // 2) Uygulanacak kuralların açıklaması
        val ruleLatex = describeRules(innerExpr, varName)
        if (ruleLatex != null) {
            steps += SolutionStep(
                stepNumber = 2,
                ruleApplied = "Türev Kuralları",
                descriptionLocalizationKey = ruleLatex.first,
                formulaUsedLatex = ruleLatex.second,
                currentExpressionLatex = rawLatex,
                highlightedParts = listOf(
                    HighlightedCoordinate(varName, HighlightColors.OPERATION, AnimationTypes.TRANSLATE)
                )
            )
        }

        // 3) Türevi hesapla
        val result = differentiate(innerExpr, varName)
        val simplified = simplify(result)
        val resultLatex = LatexRenderer.render(simplified)

        steps += SolutionStep(
            stepNumber = steps.size + 1,
            ruleApplied = "Türev Hesaplama",
            descriptionLocalizationKey = RuleKeys.DERIVATIVE_SIMPLIFY,
            formulaUsedLatex = "",
            currentExpressionLatex = resultLatex,
            highlightedParts = listOf(
                HighlightedCoordinate(resultLatex, HighlightColors.OPERATION, AnimationTypes.SCALE)
            )
        )

        // 4) Sonuç
        val finalLatex = resultLatex
        steps += SolutionStep(
            stepNumber = steps.size + 1,
            ruleApplied = "Sonuç",
            descriptionLocalizationKey = RuleKeys.DERIVATIVE_SOLUTION,
            formulaUsedLatex = "",
            currentExpressionLatex = finalLatex,
            highlightedParts = listOf(
                HighlightedCoordinate(finalLatex, HighlightColors.RESULT, AnimationTypes.SCALE)
            )
        )

        return MathematicalSolution(
            id = "sol_${abs(rawLatex.hashCode())}",
            topic = topic,
            baseDifficulty = difficultyOf(innerExpr),
            rawEquationLatex = rawLatex,
            steps = steps,
            finalAnswerLatex = finalLatex
        )
    }

    /**
     * Bir ifadenin türevini sembolik olarak hesaplar.
     * Ağaç üzerinde özyinelemeli dönüşüm uygular.
     */
    internal fun differentiate(expr: Expr, varName: String): Expr = when (expr) {
        // d/dx[c] = 0
        is Expr.Num -> Exprs.ZERO

        // d/dx[x] = 1, d/dx[y] = 0 (farklı değişken)
        is Expr.Variable -> if (expr.name == varName) Exprs.ONE else Exprs.ZERO

        // d/dx[f + g] = f' + g'
        is Expr.Add -> Expr.Add(
            differentiate(expr.left, varName),
            differentiate(expr.right, varName)
        )

        // d/dx[f - g] = f' - g'
        is Expr.Sub -> Expr.Sub(
            differentiate(expr.left, varName),
            differentiate(expr.right, varName)
        )

        // d/dx[-f] = -f'
        is Expr.Neg -> Expr.Neg(differentiate(expr.arg, varName))

        // d/dx[c * f] veya d/dx[f * g] (basit çarpım kuralı)
        is Expr.Mul -> {
            val leftConst = isConstant(expr.left, varName)
            val rightConst = isConstant(expr.right, varName)
            when {
                leftConst && rightConst -> Exprs.ZERO
                leftConst -> Expr.Mul(expr.left, differentiate(expr.right, varName))
                rightConst -> Expr.Mul(differentiate(expr.left, varName), expr.right)
                else -> {
                    // Çarpım kuralı: f'g + fg'
                    Expr.Add(
                        Expr.Mul(differentiate(expr.left, varName), expr.right),
                        Expr.Mul(expr.left, differentiate(expr.right, varName))
                    )
                }
            }
        }

        // d/dx[f / g] — bölüm kuralı (basit: g sabitken)
        is Expr.Div -> {
            if (isConstant(expr.right, varName)) {
                Expr.Div(differentiate(expr.left, varName), expr.right)
            } else {
                // (f'g - fg') / g^2
                Expr.Div(
                    Expr.Sub(
                        Expr.Mul(differentiate(expr.left, varName), expr.right),
                        Expr.Mul(expr.left, differentiate(expr.right, varName))
                    ),
                    Expr.Pow(expr.right, Exprs.num(2))
                )
            }
        }

        // d/dx[x^n] = n * x^(n-1)  (n sabit)
        is Expr.Pow -> {
            val baseIsVar = !isConstant(expr.base, varName)
            val expIsConst = isConstant(expr.exponent, varName)
            when {
                baseIsVar && expIsConst -> {
                    // Kuvvet kuralı: n * base' * base^(n-1)
                    val baseDeriv = differentiate(expr.base, varName)
                    Expr.Mul(
                        Expr.Mul(expr.exponent, Expr.Pow(expr.base, Expr.Sub(expr.exponent, Exprs.ONE))),
                        baseDeriv
                    )
                }
                !baseIsVar && !expIsConst -> Exprs.ZERO // sabit^sabit
                else -> throw UnsupportedProblemException("Karmaşık üs türevi desteklenmiyor")
            }
        }

        is Expr.Trig -> {
            val argDeriv = differentiate(expr.arg, varName)
            when (expr.func) {
                "sin" -> Expr.Mul(Expr.Trig("cos", expr.arg), argDeriv)
                "cos" -> Expr.Mul(Expr.Neg(Expr.Trig("sin", expr.arg)), argDeriv)
                "tan" -> Expr.Mul(Expr.Add(Exprs.ONE, Expr.Pow(Expr.Trig("tan", expr.arg), Exprs.num(2))), argDeriv)
                "cot" -> Expr.Mul(Expr.Neg(Expr.Add(Exprs.ONE, Expr.Pow(Expr.Trig("cot", expr.arg), Exprs.num(2)))), argDeriv)
                else -> throw UnsupportedProblemException("Desteklenmeyen trigonometrik türev: ${expr.func}")
            }
        }

        // Logaritma ve diğer fonksiyonlar şimdilik desteklenmiyor
        is Expr.Log, is Expr.Ln, is Expr.Derivative, is Expr.Limit, is Expr.Integral ->
            throw UnsupportedProblemException("Bu ifadenin türevi henüz desteklenmiyor")
    }

    /** İfade, verilen değişkene göre sabit mi? */
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

    /** İfade diferansiyellenebilir mi (türev çözücünün desteklediği alt küme)? */
    private fun isDifferentiable(expr: Expr): Boolean = when (expr) {
        is Expr.Num, is Expr.Variable -> true
        is Expr.Add -> isDifferentiable(expr.left) && isDifferentiable(expr.right)
        is Expr.Sub -> isDifferentiable(expr.left) && isDifferentiable(expr.right)
        is Expr.Mul -> isDifferentiable(expr.left) && isDifferentiable(expr.right)
        is Expr.Div -> isDifferentiable(expr.left) && isDifferentiable(expr.right)
        is Expr.Pow -> isDifferentiable(expr.base) && isDifferentiable(expr.exponent)
        is Expr.Neg -> isDifferentiable(expr.arg)
        is Expr.Trig -> isDifferentiable(expr.arg)
        is Expr.Log, is Expr.Ln, is Expr.Derivative, is Expr.Limit, is Expr.Integral -> false
    }

    /** Basit cebirsel sadeleştirme. */
    internal fun simplify(expr: Expr): Expr = when (expr) {
        is Expr.Add -> {
            val l = simplify(expr.left)
            val r = simplify(expr.right)
            when {
                l == Exprs.ZERO -> r
                r == Exprs.ZERO -> l
                l is Expr.Num && r is Expr.Num -> Exprs.num(l.value + r.value)
                else -> Expr.Add(l, r)
            }
        }
        is Expr.Sub -> {
            val l = simplify(expr.left)
            val r = simplify(expr.right)
            when {
                r == Exprs.ZERO -> l
                l == Exprs.ZERO -> simplify(Expr.Neg(r))
                l is Expr.Num && r is Expr.Num -> Exprs.num(l.value - r.value)
                else -> Expr.Sub(l, r)
            }
        }
        is Expr.Mul -> {
            val l = simplify(expr.left)
            val r = simplify(expr.right)
            when {
                l == Exprs.ZERO || r == Exprs.ZERO -> Exprs.ZERO
                l == Exprs.ONE -> r
                r == Exprs.ONE -> l
                l is Expr.Num && r is Expr.Num -> Exprs.num(l.value * r.value)
                // Sabit * (Sabit * ifade) → (iki sabitin çarpımı) * ifade
                l is Expr.Num && r is Expr.Mul && r.left is Expr.Num -> {
                    simplify(Expr.Mul(Exprs.num(l.value * (r.left as Expr.Num).value), r.right))
                }
                else -> Expr.Mul(l, r)
            }
        }
        is Expr.Div -> {
            val l = simplify(expr.left)
            val r = simplify(expr.right)
            when {
                l == Exprs.ZERO -> Exprs.ZERO
                r == Exprs.ONE -> l
                else -> Expr.Div(l, r)
            }
        }
        is Expr.Pow -> {
            val b = simplify(expr.base)
            val e = simplify(expr.exponent)
            when {
                e == Exprs.ZERO -> Exprs.ONE
                e == Exprs.ONE -> b
                else -> Expr.Pow(b, e)
            }
        }
        is Expr.Neg -> {
            val a = simplify(expr.arg)
            when {
                a == Exprs.ZERO -> Exprs.ZERO
                a is Expr.Neg -> simplify(a.arg) // çift negatif
                a is Expr.Num -> Exprs.num(-a.value)
                else -> Expr.Neg(a)
            }
        }
        else -> expr
    }

    private fun describeRules(expr: Expr, varName: String): Pair<String, String>? {
        return when {
            expr is Expr.Pow -> RuleKeys.DERIVATIVE_POWER_RULE to
                "\\frac{d}{d$varName}\\left($varName^{n}\\right) = n \\cdot $varName^{n-1}"
            expr is Expr.Add || expr is Expr.Sub -> RuleKeys.DERIVATIVE_SUM_RULE to
                "\\frac{d}{d$varName}\\left(f \\pm g\\right) = f' \\pm g'"
            expr is Expr.Mul && (isConstant(expr.left, varName) || isConstant(expr.right, varName)) ->
                RuleKeys.DERIVATIVE_COEFFICIENT_RULE to
                    "\\frac{d}{d$varName}\\left(c \\cdot f\\right) = c \\cdot f'"
            expr is Expr.Num -> RuleKeys.DERIVATIVE_CONSTANT_RULE to
                "\\frac{d}{d$varName}\\left(c\\right) = 0"
            else -> null
        }
    }

    private fun difficultyOf(expr: Expr): DifficultyLevel {
        val depth = exprDepth(expr)
        return when {
            depth <= 2 -> DifficultyLevel.EASY
            depth <= 4 -> DifficultyLevel.MEDIUM
            else -> DifficultyLevel.HARD
        }
    }

    private fun exprDepth(expr: Expr): Int = when (expr) {
        is Expr.Num, is Expr.Variable -> 1
        is Expr.Add -> 1 + maxOf(exprDepth(expr.left), exprDepth(expr.right))
        is Expr.Sub -> 1 + maxOf(exprDepth(expr.left), exprDepth(expr.right))
        is Expr.Mul -> 1 + maxOf(exprDepth(expr.left), exprDepth(expr.right))
        is Expr.Div -> 1 + maxOf(exprDepth(expr.left), exprDepth(expr.right))
        is Expr.Pow -> 1 + maxOf(exprDepth(expr.base), exprDepth(expr.exponent))
        is Expr.Neg -> 1 + exprDepth(expr.arg)
        is Expr.Log -> 1 + maxOf(exprDepth(expr.base), exprDepth(expr.arg))
        is Expr.Ln -> 1 + exprDepth(expr.arg)
        is Expr.Derivative -> 1 + exprDepth(expr.expr)
        is Expr.Limit -> 1 + maxOf(exprDepth(expr.expr), exprDepth(expr.target))
        is Expr.Integral -> 1 + exprDepth(expr.expr) + (expr.lowerBound?.let { exprDepth(it) } ?: 0) + (expr.upperBound?.let { exprDepth(it) } ?: 0)
        is Expr.Trig -> 1 + exprDepth(expr.arg)
    }
}
