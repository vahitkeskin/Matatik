package com.vahitkeskin.matatik.core.domain.engine

import com.vahitkeskin.matatik.core.domain.ast.Expr
import com.vahitkeskin.matatik.core.domain.model.MathTopic
import com.vahitkeskin.matatik.core.domain.parser.Parser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LimitSolverTest {

    private val engine = MathSolverEngine()

    @Test
    fun `dogrudan yerine koyma limiti calisir`() {
        val solution = engine.solve("lim(x^2 + 5, x, 2)")
        assertEquals(MathTopic.CALCULUS_DERIVATIVE, solution.topic)
        assertEquals("9", solution.finalAnswerLatex)
        assertTrue(solution.steps.size >= 2)
    }

    @Test
    fun `belirsiz limit L Hopital ile cozulur`() {
        // lim((x^2 - 4)/(x - 2), x, 2) -> L'Hopital ile limit 2x/1 = 4.0 olur
        val solution = engine.solve("lim((x^2 - 4)/(x - 2), x, 2)")
        assertEquals("4", solution.finalAnswerLatex)
        // Belirsizlik tespiti adımı olmalı
        assertTrue(solution.steps.any { it.ruleApplied == "Belirsizlik Tespiti" })
        assertTrue(solution.steps.any { it.ruleApplied == "L'Hôpital Kuralı" })
    }

    @Test
    fun `trigonometrik limit dogrudan yerine koyma ile calisir`() {
        val solution = engine.solve("lim(sin(x), x, 0)")
        assertEquals("0", solution.finalAnswerLatex)
    }
}
