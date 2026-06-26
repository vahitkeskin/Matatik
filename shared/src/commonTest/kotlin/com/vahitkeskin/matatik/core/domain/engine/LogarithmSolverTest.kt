package com.vahitkeskin.matatik.core.domain.engine

import com.vahitkeskin.matatik.core.domain.model.MathTopic
import com.vahitkeskin.matatik.core.domain.parser.Parser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LogarithmSolverTest {

    private val solver = LogarithmSolver()

    private fun solve(input: String) = solver.solve(Parser(input).parseEquation())

    @Test
    fun `logaritma sayisal hesaplama`() {
        val solution = solve("log_2(8)")
        assertEquals(MathTopic.LOGARITHM, solution.topic)
        assertEquals("3", solution.finalAnswerLatex)
        assertTrue(solution.steps.any { it.descriptionLocalizationKey == RuleKeys.LOG_CHANGE_OF_BASE })
    }

    @Test
    fun `logaritmik denklem ustel tanimla cozulur`() {
        val solution = solve("log_3(x) = 4")
        assertEquals("x = 81", solution.finalAnswerLatex)
        assertTrue(solution.steps.any { it.descriptionLocalizationKey == RuleKeys.LOG_EXP_DEFINITION })
    }

    @Test
    fun `dogal logaritma hesaplanir`() {
        // ln(1) = 0
        val solution = solve("ln(1)")
        assertEquals("0", solution.finalAnswerLatex)
    }
}
