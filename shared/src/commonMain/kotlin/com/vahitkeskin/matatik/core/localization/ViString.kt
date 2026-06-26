package com.vahitkeskin.matatik.core.localization

import com.vahitkeskin.matatik.core.domain.engine.RuleKeys
import com.vahitkeskin.matatik.core.domain.model.MathTopic

/** Tiếng Việt (Vietnamca) metinler. */
object ViString : LocalizedStrings {
    override val language = Language.VI

    override val appName = "Matatik"
    override val solverTitle = "Giải từng bước"
    override val inputPlaceholder = "Nhập phương trình, vd: 2x + 3 = 7"
    override val solveButton = "Giải"
    override val clearButton = "Xóa"
    override val stepLabel = "Bước"
    override val finalAnswerLabel = "Đáp án"
    override val examplesTitle = "Ví dụ"
    override val emptyStateMessage = "Nhập phương trình để giải"
    override val languageSelectorTitle = "Ngôn ngữ"
    override val versionPrefix = "Phiên bản"

    override val errorParse = "Không thể phân tích đầu vào. Vui lòng kiểm tra phương trình."
    override val errorUnsupported = "Bài toán này chưa được hỗ trợ."
    override val errorEmpty = "Vui lòng nhập một phương trình."

    override fun topicName(topic: MathTopic): String = when (topic) {
        MathTopic.BASIC_ALGEBRA -> "Đại số cơ bản"
        MathTopic.LOGARITHM -> "Logarit"
        MathTopic.CALCULUS_DERIVATIVE -> "Đạo hàm"
        MathTopic.CALCULUS_INTEGRAL -> "Tích phân"
        MathTopic.MATRIX_OPERATIONS -> "Phép toán ma trận"
    }

    override fun rule(key: String): String = when (key) {
        RuleKeys.LINEAR_ORIGINAL -> "Viết phương trình đã cho."
        RuleKeys.LINEAR_MOVE_TERMS -> "Chuyển các hạng tử biến sang trái, hằng số sang phải."
        RuleKeys.LINEAR_COMBINE -> "Gộp các hạng tử đồng dạng."
        RuleKeys.LINEAR_DIVIDE -> "Chia cả hai vế cho hệ số."
        RuleKeys.LINEAR_SOLUTION -> "Tìm nghiệm của phương trình."
        RuleKeys.LOG_ORIGINAL -> "Viết biểu thức logarit đã cho."
        RuleKeys.LOG_CHANGE_OF_BASE -> "Áp dụng công thức đổi cơ số."
        RuleKeys.LOG_EVALUATE -> "Tính giá trị biểu thức."
        RuleKeys.LOG_EXP_DEFINITION -> "Sử dụng định nghĩa mũ của logarit."
        RuleKeys.LOG_SOLUTION -> "Tính giá trị của biến."
        RuleKeys.DERIVATIVE_ORIGINAL -> "Viết biểu thức đạo hàm đã cho."
        RuleKeys.DERIVATIVE_POWER_RULE -> "Áp dụng quy tắc lũy thừa: d/dx[xⁿ] = n·xⁿ⁻¹."
        RuleKeys.DERIVATIVE_CONSTANT_RULE -> "Đạo hàm của hằng số bằng không."
        RuleKeys.DERIVATIVE_SUM_RULE -> "Quy tắc tổng: lấy đạo hàm từng hạng tử."
        RuleKeys.DERIVATIVE_COEFFICIENT_RULE -> "Đưa hệ số hằng ra ngoài."
        RuleKeys.DERIVATIVE_SIMPLIFY -> "Rút gọn biểu thức thu được."
        RuleKeys.DERIVATIVE_SOLUTION -> "Tìm kết quả đạo hàm."
        else -> key
    }
}
