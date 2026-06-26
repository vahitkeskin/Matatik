package com.vahitkeskin.matatik.core.domain.engine

import com.vahitkeskin.matatik.core.domain.model.MathTopic
import com.vahitkeskin.matatik.core.domain.parser.Parser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LinearEquationSolverTest {

    private val solver = LinearEquationSolver()

    private fun solve(input: String) = solver.solve(Parser(input).parseEquation())

    @Test
    fun `basit denklem cozulur`() {
        val solution = solve("2x + 3 = 7")
        assertEquals(MathTopic.BASIC_ALGEBRA, solution.topic)
        assertEquals("x = 2", solution.finalAnswerLatex)
        assertTrue(solution.steps.size >= 4)
        assertEquals(1, solution.steps.first().stepNumber)
    }

    @Test
    fun `degiskenler iki tarafta toplanir`() {
        val solution = solve("5x - 4 = 2x + 11")
        assertEquals("x = 5", solution.finalAnswerLatex)
    }

    @Test
    fun `negatif ve kesirli sonuc`() {
        val solution = solve("4x = 2")
        // katsayı 1 olmadığından bölme adımı içerir
        assertEquals("x = 0.5", solution.finalAnswerLatex)
    }

    @Test
    fun `dogrusal olmayan denklem reddedilir`() {
        assertFalse(solver.canSolve(Parser("x^2 = 4").parseEquation()))
    }

    @Test
    fun `degiskensiz ifade reddedilir`() {
        assertFalse(solver.canSolve(Parser("3 + 4 = 7").parseEquation()))
    }

    @Test
    fun `adim numaralari ardisiktir`() {
        val steps = solve("2x + 3 = 7").steps
        steps.forEachIndexed { index, step ->
            assertEquals(index + 1, step.stepNumber)
        }
    }
}
