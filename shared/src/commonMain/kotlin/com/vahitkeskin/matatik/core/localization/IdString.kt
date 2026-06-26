package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** Bahasa Indonesia (Endonezce) metinler. */
object IdString : LocalizedStrings {
    override val language = Language.ID

    override val appName = "Matatik"
    override val solverTitle = "Pemecah langkah demi langkah"
    override val inputPlaceholder = "Masukkan persamaan, mis. 2x + 3 = 7"
    override val solveButton = "Selesaikan"
    override val clearButton = "Hapus"
    override val stepLabel = "Langkah"
    override val finalAnswerLabel = "Jawaban"
    override val examplesTitle = "Contoh"
    override val emptyStateMessage = "Masukkan persamaan untuk diselesaikan"
    override val languageSelectorTitle = "Bahasa"
    override val versionPrefix = "Versi"

    override val errorParse = "Tidak dapat menganalisis masukan. Periksa persamaannya."
    override val errorUnsupported = "Masalah ini belum didukung."
    override val errorEmpty = "Silakan masukkan persamaan."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "Aljabar Dasar"
        MathTopic.LOGARITHM -> "Logaritma"
        MathTopic.CALCULUS_DERIVATIVE -> "Turunan"
        MathTopic.CALCULUS_INTEGRAL -> "Integral"
        MathTopic.MATRIX_OPERATIONS -> "Operasi Matriks"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "Tuliskan persamaan yang diberikan."
        RuleKeys.LINEAR_MOVE_TERMS -> "Pindahkan suku variabel ke kiri, konstanta ke kanan."
        RuleKeys.LINEAR_COMBINE -> "Gabungkan suku-suku sejenis."
        RuleKeys.LINEAR_DIVIDE -> "Bagi kedua ruas dengan koefisien."
        RuleKeys.LINEAR_SOLUTION -> "Dapatkan solusi persamaan."
        RuleKeys.LOG_ORIGINAL -> "Tuliskan ekspresi logaritma yang diberikan."
        RuleKeys.LOG_CHANGE_OF_BASE -> "Terapkan rumus perubahan basis."
        RuleKeys.LOG_EVALUATE -> "Hitung ekspresi secara numerik."
        RuleKeys.LOG_EXP_DEFINITION -> "Gunakan definisi eksponensial logaritma."
        RuleKeys.LOG_SOLUTION -> "Hitung nilai variabel."
        RuleKeys.DERIVATIVE_ORIGINAL -> "Tuliskan ekspresi turunan yang diberikan."
        RuleKeys.DERIVATIVE_POWER_RULE -> "Terapkan aturan pangkat: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "Turunan konstanta adalah nol."
        RuleKeys.DERIVATIVE_SUM_RULE -> "Aturan jumlah: turunkan setiap suku."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "Keluarkan koefisien konstanta."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "Sederhanakan ekspresi yang diperoleh."
        RuleKeys.DERIVATIVE_SOLUTION -> "Dapatkan hasil turunan."
        else -> key
    }
}
