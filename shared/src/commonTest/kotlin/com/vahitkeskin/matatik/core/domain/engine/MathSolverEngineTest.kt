package com.vahitkeskin.matatik.core.domain.engine

import com.vahitkeskin.matatik.core.domain.model.MathTopic
import com.vahitkeskin.matatik.core.domain.parser.MathParseException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MathSolverEngineTest {

    private val engine = MathSolverEngine()

    @Test
    fun `dogru cozucuyu secer`() {
        assertEquals(MathTopic.BASIC_ALGEBRA, engine.solve("2x = 10").topic)
        assertEquals(MathTopic.LOGARITHM, engine.solve("log_2(8)").topic)
    }

    @Test
    fun `desteklenmeyen problem istisna firlatir`() {
        assertFailsWith<UnsupportedProblemException> { engine.solve("x^2 + 1 = 0") }
    }

    @Test
    fun `bos girdi parse hatasi`() {
        assertFailsWith<MathParseException> { engine.solve("   ") }
    }

    @Test
    fun `canSolve guvenli kontrol`() {
        assertTrue(engine.canSolve("3x - 6 = 0"))
        assertFalse(engine.canSolve("2 @ 3"))
    }
}
