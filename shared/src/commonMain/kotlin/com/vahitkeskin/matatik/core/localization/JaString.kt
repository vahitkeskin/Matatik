package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** 日本語 (Japonca) metinler. */
object JaString : LocalizedStrings {
    override val language = Language.JA

    override val appName = "Matatik"
    override val solverTitle = "ステップバイステップソルバー"
    override val inputPlaceholder = "方程式を入力してください（例: 2x + 3 = 7）"
    override val solveButton = "解く"
    override val clearButton = "クリア"
    override val stepLabel = "ステップ"
    override val finalAnswerLabel = "答え"
    override val examplesTitle = "例"
    override val emptyStateMessage = "解くための方程式を入力してください"
    override val languageSelectorTitle = "言語"
    override val versionPrefix = "バージョン"

    override val errorParse = "入力を解析できませんでした。方程式を確認してください。"
    override val errorUnsupported = "この問題はまだサポートされていません。"
    override val errorEmpty = "方程式を入力してください。"

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "基本代数"
        MathTopic.LOGARITHM -> "対数"
        MathTopic.CALCULUS_DERIVATIVE -> "微分"
        MathTopic.CALCULUS_INTEGRAL -> "積分"
        MathTopic.MATRIX_OPERATIONS -> "行列演算"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "与えられた方程式を書き出します。"
        RuleKeys.LINEAR_MOVE_TERMS -> "変数項を左辺に、定数を右辺に移動します。"
        RuleKeys.LINEAR_COMBINE -> "同類項をまとめて簡略化します。"
        RuleKeys.LINEAR_DIVIDE -> "両辺を係数で割って変数を求めます。"
        RuleKeys.LINEAR_SOLUTION -> "方程式の解を求めます。"
        RuleKeys.LOG_ORIGINAL -> "与えられた対数式を書き出します。"
        RuleKeys.LOG_CHANGE_OF_BASE -> "底の変換公式を適用します。"
        RuleKeys.LOG_EVALUATE -> "数値的に計算します。"
        RuleKeys.LOG_EXP_DEFINITION -> "対数の指数定義を使用します。"
        RuleKeys.LOG_SOLUTION -> "変数の値を計算します。"
        RuleKeys.DERIVATIVE_ORIGINAL -> "与えられた微分式を書き出します。"
        RuleKeys.DERIVATIVE_POWER_RULE -> "べき乗の法則を適用します: d/dx[xⁿ] = n·xⁿ⁻¹。"
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "定数の微分はゼロです。"
        RuleKeys.DERIVATIVE_SUM_RULE -> "和・差の法則: 各項を個別に微分します。"
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "定数係数を外に出します。"
        RuleKeys.DERIVATIVE_SIMPLIFY -> "得られた式を簡略化します。"
        RuleKeys.DERIVATIVE_SOLUTION -> "微分の結果を求めます。"
        else -> key
    }
}
