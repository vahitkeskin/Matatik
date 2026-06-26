package com.vahitkeskin.matatik.core.localization

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Tüm 19 dilin Localization registry'de eksiksiz kayıtlı olduğunu ve
 * temel arayüz sözleşmelerinin (appName, solveButton) doğru uygulandığını doğrular.
 */
class LocalizationTest {

    @Test
    fun `tum diller registryde kayitli`() {
        Language.entries.forEach { lang ->
            val strings = Localization[lang]
            assertNotNull(strings, "$lang dili registry'de bulunamadı")
            assertEquals(lang, strings.language, "$lang için language alanı yanlış")
        }
    }

    @Test
    fun `registryde 19 dil var`() {
        assertEquals(19, Localization.translatedLanguages.size)
    }

    @Test
    fun `her dilin appName degeri dolu`() {
        Language.entries.forEach { lang ->
            val strings = Localization[lang]
            assert(strings.appName.isNotBlank()) { "$lang appName boş" }
        }
    }

    @Test
    fun `her dilin solveButton degeri dolu`() {
        Language.entries.forEach { lang ->
            val strings = Localization[lang]
            assert(strings.solveButton.isNotBlank()) { "$lang solveButton boş" }
        }
    }

    @Test
    fun `bilinmeyen kural anahtari kendisini doner`() {
        Language.entries.forEach { lang ->
            val strings = Localization[lang]
            val unknownKey = "rules.unknown.test_key"
            assertEquals(unknownKey, strings.rule(unknownKey),
                "$lang bilinmeyen kural için fallback yanlış")
        }
    }

    @Test
    fun `turkce metin doğru`() {
        val tr = Localization[Language.TR]
        assertEquals("Çöz", tr.solveButton)
        assertEquals("Temizle", tr.clearButton)
    }

    @Test
    fun `ingilizce metin doğru`() {
        val en = Localization[Language.EN]
        assertEquals("Solve", en.solveButton)
        assertEquals("Clear", en.clearButton)
    }
}
