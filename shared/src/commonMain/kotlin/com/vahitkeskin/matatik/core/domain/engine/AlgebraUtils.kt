package com.vahitkeskin.matatik.core.domain.engine

import com.vahitkeskin.matatik.core.domain.ast.Expr
import kotlin.math.pow

/**
 * Bir ifadenin tek değişkenli lineer biçimi: `a * x + b`.
 */
data class LinearForm(val a: Double, val b: Double) {
    operator fun plus(o: LinearForm) = LinearForm(a + o.a, b + o.b)
    operator fun minus(o: LinearForm) = LinearForm(a - o.a, b - o.b)
    operator fun unaryMinus() = LinearForm(-a, -b)
}

/** İfade lineer değilse (örn. x^2, x*x, log(x)) fırlatılır. */
class NotLinearException(message: String) : Exception(message)

/**
 * Tek değişkenli cebir için yardımcılar.
 */
object AlgebraUtils {

    /** Ağaçta geçen ilk değişken adını döndürür; yoksa null. */
    fun findVariableName(expr: Expr): String? = when (expr) {
        is Expr.Variable -> expr.name
        is Expr.Num -> null
        is Expr.Add -> findVariableName(expr.left) ?: findVariableName(expr.right)
        is Expr.Sub -> findVariableName(expr.left) ?: findVariableName(expr.right)
        is Expr.Mul -> findVariableName(expr.left) ?: findVariableName(expr.right)
        is Expr.Div -> findVariableName(expr.left) ?: findVariableName(expr.right)
        is Expr.Pow -> findVariableName(expr.base) ?: findVariableName(expr.exponent)
        is Expr.Neg -> findVariableName(expr.arg)
        is Expr.Log -> findVariableName(expr.base) ?: findVariableName(expr.arg)
        is Expr.Ln -> findVariableName(expr.arg)
        is Expr.Derivative -> findVariableName(expr.expr)
    }

    /**
     * İfadeyi `a*x + b` lineer biçimine indirger.
     * Lineer olmayan bir yapı görülürse [NotLinearException] fırlatır.
     */
    fun toLinearForm(expr: Expr): LinearForm = when (expr) {
        is Expr.Num -> LinearForm(0.0, expr.value)
        is Expr.Variable -> LinearForm(1.0, 0.0)
        is Expr.Add -> toLinearForm(expr.left) + toLinearForm(expr.right)
        is Expr.Sub -> toLinearForm(expr.left) - toLinearForm(expr.right)
        is Expr.Neg -> -toLinearForm(expr.arg)
        is Expr.Mul -> {
            val l = toLinearForm(expr.left)
            val r = toLinearForm(expr.right)
            when {
                l.a == 0.0 -> LinearForm(r.a * l.b, r.b * l.b) // sol sabit
                r.a == 0.0 -> LinearForm(l.a * r.b, l.b * r.b) // sağ sabit
                else -> throw NotLinearException("İki değişkenli çarpım lineer değil")
            }
        }
        is Expr.Div -> {
            val l = toLinearForm(expr.left)
            val r = toLinearForm(expr.right)
            if (r.a != 0.0) throw NotLinearException("Değişkene bölme lineer değil")
            if (r.b == 0.0) throw NotLinearException("Sıfıra bölme")
            LinearForm(l.a / r.b, l.b / r.b)
        }
        is Expr.Pow -> {
            val exp = expr.exponent
            if (exp is Expr.Num && exp.value == 1.0) {
                toLinearForm(expr.base)
            } else {
                val base = toLinearForm(expr.base)
                if (base.a == 0.0 && exp is Expr.Num) {
                    // sabit^sabit -> sabit
                    LinearForm(0.0, pow(base.b, exp.value))
                } else {
                    throw NotLinearException("Üslü değişken lineer değil")
                }
            }
        }
        is Expr.Log, is Expr.Ln -> throw NotLinearException("Logaritma lineer cebir değil")
        is Expr.Derivative -> throw NotLinearException("Türev ifadesi lineer cebir değil")
    }

    private fun pow(base: Double, exp: Double): Double = base.pow(exp)
}
