package com.vahitkeskin.matatik.core.domain.engine

import com.vahitkeskin.matatik.core.domain.model.MathematicalSolution
import com.vahitkeskin.matatik.core.domain.parser.MathParseException
import com.vahitkeskin.matatik.core.domain.parser.Parser

/**
 * CAS çekirdeğinin tek giriş noktası (facade).
 *
 * Ham metni ayrıştırır, kayıtlı çözücüler arasından uygun olanı seçer ve
 * adım adım [MathematicalSolution] üretir. Standalone (sunucusuz) çalışır.
 */
class MathSolverEngine(
    private val solvers: List<Solver> = defaultSolvers()
) {

    /**
     * @throws MathParseException girdi ayrıştırılamazsa
     * @throws UnsupportedProblemException hiçbir çözücü eşleşmezse
     */
    fun solve(rawInput: String): MathematicalSolution {
        val trimmed = rawInput.trim()
        if (trimmed.isEmpty()) throw MathParseException("Boş girdi")
        val equation = Parser(trimmed).parseEquation()
        val solver = solvers.firstOrNull { it.canSolve(equation) }
            ?: throw UnsupportedProblemException(
                "Bu problem henüz desteklenmiyor: $trimmed"
            )
        return solver.solve(equation)
    }

    /** Girdinin çözülebilir olup olmadığını (istisna fırlatmadan) kontrol eder. */
    fun canSolve(rawInput: String): Boolean = try {
        val equation = Parser(rawInput.trim()).parseEquation()
        solvers.any { it.canSolve(equation) }
    } catch (_: Exception) {
        false
    }

    companion object {
        fun defaultSolvers(): List<Solver> = listOf(
            LinearEquationSolver(),
            LogarithmSolver()
        )
    }
}
