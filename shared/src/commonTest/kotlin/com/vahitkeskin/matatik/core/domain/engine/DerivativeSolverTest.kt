package com.vahitkeskin.matatik.core.domain.engine

import com.vahitkeskin.matatik.core.domain.ast.Expr
import com.vahitkeskin.matatik.core.domain.ast.Exprs
import com.vahitkeskin.matatik.core.domain.model.MathTopic
import com.vahitkeskin.matatik.core.domain.parser.Parser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DerivativeSolverTest {

    private val solver = DerivativeSolver()
    private val engine = MathSolverEngine()

    private fun solve(input: String) = engine.solve(input)

    @Test
    fun `basit polinom turevi doğru hesaplanir`() {
        // d/dx(x^3) = 3x^2
        val solution = solve("d/dx(x^3)")
        assertEquals(MathTopic.CALCULUS_DERIVATIVE, solution.topic)
        assertTrue(solution.steps.isNotEmpty())
    }

    @Test
    fun `sabit turevi sifirdir`() {
        val result = solver.differentiate(Expr.Num(5.0), "x")
        assertEquals(Exprs.ZERO, result)
    }

    @Test
    fun `degisken turevi birdir`() {
        val result = solver.differentiate(Expr.Variable("x"), "x")
        assertEquals(Exprs.ONE, result)
    }

    @Test
    fun `farkli degisken turevi sifirdir`() {
        val result = solver.differentiate(Expr.Variable("y"), "x")
        assertEquals(Exprs.ZERO, result)
    }

    @Test
    fun `toplam turevi doğru hesaplanir`() {
        // d/dx(x + 5) = 1 + 0 = 1
        val expr = Expr.Add(Expr.Variable("x"), Expr.Num(5.0))
        val result = solver.simplify(solver.differentiate(expr, "x"))
        assertEquals(Exprs.ONE, result)
    }

    @Test
    fun `katsayili terim turevi doğru hesaplanir`() {
        // d/dx(3x) = 3
        val expr = Expr.Mul(Expr.Num(3.0), Expr.Variable("x"))
        val result = solver.simplify(solver.differentiate(expr, "x"))
        assertEquals(Expr.Num(3.0), result)
    }

    @Test
    fun `kuvvet kurali doğru uygulanir`() {
        // d/dx(x^2) → sonuç sadeleştirildikten sonra 2x olmalı
        val expr = Expr.Pow(Expr.Variable("x"), Expr.Num(2.0))
        val result = solver.simplify(solver.differentiate(expr, "x"))
        // 2 * x^1 → sadeleşerek 2 * x olur
        assertTrue(result is Expr.Mul)
        val mul = result as Expr.Mul
        assertEquals(2.0, (mul.left as Expr.Num).value)
        assertTrue(mul.right is Expr.Variable)
    }

    @Test
    fun `motor turev cozucuyu secer`() {
        assertEquals(MathTopic.CALCULUS_DERIVATIVE, solve("d/dx(x^3 + 2x)").topic)
    }

    @Test
    fun `motor turev canSolve true doner`() {
        assertTrue(engine.canSolve("d/dx(x^2)"))
    }

    @Test
    fun `simplify sifir eliminasyonu`() {
        // 0 + expr → expr
        val result = solver.simplify(Expr.Add(Exprs.ZERO, Expr.Variable("x")))
        assertEquals(Expr.Variable("x"), result)
    }

    @Test
    fun `simplify bir eliminasyonu`() {
        // 1 * expr → expr
        val result = solver.simplify(Expr.Mul(Exprs.ONE, Expr.Variable("x")))
        assertEquals(Expr.Variable("x"), result)
    }

    @Test
    fun `deriv notasyonu parser tarafından ayristirilir`() {
        val eq = Parser("d/dx(x^2 + 1)").parseEquation()
        assertTrue(eq.lhs is Expr.Derivative)
        val deriv = eq.lhs as Expr.Derivative
        assertEquals("x", deriv.variable)
    }

    @Test
    fun `logaritmik ifade polinom degil`() {
        assertFalse(engine.canSolve("d/dx(log_2(x))"))
    }
}
