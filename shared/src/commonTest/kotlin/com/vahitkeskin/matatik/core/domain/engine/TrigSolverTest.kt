package com.vahitkeskin.matatik.core.domain.engine

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TrigSolverTest {

    private val engine = MathSolverEngine()

    @Test
    fun `trigonometrik sin pi bolu 2 degeri hesaplanir`() {
        // sin(pi/2) = 1.0 (veya 1)
        val solution = engine.solve("sin(pi/2)")
        assertEquals("1", solution.finalAnswerLatex)
    }

    @Test
    fun `trigonometrik denklem cozumu genel cozumu verir`() {
        // sin(x) = 0.5 -> x = pi/6 + 2k*pi
        val solution = engine.solve("sin(x) = 0.5")
        assertTrue(solution.finalAnswerLatex.contains("pi/6") || solution.finalAnswerLatex.contains("\\pi"))
        assertTrue(solution.finalAnswerLatex.contains("2k"))
    }
}
