package com.vahitkeskin.matatik.core.domain.ast

/**
 * Bir [Expr] ağacını öncelik (precedence) duyarlı, en sade parantezleme ile
 * LaTeX dizgesine çevirir.
 *
 * Öncelik düzeyleri: + - (1), * / (2), tekli - (3), ^ (4), atom (5).
 */
object LatexRenderer {

    fun render(expr: Expr): String = renderWithPrecedence(expr, 0)

    fun render(equation: Equation): String =
        "${render(equation.lhs)} = ${render(equation.rhs)}"

    private fun precedence(expr: Expr): Int = when (expr) {
        is Expr.Add, is Expr.Sub -> 1
        is Expr.Mul, is Expr.Div -> 2
        is Expr.Neg -> 3
        is Expr.Pow -> 4
        is Expr.Num, is Expr.Variable, is Expr.Log, is Expr.Ln, is Expr.Derivative,
        is Expr.Limit, is Expr.Integral, is Expr.Trig -> 5
    }

    private fun wrap(child: Expr, parentPrec: Int): String {
        val rendered = renderWithPrecedence(child, parentPrec)
        return if (precedence(child) < parentPrec) "\\left($rendered\\right)" else rendered
    }

    private fun renderWithPrecedence(expr: Expr, parentPrec: Int): String = when (expr) {
        is Expr.Num -> formatNumber(expr.value)
        is Expr.Variable -> expr.name
        is Expr.Add -> "${wrap(expr.left, 1)} + ${wrap(expr.right, 1)}"
        is Expr.Sub -> "${wrap(expr.left, 1)} - ${wrap(expr.right, 2)}"
        is Expr.Mul -> renderMul(expr)
        is Expr.Div -> "\\frac{${render(expr.left)}}{${render(expr.right)}}"
        is Expr.Pow -> "${wrap(expr.base, 5)}^{${render(expr.exponent)}}"
        is Expr.Neg -> "-${wrap(expr.arg, 3)}"
        is Expr.Log -> "\\log_{${render(expr.base)}}\\left(${render(expr.arg)}\\right)"
        is Expr.Ln -> "\\ln\\left(${render(expr.arg)}\\right)"
        is Expr.Derivative -> "\\frac{d}{d${expr.variable}}\\left(${render(expr.expr)}\\right)"
        is Expr.Limit -> "\\lim_{${expr.variable} \\to ${render(expr.target)}}\\left(${render(expr.expr)}\\right)"
        is Expr.Integral -> {
            val varPart = if (expr.variable.isNotEmpty()) "\\, d${expr.variable}" else ""
            if (expr.lowerBound != null && expr.upperBound != null) {
                "\\int_{${render(expr.lowerBound)}}^{${render(expr.upperBound)}} {${render(expr.expr)}}$varPart"
            } else {
                "\\int {${render(expr.expr)}}$varPart"
            }
        }
        is Expr.Trig -> "\\${expr.func}\\left(${render(expr.arg)}\\right)"
    }

    /** Katsayı-değişken çarpımlarında "\\cdot" yerine bitişik gösterim (2x) kullanılır. */
    private fun renderMul(mul: Expr.Mul): String {
        val left = mul.left
        val right = mul.right
        val implicit = (left is Expr.Num && (right is Expr.Variable || right is Expr.Pow)) ||
            (left is Expr.Num && right is Expr.Log) || (left is Expr.Num && right is Expr.Ln)
        val l = wrap(left, 2)
        val r = wrap(right, 2)
        return if (implicit) "$l$r" else "$l \\cdot $r"
    }
}
