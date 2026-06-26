package com.vahitkeskin.matatik.core.localization

/**
 * Dil → metin sözleşmesi kayıt defteri.
 *
 * Şu an TR ve EN tam olarak çevrilidir. Henüz çevrilmemiş diller İngilizce'ye
 * (uluslararası taban) düşer; her yeni dil yalnızca kendi `*String` object'ini
 * ekleyip [registry]'ye kaydederek aktive edilir.
 */
object Localization {

    private val registry: Map<Language, LocalizedStrings> = mapOf(
        Language.TR to TrString,
        Language.EN to EnString
    )

    /** Verilen dilin metinlerini döndürür; yoksa İngilizce'ye düşer. */
    operator fun get(language: Language): LocalizedStrings =
        registry[language] ?: EnString

    /** Çevirisi tamamlanmış (gerçek) diller. */
    val translatedLanguages: List<Language> = registry.keys.sortedBy { it.ordinal }
}
