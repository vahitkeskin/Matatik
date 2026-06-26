package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** Türkçe metinler. */
object TrString : LocalizedStrings {
    override val language = Language.TR

    override val appName = "Matatik"
    override val solverTitle = "Adım Adım Çözücü"
    override val inputPlaceholder = "Denklem girin, örn. 2x + 3 = 7"
    override val solveButton = "Çöz"
    override val clearButton = "Temizle"
    override val stepLabel = "Adım"
    override val finalAnswerLabel = "Sonuç"
    override val examplesTitle = "Örnekler"
    override val emptyStateMessage = "Çözmek için bir denklem girin"
    override val languageSelectorTitle = "Dil Seçimi"
    override val versionPrefix = "Sürüm"

    override val errorParse = "Girdi çözümlenemedi. Lütfen denklemi kontrol edin."
    override val errorUnsupported = "Bu problem henüz desteklenmiyor."
    override val errorEmpty = "Lütfen bir denklem girin."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "Temel Cebir"
        MathTopic.LOGARITHM -> "Logaritma"
        MathTopic.CALCULUS_DERIVATIVE -> "Türev"
        MathTopic.CALCULUS_INTEGRAL -> "İntegral"
        MathTopic.MATRIX_OPERATIONS -> "Matris İşlemleri"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "Verilen denklem yazılır."
        RuleKeys.LINEAR_MOVE_TERMS -> "Değişken terimler sola, sabitler sağa taşınır."
        RuleKeys.LINEAR_COMBINE -> "Benzer terimler toplanarak sadeleştirilir."
        RuleKeys.LINEAR_DIVIDE -> "Değişkeni yalnız bırakmak için iki taraf katsayıya bölünür."
        RuleKeys.LINEAR_SOLUTION -> "Denklemin çözümü bulunur."
        RuleKeys.LOG_ORIGINAL -> "Verilen logaritmik ifade yazılır."
        RuleKeys.LOG_CHANGE_OF_BASE -> "Taban değiştirme kuralı uygulanır."
        RuleKeys.LOG_EVALUATE -> "İfade sayısal olarak hesaplanır."
        RuleKeys.LOG_EXP_DEFINITION -> "Logaritmanın üstel tanımı kullanılır."
        RuleKeys.LOG_SOLUTION -> "Değişkenin değeri hesaplanır."
        RuleKeys.DERIVATIVE_ORIGINAL -> "Verilen türev ifadesi yazılır."
        RuleKeys.DERIVATIVE_POWER_RULE -> "Kuvvet kuralı uygulanır: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "Sabitin türevi sıfırdır."
        RuleKeys.DERIVATIVE_SUM_RULE -> "Toplam/fark kuralı: her terim ayrı türevlenir."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "Sabit çarpan türevin dışına alınır."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "Elde edilen ifade sadeleştirilir."
        RuleKeys.DERIVATIVE_SOLUTION -> "Türevin sonucu hesaplanır."
        else -> key
    }
}
