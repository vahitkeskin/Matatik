package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** Русский (Rusça) metinler. */
object RuString : LocalizedStrings {
    override val language = Language.RU

    override val appName = "Matatik"
    override val solverTitle = "Пошаговый решатель"
    override val inputPlaceholder = "Введите уравнение, напр. 2x + 3 = 7"
    override val solveButton = "Решить"
    override val clearButton = "Очистить"
    override val stepLabel = "Шаг"
    override val finalAnswerLabel = "Ответ"
    override val examplesTitle = "Примеры"
    override val emptyStateMessage = "Введите уравнение для решения"
    override val languageSelectorTitle = "Язык"
    override val versionPrefix = "Версия"

    override val errorParse = "Не удалось разобрать ввод. Проверьте уравнение."
    override val errorUnsupported = "Эта задача пока не поддерживается."
    override val errorEmpty = "Пожалуйста, введите уравнение."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "Базовая алгебра"
        MathTopic.LOGARITHM -> "Логарифм"
        MathTopic.CALCULUS_DERIVATIVE -> "Производная"
        MathTopic.CALCULUS_INTEGRAL -> "Интеграл"
        MathTopic.MATRIX_OPERATIONS -> "Матричные операции"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "Записать данное уравнение."
        RuleKeys.LINEAR_MOVE_TERMS -> "Перенести переменные влево, константы вправо."
        RuleKeys.LINEAR_COMBINE -> "Объединить подобные члены."
        RuleKeys.LINEAR_DIVIDE -> "Разделить обе стороны на коэффициент."
        RuleKeys.LINEAR_SOLUTION -> "Получить решение уравнения."
        RuleKeys.LOG_ORIGINAL -> "Записать данное логарифмическое выражение."
        RuleKeys.LOG_CHANGE_OF_BASE -> "Применить формулу перехода к другому основанию."
        RuleKeys.LOG_EVALUATE -> "Вычислить выражение численно."
        RuleKeys.LOG_EXP_DEFINITION -> "Использовать показательное определение логарифма."
        RuleKeys.LOG_SOLUTION -> "Вычислить значение переменной."
        RuleKeys.DERIVATIVE_ORIGINAL -> "Записать данное выражение для дифференцирования."
        RuleKeys.DERIVATIVE_POWER_RULE -> "Применить степенное правило: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "Производная константы равна нулю."
        RuleKeys.DERIVATIVE_SUM_RULE -> "Правило суммы: дифференцировать каждый член."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "Вынести постоянный множитель."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "Упростить полученное выражение."
        RuleKeys.DERIVATIVE_SOLUTION -> "Получить результат дифференцирования."
        else -> key
    }
}
