package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** العربية (Arapça) metinler. */
object ArString : LocalizedStrings {
    override val language = Language.AR

    override val appName = "Matatik"
    override val solverTitle = "حل خطوة بخطوة"
    override val inputPlaceholder = "أدخل معادلة، مثال: 2x + 3 = 7"
    override val solveButton = "حل"
    override val clearButton = "مسح"
    override val stepLabel = "خطوة"
    override val finalAnswerLabel = "الإجابة"
    override val examplesTitle = "أمثلة"
    override val emptyStateMessage = "أدخل معادلة لحلها"
    override val languageSelectorTitle = "اللغة"
    override val versionPrefix = "الإصدار"

    override val errorParse = "تعذر تحليل المدخل. يرجى التحقق من المعادلة."
    override val errorUnsupported = "هذه المسألة غير مدعومة بعد."
    override val errorEmpty = "يرجى إدخال معادلة."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "الجبر الأساسي"
        MathTopic.LOGARITHM -> "اللوغاريتم"
        MathTopic.CALCULUS_DERIVATIVE -> "المشتقة"
        MathTopic.CALCULUS_INTEGRAL -> "التكامل"
        MathTopic.MATRIX_OPERATIONS -> "عمليات المصفوفات"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "كتابة المعادلة المعطاة."
        RuleKeys.LINEAR_MOVE_TERMS -> "نقل الحدود المتغيرة إلى اليسار والثوابت إلى اليمين."
        RuleKeys.LINEAR_COMBINE -> "جمع الحدود المتشابهة."
        RuleKeys.LINEAR_DIVIDE -> "قسمة كلا الطرفين على المعامل."
        RuleKeys.LINEAR_SOLUTION -> "الحصول على حل المعادلة."
        RuleKeys.LOG_ORIGINAL -> "كتابة التعبير اللوغاريتمي المعطى."
        RuleKeys.LOG_CHANGE_OF_BASE -> "تطبيق قاعدة تغيير الأساس."
        RuleKeys.LOG_EVALUATE -> "حساب التعبير عددياً."
        RuleKeys.LOG_EXP_DEFINITION -> "استخدام التعريف الأسي للوغاريتم."
        RuleKeys.LOG_SOLUTION -> "حساب قيمة المتغير."
        RuleKeys.DERIVATIVE_ORIGINAL -> "كتابة تعبير المشتقة المعطى."
        RuleKeys.DERIVATIVE_POWER_RULE -> "تطبيق قاعدة القوة: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "مشتقة الثابت تساوي صفراً."
        RuleKeys.DERIVATIVE_SUM_RULE -> "قاعدة المجموع: اشتقاق كل حد على حدة."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "إخراج المعامل الثابت."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "تبسيط التعبير الناتج."
        RuleKeys.DERIVATIVE_SOLUTION -> "الحصول على نتيجة المشتقة."
        else -> key
    }
}
