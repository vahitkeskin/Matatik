package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** हिन्दी (Hintçe) metinler. */
object HiString : LocalizedStrings {
    override val language = Language.HI

    override val appName = "Matatik"
    override val solverTitle = "चरण-दर-चरण हल"
    override val inputPlaceholder = "एक समीकरण दर्ज करें, जैसे 2x + 3 = 7"
    override val solveButton = "हल करें"
    override val clearButton = "साफ़ करें"
    override val stepLabel = "चरण"
    override val finalAnswerLabel = "उत्तर"
    override val examplesTitle = "उदाहरण"
    override val emptyStateMessage = "हल करने के लिए एक समीकरण दर्ज करें"
    override val languageSelectorTitle = "भाषा"
    override val versionPrefix = "संस्करण"

    override val errorParse = "इनपुट का विश्लेषण नहीं हो सका। कृपया समीकरण जांचें।"
    override val errorUnsupported = "यह समस्या अभी समर्थित नहीं है।"
    override val errorEmpty = "कृपया एक समीकरण दर्ज करें।"

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "मूल बीजगणित"
        MathTopic.LOGARITHM -> "लघुगणक"
        MathTopic.CALCULUS_DERIVATIVE -> "अवकलज"
        MathTopic.CALCULUS_INTEGRAL -> "समाकलन"
        MathTopic.MATRIX_OPERATIONS -> "मैट्रिक्स संक्रियाएँ"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "दी गई समीकरण लिखें।"
        RuleKeys.LINEAR_MOVE_TERMS -> "चर पदों को बाईं ओर, अचरों को दाईं ओर ले जाएं।"
        RuleKeys.LINEAR_COMBINE -> "समान पदों को मिलाकर सरल करें।"
        RuleKeys.LINEAR_DIVIDE -> "चर को अलग करने के लिए दोनों पक्षों को गुणांक से भाग दें।"
        RuleKeys.LINEAR_SOLUTION -> "समीकरण का हल प्राप्त करें।"
        RuleKeys.LOG_ORIGINAL -> "दी गई लघुगणकीय अभिव्यक्ति लिखें।"
        RuleKeys.LOG_CHANGE_OF_BASE -> "आधार परिवर्तन नियम लागू करें।"
        RuleKeys.LOG_EVALUATE -> "अभिव्यक्ति का संख्यात्मक मूल्यांकन करें।"
        RuleKeys.LOG_EXP_DEFINITION -> "लघुगणक की घातांकीय परिभाषा का उपयोग करें।"
        RuleKeys.LOG_SOLUTION -> "चर का मान गणना करें।"
        RuleKeys.DERIVATIVE_ORIGINAL -> "दी गई अवकलज अभिव्यक्ति लिखें।"
        RuleKeys.DERIVATIVE_POWER_RULE -> "घात नियम लागू करें: d/dx[xⁿ] = n·xⁿ⁻¹।"
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "अचर का अवकलज शून्य होता है।"
        RuleKeys.DERIVATIVE_SUM_RULE -> "योग नियम: प्रत्येक पद का अलग-अलग अवकलन करें।"
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "अचर गुणांक को बाहर निकालें।"
        RuleKeys.DERIVATIVE_SIMPLIFY -> "प्राप्त अभिव्यक्ति को सरल करें।"
        RuleKeys.DERIVATIVE_SOLUTION -> "अवकलज का परिणाम प्राप्त करें।"
        else -> key
    }
}
