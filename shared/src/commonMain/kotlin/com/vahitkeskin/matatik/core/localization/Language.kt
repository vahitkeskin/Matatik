package com.vahitkeskin.matatik.core.localization

/**
 * Uygulamanın desteklediği 20 dil. `strings.xml` yerine tamamen Kotlin tabanlıdır.
 *
 * @param code ISO 639-1 dil kodu.
 * @param nativeName Dilin kendi adı (seçim menüsünde gösterilir).
 * @param flag Bayrak emoji'si.
 * @param isRtl Sağdan sola yazım (örn. Arapça).
 */
enum class Language(
    val code: String,
    val nativeName: String,
    val flag: String,
    val isRtl: Boolean = false
) {
    TR("tr", "Türkçe", "🇹🇷"),
    EN("en", "English", "🇬🇧"),
    JA("ja", "日本語", "🇯🇵"),
    DE("de", "Deutsch", "🇩🇪"),
    RU("ru", "Русский", "🇷🇺"),
    FR("fr", "Français", "🇫🇷"),
    ES("es", "Español", "🇪🇸"),
    HI("hi", "हिन्दी", "🇮🇳"),
    AR("ar", "العربية", "🇸🇦", isRtl = true),
    AZ("az", "Azərbaycanca", "🇦🇿"),
    ZH("zh", "简体中文", "🇨🇳"),
    PT("pt", "Português", "🇧🇷"),
    ID("id", "Bahasa Indonesia", "🇮🇩"),
    KO("ko", "한국어", "🇰🇷"),
    IT("it", "Italiano", "🇮🇹"),
    NL("nl", "Nederlands", "🇳🇱"),
    VI("vi", "Tiếng Việt", "🇻🇳"),
    TH("th", "ไทย", "🇹🇭"),
    PL("pl", "Polski", "🇵🇱");

    companion object {
        val DEFAULT = TR
        fun fromCode(code: String): Language =
            entries.firstOrNull { it.code == code } ?: DEFAULT
    }
}
