package com.vahitkeskin.matatik.core.domain.ast

import kotlin.math.abs
import kotlin.math.roundToLong

/**
 * Soyut Sözdizimi Ağacı (Abstract Syntax Tree).
 *
 * Matematik asla ham string veya regex olarak işlenmez; her ifade tipli bir ağaca
 * ayrıştırılır. Çözüm adımları bu ağaç üzerinde yapılan atomik dönüşümlerdir.
 */
sealed interface Expr {

    /** Sabit sayı düğümü. */
    data class Num(val value: Double) : Expr

    /** Değişken düğümü (örn. x, y). */
    data class Variable(val name: String) : Expr

    /** Toplama: left + right. */
    data class Add(val left: Expr, val right: Expr) : Expr

    /** Çıkarma: left - right. */
    data class Sub(val left: Expr, val right: Expr) : Expr

    /** Çarpma: left * right. */
    data class Mul(val left: Expr, val right: Expr) : Expr

    /** Bölme: left / right. */
    data class Div(val left: Expr, val right: Expr) : Expr

    /** Üs alma: base ^ exponent. */
    data class Pow(val base: Expr, val exponent: Expr) : Expr

    /** Tekli negatif: -arg. */
    data class Neg(val arg: Expr) : Expr

    /** Belirli tabanlı logaritma: log_base(arg). */
    data class Log(val base: Expr, val arg: Expr) : Expr

    /** Doğal logaritma: ln(arg). */
    data class Ln(val arg: Expr) : Expr
}

/** lhs = rhs biçimindeki bir denklemi temsil eder. */
data class Equation(val lhs: Expr, val rhs: Expr)

/** Sık kullanılan sabit/yardımcı kurucular. */
object Exprs {
    val ZERO = Expr.Num(0.0)
    val ONE = Expr.Num(1.0)
    fun num(v: Double) = Expr.Num(v)
    fun num(v: Int) = Expr.Num(v.toDouble())
    fun x(name: String = "x") = Expr.Variable(name)
}

/**
 * Bir double değeri, gereksiz ondalık olmadan insan-okunur biçime çevirir.
 * 4.0 -> "4", 1.5 -> "1.5", -0.0 -> "0".
 */
fun formatNumber(value: Double): String {
    if (value.isNaN()) return "NaN"
    if (value.isInfinite()) return if (value > 0) "\\infty" else "-\\infty"
    val rounded = value.roundToLong()
    return if (abs(value - rounded) < 1e-9) {
        rounded.toString()
    } else {
        // Sondaki gereksiz sıfırları temizle
        value.toString().trimEnd('0').trimEnd('.')
    }
}
