package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** 简体中文 (Çince - Basitleştirilmiş) metinler. */
object ZhString : LocalizedStrings {
    override val language = Language.ZH

    override val appName = "Matatik"
    override val solverTitle = "分步求解器"
    override val inputPlaceholder = "输入方程，例如 2x + 3 = 7"
    override val solveButton = "求解"
    override val clearButton = "清除"
    override val stepLabel = "步骤"
    override val finalAnswerLabel = "答案"
    override val examplesTitle = "示例"
    override val emptyStateMessage = "输入一个方程来求解"
    override val languageSelectorTitle = "语言"
    override val versionPrefix = "版本"

    override val errorParse = "无法解析输入。请检查方程。"
    override val errorUnsupported = "此问题尚不支持。"
    override val errorEmpty = "请输入一个方程。"

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "基础代数"
        MathTopic.LOGARITHM -> "对数"
        MathTopic.CALCULUS_DERIVATIVE -> "导数"
        MathTopic.CALCULUS_INTEGRAL -> "积分"
        MathTopic.MATRIX_OPERATIONS -> "矩阵运算"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "写出给定的方程。"
        RuleKeys.LINEAR_MOVE_TERMS -> "将变量项移到左边，常数移到右边。"
        RuleKeys.LINEAR_COMBINE -> "合并同类项进行化简。"
        RuleKeys.LINEAR_DIVIDE -> "两边除以系数以求解变量。"
        RuleKeys.LINEAR_SOLUTION -> "得到方程的解。"
        RuleKeys.LOG_ORIGINAL -> "写出给定的对数表达式。"
        RuleKeys.LOG_CHANGE_OF_BASE -> "应用换底公式。"
        RuleKeys.LOG_EVALUATE -> "进行数值计算。"
        RuleKeys.LOG_EXP_DEFINITION -> "使用对数的指数定义。"
        RuleKeys.LOG_SOLUTION -> "计算变量的值。"
        RuleKeys.DERIVATIVE_ORIGINAL -> "写出给定的导数表达式。"
        RuleKeys.DERIVATIVE_POWER_RULE -> "应用幂法则：d/dx[xⁿ] = n·xⁿ⁻¹。"
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "常数的导数为零。"
        RuleKeys.DERIVATIVE_SUM_RULE -> "和差法则：分别对每一项求导。"
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "提取常数系数。"
        RuleKeys.DERIVATIVE_SIMPLIFY -> "化简所得表达式。"
        RuleKeys.DERIVATIVE_SOLUTION -> "得到导数的结果。"
        else -> key
    }
}
