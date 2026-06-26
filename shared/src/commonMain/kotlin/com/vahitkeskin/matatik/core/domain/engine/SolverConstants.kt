package com.vahitkeskin.matatik.core.domain.engine

/** Adım vurgularında kullanılan renk paleti (glassmorphic temaya uyumlu). */
object HighlightColors {
    const val VARIABLE = "#7C4DFF"   // mor — değişken terimler
    const val CONSTANT = "#00BFA5"   // turkuaz — sabitler
    const val RESULT = "#FF6D00"     // turuncu — nihai sonuç
    const val OPERATION = "#2979FF"  // mavi — uygulanan işlem
}

/** Geçerli animasyon türleri (kontrat ile sınırlı). */
object AnimationTypes {
    const val SCALE = "SCALE"
    const val FADE = "FADE"
    const val TRANSLATE = "TRANSLATE"
    const val GLOW = "GLOW"
}

/** Çözüm adımı açıklamaları için lokalizasyon anahtarları. */
object RuleKeys {
    const val LINEAR_ORIGINAL = "rules.linear.original"
    const val LINEAR_MOVE_TERMS = "rules.linear.move_terms"
    const val LINEAR_COMBINE = "rules.linear.combine_like_terms"
    const val LINEAR_DIVIDE = "rules.linear.isolate_variable"
    const val LINEAR_SOLUTION = "rules.linear.solution"

    const val LOG_ORIGINAL = "rules.log.original"
    const val LOG_CHANGE_OF_BASE = "rules.log.change_of_base"
    const val LOG_EVALUATE = "rules.log.evaluate"
    const val LOG_EXP_DEFINITION = "rules.log.exponential_definition"
    const val LOG_SOLUTION = "rules.log.solution"

    const val DERIVATIVE_ORIGINAL = "rules.derivative.original"
    const val DERIVATIVE_POWER_RULE = "rules.derivative.power_rule"
    const val DERIVATIVE_CONSTANT_RULE = "rules.derivative.constant_rule"
    const val DERIVATIVE_SUM_RULE = "rules.derivative.sum_rule"
    const val DERIVATIVE_COEFFICIENT_RULE = "rules.derivative.coefficient_rule"
    const val DERIVATIVE_SIMPLIFY = "rules.derivative.simplify"
    const val DERIVATIVE_SOLUTION = "rules.derivative.solution"
}
