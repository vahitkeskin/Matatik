package com.vahitkeskin.matatik.core.domain.engine

import com.vahitkeskin.matatik.core.domain.model.MathTopic
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IntegralSolverTest {

    private val engine = MathSolverEngine()

    @Test
    fun `belirsiz integral polinom cozer`() {
        val solution = engine.solve("int(x^3, x)")
        assertEquals(MathTopic.CALCULUS_INTEGRAL, solution.topic)
        // x^4 / 4 + C render edilmeli
        assertTrue(solution.finalAnswerLatex.contains("x^{4}"))
        assertTrue(solution.finalAnswerLatex.contains("C"))
    }

    @Test
    fun `belirli integral Newton Leibniz ile cozer`() {
        // int(x^2, x, 0, 3) = [x^3/3]_0^3 = 9 - 0 = 9
        val solution = engine.solve("int(x^2, x, 0, 3)")
        assertEquals("9", solution.finalAnswerLatex)
        assertTrue(solution.steps.any { it.ruleApplied == "Newton-Leibniz Kuralı" })
    }

    @Test
    fun `trigonometrik belirsiz integral cozer`() {
        // int(cos(x), x) = sin(x) + C
        val solution = engine.solve("int(cos(x), x)")
        assertTrue(solution.finalAnswerLatex.contains("sin"))
        assertTrue(solution.finalAnswerLatex.contains("C"))
    }
}
