package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** 한국어 (Korece) metinler. */
object KoString : LocalizedStrings {
    override val language = Language.KO

    override val appName = "Matatik"
    override val solverTitle = "단계별 풀이기"
    override val inputPlaceholder = "방정식을 입력하세요, 예: 2x + 3 = 7"
    override val solveButton = "풀기"
    override val clearButton = "지우기"
    override val stepLabel = "단계"
    override val finalAnswerLabel = "정답"
    override val examplesTitle = "예제"
    override val emptyStateMessage = "풀 방정식을 입력하세요"
    override val languageSelectorTitle = "언어"
    override val versionPrefix = "버전"

    override val errorParse = "입력을 분석할 수 없습니다. 방정식을 확인하세요."
    override val errorUnsupported = "이 문제는 아직 지원되지 않습니다."
    override val errorEmpty = "방정식을 입력하세요."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "기초 대수"
        MathTopic.LOGARITHM -> "로그"
        MathTopic.CALCULUS_DERIVATIVE -> "미분"
        MathTopic.CALCULUS_INTEGRAL -> "적분"
        MathTopic.MATRIX_OPERATIONS -> "행렬 연산"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "주어진 방정식을 적습니다."
        RuleKeys.LINEAR_MOVE_TERMS -> "변수항을 왼쪽으로, 상수를 오른쪽으로 이동합니다."
        RuleKeys.LINEAR_COMBINE -> "동류항을 합쳐 정리합니다."
        RuleKeys.LINEAR_DIVIDE -> "양변을 계수로 나누어 변수를 구합니다."
        RuleKeys.LINEAR_SOLUTION -> "방정식의 해를 구합니다."
        RuleKeys.LOG_ORIGINAL -> "주어진 로그 표현식을 적습니다."
        RuleKeys.LOG_CHANGE_OF_BASE -> "밑 변환 공식을 적용합니다."
        RuleKeys.LOG_EVALUATE -> "수치적으로 계산합니다."
        RuleKeys.LOG_EXP_DEFINITION -> "로그의 지수 정의를 사용합니다."
        RuleKeys.LOG_SOLUTION -> "변수의 값을 계산합니다."
        RuleKeys.DERIVATIVE_ORIGINAL -> "주어진 미분 표현식을 적습니다."
        RuleKeys.DERIVATIVE_POWER_RULE -> "거듭제곱 법칙 적용: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "상수의 미분은 0입니다."
        RuleKeys.DERIVATIVE_SUM_RULE -> "합·차 법칙: 각 항을 개별적으로 미분합니다."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "상수 계수를 밖으로 꺼냅니다."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "얻어진 식을 정리합니다."
        RuleKeys.DERIVATIVE_SOLUTION -> "미분 결과를 구합니다."
        else -> key
    }
}
