package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** Azərbaycanca (Azerice) metinler. */
object AzString : LocalizedStrings {
    override val language = Language.AZ

    override val appName = "Matatik"
    override val solverTitle = "Addım-addım həlledicı"
    override val inputPlaceholder = "Tənlik daxil edin, məs. 2x + 3 = 7"
    override val solveButton = "Həll et"
    override val clearButton = "Təmizlə"
    override val stepLabel = "Addım"
    override val finalAnswerLabel = "Cavab"
    override val examplesTitle = "Nümunələr"
    override val emptyStateMessage = "Həll etmək üçün tənlik daxil edin"
    override val languageSelectorTitle = "Dil"
    override val versionPrefix = "Versiya"

    override val errorParse = "Giriş təhlil edilə bilmədi. Tənliyi yoxlayın."
    override val errorUnsupported = "Bu məsələ hələ dəstəklənmir."
    override val errorEmpty = "Zəhmət olmasa, tənlik daxil edin."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "Əsas Cəbr"
        MathTopic.LOGARITHM -> "Loqarifm"
        MathTopic.CALCULUS_DERIVATIVE -> "Törəmə"
        MathTopic.CALCULUS_INTEGRAL -> "İnteqral"
        MathTopic.MATRIX_OPERATIONS -> "Matris əməliyyatları"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "Verilmiş tənlik yazılır."
        RuleKeys.LINEAR_MOVE_TERMS -> "Dəyişən hədlər sola, sabitlər sağa köçürülür."
        RuleKeys.LINEAR_COMBINE -> "Oxşar hədlər birləşdirilir."
        RuleKeys.LINEAR_DIVIDE -> "Hər iki tərəf əmsala bölünür."
        RuleKeys.LINEAR_SOLUTION -> "Tənliyin həlli tapılır."
        RuleKeys.LOG_ORIGINAL -> "Verilmiş loqarifmik ifadə yazılır."
        RuleKeys.LOG_CHANGE_OF_BASE -> "Əsas dəyişdirmə qaydası tətbiq edilir."
        RuleKeys.LOG_EVALUATE -> "İfadə ədədi olaraq hesablanır."
        RuleKeys.LOG_EXP_DEFINITION -> "Loqarifmin eksponensial tərifi istifadə edilir."
        RuleKeys.LOG_SOLUTION -> "Dəyişənin qiyməti hesablanır."
        RuleKeys.DERIVATIVE_ORIGINAL -> "Verilmiş törəmə ifadəsi yazılır."
        RuleKeys.DERIVATIVE_POWER_RULE -> "Qüvvət qaydası tətbiq edilir: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "Sabitin törəməsi sıfırdır."
        RuleKeys.DERIVATIVE_SUM_RULE -> "Toplama qaydası: hər həddin ayrıca törəməsi tapılır."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "Sabit əmsal kənara çıxarılır."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "Alınan ifadə sadələşdirilir."
        RuleKeys.DERIVATIVE_SOLUTION -> "Törəmənin nəticəsi tapılır."
        else -> key
    }
}
