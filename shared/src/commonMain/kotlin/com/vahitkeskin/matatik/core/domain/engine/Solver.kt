package com.vahitkeskin.matatik.core.domain.engine

import com.vahitkeskin.matatik.core.domain.ast.Equation
import com.vahitkeskin.matatik.core.domain.model.MathTopic
import com.vahitkeskin.matatik.core.domain.model.MathematicalSolution

/**
 * Bir matematik konusunu çözebilen motor birimi.
 * Her çözücü, çözebildiği denklemler için açık dönüşüm logları (adımlar) üretir.
 */
interface Solver {
    /** Bu çözücünün ilgilendiği konu. */
    val topic: MathTopic

    /** Verilen denklemi bu çözücünün ele alıp alamayacağını söyler (yan etkisiz). */
    fun canSolve(equation: Equation): Boolean

    /** Adım adım çözümü üretir. Çağırmadan önce [canSolve] true olmalıdır. */
    fun solve(equation: Equation): MathematicalSolution
}

/** Motorun girdiyi hiçbir çözücüyle eşleştirememesi durumunda fırlatılır. */
class UnsupportedProblemException(message: String) : Exception(message)
