package com.vahitkeskin.matatik.core.localization

/**
 * Dil → metin sözleşmesi kayıt defteri.
 *
 * CLAUDE.md kuralı gereği 19 dil tam olarak kayıtlıdır. Her yeni dil kendi
 * `*String` object'ini ekleyip [registry]'ye kaydederek aktive edilir.
 */
object Localization {

    private val registry: Map<Language, LocalizedStrings> = mapOf(
        Language.TR to TrString,
        Language.EN to EnString,
        Language.JA to JaString,
        Language.DE to DeString,
        Language.RU to RuString,
        Language.FR to FrString,
        Language.ES to EsString,
        Language.HI to HiString,
        Language.AR to ArString,
        Language.AZ to AzString,
        Language.ZH to ZhString,
        Language.PT to PtString,
        Language.ID to IdString,
        Language.KO to KoString,
        Language.IT to ItString,
        Language.NL to NlString,
        Language.VI to ViString,
        Language.TH to ThString,
        Language.PL to PlString
    )

    /** Verilen dilin metinlerini döndürür; yoksa İngilizce'ye düşer. */
    operator fun get(language: Language): LocalizedStrings =
        registry[language] ?: EnString

    /** Çevirisi tamamlanmış (gerçek) diller. */
    val translatedLanguages: List<Language> = registry.keys.sortedBy { it.ordinal }
}

