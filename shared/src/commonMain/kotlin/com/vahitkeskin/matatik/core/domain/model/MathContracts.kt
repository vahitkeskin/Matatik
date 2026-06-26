package com.vahitkeskin.matatik.core.domain.model

import kotlinx.serialization.Serializable

/**
 * CAS (Computer Algebra System) çekirdeğinin değişmez (immutable) veri kontratları.
 *
 * MATH_PROMPT.md'de tanımlanan "strict data contracts" bu dosyada toplanmıştır.
 * Tüm tipler [kotlinx.serialization] ile serileştirilebilir; böylece çözüm logları
 * platformlar arası (Android, iOS, Desktop) ve istemci-sunucu sınırında kayıpsız taşınır.
 */

/** Motorun desteklediği matematik konuları. */
@Serializable
enum class MathTopic {
    BASIC_ALGEBRA,
    LOGARITHM,
    CALCULUS_DERIVATIVE,
    CALCULUS_INTEGRAL,
    MATRIX_OPERATIONS
}

/** Bir problemin temel zorluk seviyesi. */
@Serializable
enum class DifficultyLevel {
    EASY,
    MEDIUM,
    HARD
}

/**
 * Bir adımda görsel olarak vurgulanacak ifade parçasının koordinat/stil tanımı.
 *
 * @param targetTerm Vurgulanacak terimin LaTeX gösterimi (örn. "2x").
 * @param hexColor Vurgu rengi, "#RRGGBB" veya "#AARRGGBB" formatında.
 * @param animationType Animasyon türü. Yalnızca: "SCALE", "FADE", "TRANSLATE", "GLOW".
 */
@Serializable
data class HighlightedCoordinate(
    val targetTerm: String,
    val hexColor: String,
    val animationType: String
)

/**
 * Atomik bir ağaç-yeniden-yazma adımı (S_n => S_{n+1}).
 *
 * Her adım, uygulanan kuralı, açıklama lokalizasyon anahtarını, kullanılan formülü
 * ve adım sonundaki ifadenin LaTeX gösterimini taşır.
 */
@Serializable
data class SolutionStep(
    val stepNumber: Int,
    val ruleApplied: String,
    val descriptionLocalizationKey: String,
    val formulaUsedLatex: String,
    val currentExpressionLatex: String,
    val highlightedParts: List<HighlightedCoordinate>
)

/**
 * Tek bir problemin uçtan uca çözümü: ham denklem, adım listesi ve nihai cevap.
 */
@Serializable
data class MathematicalSolution(
    val id: String,
    val topic: MathTopic,
    val baseDifficulty: DifficultyLevel,
    val rawEquationLatex: String,
    val steps: List<SolutionStep>,
    val finalAnswerLatex: String
)
