package com.vahitkeskin.matatik.ui.math

/**
 * LaTeX dizgesini, harici bağımlılık olmadan okunur Unicode matematik metnine çevirir.
 *
 * Tam bir TeX dizgicisi değildir; CAS motorunun ürettiği alt kümeyi (kesir, üs,
 * logaritma, taban değiştirme, ok işaretleri) düzgün gösterir. Ağır bir LaTeX
 * kütüphanesi gerektiğinde bu katman değiştirilebilir.
 */
object LatexDisplay {

    private val superscripts = mapOf(
        '0' to '⁰', '1' to '¹', '2' to '²', '3' to '³', '4' to '⁴',
        '5' to '⁵', '6' to '⁶', '7' to '⁷', '8' to '⁸', '9' to '⁹',
        '+' to '⁺', '-' to '⁻', 'n' to 'ⁿ', 'x' to 'ˣ', '(' to '⁽', ')' to '⁾'
    )

    private val subscripts = mapOf(
        '0' to '₀', '1' to '₁', '2' to '₂', '3' to '₃', '4' to '₄',
        '5' to '₅', '6' to '₆', '7' to '₇', '8' to '₈', '9' to '₉'
    )

    fun render(latex: String): String {
        var s = latex
        s = replaceFrac(s)
        s = replaceSuperscripts(s)
        s = replaceLogSubscripts(s)
        s = applySymbols(s)
        // Kalan süslü parantezleri temizle
        s = s.replace("{", "").replace("}", "")
        return s.replace(Regex(" +"), " ").trim()
    }

    /** `\frac{A}{B}` → `(A)/(B)` (iç içe destekli). */
    private fun replaceFrac(input: String): String {
        var s = input
        while (true) {
            val idx = s.indexOf("\\frac")
            if (idx < 0) break
            val firstBrace = s.indexOf('{', idx)
            if (firstBrace < 0) break
            val numEnd = matchBrace(s, firstBrace)
            if (numEnd < 0) break
            val secondBrace = s.indexOf('{', numEnd + 1)
            if (secondBrace < 0) break
            val denEnd = matchBrace(s, secondBrace)
            if (denEnd < 0) break
            val num = render(s.substring(firstBrace + 1, numEnd))
            val den = render(s.substring(secondBrace + 1, denEnd))
            val replacement = "($num)/($den)"
            s = s.substring(0, idx) + replacement + s.substring(denEnd + 1)
        }
        return s
    }

    /** `X^{...}` → üst simge (basitse) ya da `X^(...)`. */
    private fun replaceSuperscripts(input: String): String {
        var s = input
        while (true) {
            val caret = s.indexOf('^')
            if (caret < 0) break
            val brace = caret + 1
            val (content, end) = if (brace < s.length && s[brace] == '{') {
                val close = matchBrace(s, brace)
                if (close < 0) break
                s.substring(brace + 1, close) to close
            } else if (brace < s.length) {
                s[brace].toString() to brace
            } else break
            val sup = content.map { superscripts[it] ?: it }.joinToString("")
            val replacement = if (content.all { superscripts.containsKey(it) }) sup else "^($content)"
            s = s.substring(0, caret) + replacement + s.substring(end + 1)
        }
        return s
    }

    /** `\log_{B}` → `logᴮ` benzeri alt simge. */
    private fun replaceLogSubscripts(input: String): String {
        var s = input
        while (true) {
            val idx = s.indexOf("\\log_")
            if (idx < 0) break
            val brace = idx + 5
            val (content, end) = if (brace < s.length && s[brace] == '{') {
                val close = matchBrace(s, brace)
                if (close < 0) break
                s.substring(brace + 1, close) to close
            } else if (brace < s.length) {
                s[brace].toString() to brace
            } else break
            val sub = content.map { subscripts[it] ?: it }.joinToString("")
            s = s.substring(0, idx) + "log$sub" + s.substring(end + 1)
        }
        return s.replace("\\log", "log")
    }

    private fun applySymbols(input: String): String = input
        .replace("\\left(", "(")
        .replace("\\right)", ")")
        .replace("\\cdot", "·")
        .replace("\\times", "×")
        .replace("\\div", "÷")
        .replace("\\ln", "ln")
        .replace("\\infty", "∞")
        .replace("\\Rightarrow", "⇒")
        .replace("\\iff", "⟺")
        .replace("\\pi", "π")

    /** `open` konumundaki `{` ile eşleşen `}` indeksini döndürür; yoksa -1. */
    private fun matchBrace(s: String, open: Int): Int {
        var depth = 0
        for (i in open until s.length) {
            when (s[i]) {
                '{' -> depth++
                '}' -> {
                    depth--
                    if (depth == 0) return i
                }
            }
        }
        return -1
    }
}
