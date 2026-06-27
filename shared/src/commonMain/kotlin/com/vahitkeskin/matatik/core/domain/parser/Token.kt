package com.vahitkeskin.matatik.core.domain.parser

/** Sözdizimsel token türleri. */
enum class TokenType {
    NUMBER, IDENT, PLUS, MINUS, STAR, SLASH, CARET,
    LPAREN, RPAREN, EQUALS, UNDERSCORE, COMMA, EOF
}

/** Tek bir token: tür, ham metin ve kaynak konumu. */
data class Token(val type: TokenType, val text: String, val position: Int)

/** Ayrıştırma sırasında oluşan hatalar. */
class MathParseException(message: String, val position: Int = -1) : Exception(message)

/**
 * Ham matematik girdisini token akışına çevirir.
 * Boşlukları yok sayar; çok karakterli sayıları ve tanımlayıcıları tek token yapar.
 */
object Tokenizer {

    private fun preprocess(input: String): String {
        var result = input
        result = result.replace("∫", "int")
        result = result.replace("√", "sqrt")
        result = result.replace("π", "pi")
        result = result.replace("θ", "theta")
        result = result.replace("∞", "inf")
        result = result.replace("→", ",")
        result = result.replace("->", ",")
        return expandSummations(result)
    }

    private fun expandSummations(input: String): String {
        val sb = StringBuilder()
        var i = 0
        while (i < input.length) {
            if ((i + 4 <= input.length && input.substring(i, i + 4) == "sum(") ||
                (i + 2 <= input.length && input.substring(i, i + 2) == "∑(")
            ) {
                val isSigma = input[i] == '∑'
                val startIdx = i
                val parenStart = if (isSigma) i + 2 else i + 4
                // Eşleşen kapayan parantezi bul
                var depth = 1
                var j = parenStart
                while (j < input.length && depth > 0) {
                    if (input[j] == '(') depth++
                    else if (input[j] == ')') depth--
                    if (depth > 0) j++
                }
                if (depth == 0 && j < input.length) {
                    val inner = input.substring(parenStart, j)
                    // Virgüllerle argümanları ayır (iç parantez derinliğine dikkat et)
                    val args = mutableListOf<String>()
                    val argSb = StringBuilder()
                    var argDepth = 0
                    for (c in inner) {
                        if (c == '(') argDepth++
                        else if (c == ')') argDepth--

                        if (c == ',' && argDepth == 0) {
                            args.add(argSb.toString().trim())
                            argSb.setLength(0)
                        } else {
                            argSb.append(c)
                        }
                    }
                    args.add(argSb.toString().trim())

                    if (args.size == 4) {
                        val expr = args[0]
                        val variable = args[1]
                        val startVal = args[2].toIntOrNull()
                        val endVal = args[3].toIntOrNull()
                        if (startVal != null && endVal != null && startVal <= endVal) {
                            val terms = (startVal..endVal).map { v ->
                                replaceVariableWithValue(expr, variable, v)
                            }
                            val expanded = terms.joinToString(" + ") { "($it)" }
                            sb.append(expanded)
                            i = j + 1
                            continue
                        }
                    }
                }
            }
            sb.append(input[i])
            i++
        }
        return sb.toString()
    }

    private fun replaceVariableWithValue(expr: String, varName: String, value: Int): String {
        val sb = StringBuilder()
        var i = 0
        while (i < expr.length) {
            if (expr.substring(i).startsWith(varName)) {
                val isPrevLetter = i > 0 && expr[i - 1].isLetterOrDigit()
                val nextIdx = i + varName.length
                val isNextLetter = nextIdx < expr.length && expr[nextIdx].isLetterOrDigit()
                if (!isPrevLetter && !isNextLetter) {
                    sb.append(value)
                    i += varName.length
                    continue
                }
            }
            sb.append(expr[i])
            i++
        }
        return sb.toString()
    }

    fun tokenize(input: String): List<Token> {
        val preprocessed = preprocess(input)
        val tokens = mutableListOf<Token>()
        var i = 0
        while (i < preprocessed.length) {
            val c = preprocessed[i]
            when {
                c.isWhitespace() -> i++
                c.isDigit() || c == '.' -> {
                    val start = i
                    while (i < preprocessed.length && (preprocessed[i].isDigit() || preprocessed[i] == '.')) i++
                    tokens += Token(TokenType.NUMBER, preprocessed.substring(start, i), start)
                }
                c.isLetter() -> {
                    val start = i
                    while (i < preprocessed.length && preprocessed[i].isLetter()) i++
                    tokens += Token(TokenType.IDENT, preprocessed.substring(start, i), start)
                }
                else -> {
                    val type = when (c) {
                        '+' -> TokenType.PLUS
                        '-', '−' -> TokenType.MINUS // ASCII '-' ve Unicode minus
                        '*', '×', '⋅' -> TokenType.STAR // *, ×, ⋅
                        '/', '÷' -> TokenType.SLASH // /, ÷
                        '^' -> TokenType.CARET
                        '(', '[', '{' -> TokenType.LPAREN
                        ')', ']', '}' -> TokenType.RPAREN
                        '=' -> TokenType.EQUALS
                        '_' -> TokenType.UNDERSCORE
                        ',' -> TokenType.COMMA
                        else -> throw MathParseException("Bilinmeyen karakter: '$c'", i)
                    }
                    tokens += Token(type, c.toString(), i)
                    i++
                }
            }
        }
        tokens += Token(TokenType.EOF, "", preprocessed.length)
        return tokens
    }
}
