package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.model.MathTopic

/**
 * Tek bir dilin tüm metin sözleşmesi. Her dil bir Kotlin `object` olarak uygular.
 * Anahtarlar camelCase'dir ve ViewModel ile çalışma zamanında anlık değiştirilir.
 */
interface LocalizedStrings {
    val language: Language

    // — Genel UI —
    val appName: String
    val solverTitle: String
    val inputPlaceholder: String
    val solveButton: String
    val clearButton: String
    val stepLabel: String
    val finalAnswerLabel: String
    val examplesTitle: String
    val emptyStateMessage: String
    val languageSelectorTitle: String
    val versionPrefix: String

    // — Hata mesajları —
    val errorParse: String
    val errorUnsupported: String
    val errorEmpty: String

    /** Konu görünen adı. */
    fun topicName(topic: MathTopic): String

    /** Kural açıklaması; bilinmeyen anahtar için anahtarın kendisini döndürür. */
    fun rule(key: String): String
}
