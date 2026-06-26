package com.vahitkeskin.matatik.core.domain.parser

import com.vahitkeskin.matatik.core.domain.ast.Expr
import com.vahitkeskin.matatik.core.domain.ast.LatexRenderer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ParserTest {

    @Test
    fun `ortuk carpim katsayiyi cozer`() {
        val eq = Parser("2x + 3 = 7").parseEquation()
        val lhs = eq.lhs
        assertTrue(lhs is Expr.Add)
        val mul = (lhs as Expr.Add).left
        assertTrue(mul is Expr.Mul)
        assertEquals(2.0, ((mul as Expr.Mul).left as Expr.Num).value)
        assertEquals("x", (mul.right as Expr.Variable).name)
    }

    @Test
    fun `denklem olmadan rhs sifir olur`() {
        val eq = Parser("x + 1").parseEquation()
        assertEquals(0.0, (eq.rhs as Expr.Num).value)
    }

    @Test
    fun `logaritma tabanli ayristirilir`() {
        val eq = Parser("log_2(8)").parseEquation()
        val log = eq.lhs
        assertTrue(log is Expr.Log)
        assertEquals(2.0, ((log as Expr.Log).base as Expr.Num).value)
        assertEquals(8.0, (log.arg as Expr.Num).value)
    }

    @Test
    fun `us oncelik dogru render edilir`() {
        val expr = Parser("2 + 3 * x ^ 2").parseExpression()
        // 3 * x^2 çarpımı toplamadan önce, üs çarpımdan önce gelir
        assertEquals("2 + 3x^{2}", LatexRenderer.render(expr))
    }

    @Test
    fun `gecersiz karakter hata firlatir`() {
        assertFailsWith<MathParseException> { Parser("2 @ 3").parseEquation() }
    }

    @Test
    fun `limit integral trig ifadeleri dogru ayristirilir`() {
        val limExpr = Parser("lim(x^2, x, 2)").parseExpression()
        assertTrue(limExpr is Expr.Limit)
        assertEquals("x", limExpr.variable)
        assertEquals(2.0, (limExpr.target as Expr.Num).value)

        val intExpr = Parser("int(x, x, 0, 1)").parseExpression()
        assertTrue(intExpr is Expr.Integral)
        assertEquals("x", intExpr.variable)
        assertEquals(0.0, (intExpr.lowerBound as Expr.Num).value)
        assertEquals(1.0, (intExpr.upperBound as Expr.Num).value)

        val trigExpr = Parser("sin(pi)").parseExpression()
        assertTrue(trigExpr is Expr.Trig)
        assertEquals("sin", trigExpr.func)
        assertEquals("pi", (trigExpr.arg as Expr.Variable).name)
    }
}
