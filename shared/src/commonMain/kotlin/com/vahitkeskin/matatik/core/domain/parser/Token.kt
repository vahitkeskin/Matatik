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

    fun tokenize(input: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var i = 0
        while (i < input.length) {
            val c = input[i]
            when {
                c.isWhitespace() -> i++
                c.isDigit() || c == '.' -> {
                    val start = i
                    while (i < input.length && (input[i].isDigit() || input[i] == '.')) i++
                    tokens += Token(TokenType.NUMBER, input.substring(start, i), start)
                }
                c.isLetter() -> {
                    val start = i
                    while (i < input.length && input[i].isLetter()) i++
                    tokens += Token(TokenType.IDENT, input.substring(start, i), start)
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
        tokens += Token(TokenType.EOF, "", input.length)
        return tokens
    }
}
