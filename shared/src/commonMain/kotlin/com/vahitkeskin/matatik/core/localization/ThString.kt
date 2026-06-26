package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** ไทย (Tayca) metinler. */
object ThString : LocalizedStrings {
    override val language = Language.TH

    override val appName = "Matatik"
    override val solverTitle = "ตัวแก้ทีละขั้นตอน"
    override val inputPlaceholder = "ป้อนสมการ เช่น 2x + 3 = 7"
    override val solveButton = "แก้"
    override val clearButton = "ล้าง"
    override val stepLabel = "ขั้นตอน"
    override val finalAnswerLabel = "คำตอบ"
    override val examplesTitle = "ตัวอย่าง"
    override val emptyStateMessage = "ป้อนสมการเพื่อแก้"
    override val languageSelectorTitle = "ภาษา"
    override val versionPrefix = "เวอร์ชัน"

    override val errorParse = "ไม่สามารถวิเคราะห์อินพุตได้ กรุณาตรวจสอบสมการ"
    override val errorUnsupported = "ปัญหานี้ยังไม่รองรับ"
    override val errorEmpty = "กรุณาป้อนสมการ"

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "พีชคณิตพื้นฐาน"
        MathTopic.LOGARITHM -> "ลอการิทึม"
        MathTopic.CALCULUS_DERIVATIVE -> "อนุพันธ์"
        MathTopic.CALCULUS_INTEGRAL -> "ปริพันธ์"
        MathTopic.MATRIX_OPERATIONS -> "การดำเนินการเมทริกซ์"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "เขียนสมการที่กำหนดให้"
        RuleKeys.LINEAR_MOVE_TERMS -> "ย้ายพจน์ตัวแปรไปทางซ้าย ค่าคงที่ไปทางขวา"
        RuleKeys.LINEAR_COMBINE -> "รวมพจน์ที่คล้ายกัน"
        RuleKeys.LINEAR_DIVIDE -> "หารทั้งสองข้างด้วยสัมประสิทธิ์"
        RuleKeys.LINEAR_SOLUTION -> "ได้คำตอบของสมการ"
        RuleKeys.LOG_ORIGINAL -> "เขียนนิพจน์ลอการิทึมที่กำหนดให้"
        RuleKeys.LOG_CHANGE_OF_BASE -> "ใช้สูตรเปลี่ยนฐาน"
        RuleKeys.LOG_EVALUATE -> "คำนวณนิพจน์เป็นตัวเลข"
        RuleKeys.LOG_EXP_DEFINITION -> "ใช้นิยามเลขชี้กำลังของลอการิทึม"
        RuleKeys.LOG_SOLUTION -> "คำนวณค่าของตัวแปร"
        RuleKeys.DERIVATIVE_ORIGINAL -> "เขียนนิพจน์อนุพันธ์ที่กำหนดให้"
        RuleKeys.DERIVATIVE_POWER_RULE -> "ใช้กฎกำลัง: d/dx[xⁿ] = n·xⁿ⁻¹"
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "อนุพันธ์ของค่าคงที่เท่ากับศูนย์"
        RuleKeys.DERIVATIVE_SUM_RULE -> "กฎผลรวม: หาอนุพันธ์แต่ละพจน์แยกกัน"
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "ดึงสัมประสิทธิ์คงที่ออก"
        RuleKeys.DERIVATIVE_SIMPLIFY -> "ทำนิพจน์ที่ได้ให้ง่ายขึ้น"
        RuleKeys.DERIVATIVE_SOLUTION -> "ได้ผลลัพธ์ของอนุพันธ์"
        else -> key
    }
}
