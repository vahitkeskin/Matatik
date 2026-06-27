package com.vahitkeskin.matatik.core.domain.parser

import com.vahitkeskin.matatik.core.domain.ast.Equation
import com.vahitkeskin.matatik.core.domain.ast.Expr

/**
 * Recursive-descent (özyinelemeli iniş) ayrıştırıcı.
 *
 * Dilbilgisi (öncelik artan sırada):
 * ```
 * equation := expr ('=' expr)?
 * expr     := term (('+' | '-') term)*
 * term     := unary ( ('*' | '/') unary | implicitMul )*
 * unary    := '-' unary | power
 * power    := atom ('^' unary)?           // sağ-ilişkili
 * atom     := NUMBER | function | IDENT | '(' expr ')'
 * function := 'ln' '(' expr ')'
 *           | 'log' ('_' NUMBER)? '(' expr ')'   // taban verilmezse 10
 * ```
 * Örtük çarpma desteklenir: `2x`, `3(x+1)`, `2 x`, `(x+1)(x-1)`.
 */
class Parser(input: String) {

    private val tokens: List<Token> = Tokenizer.tokenize(input)
    private var pos = 0

    private val current: Token get() = tokens[pos]

    private fun advance(): Token = tokens[pos++]

    private fun expect(type: TokenType): Token {
        if (current.type != type) {
            throw MathParseException(
                "Beklenen '$type' fakat '${current.text.ifEmpty { "<son>" }}' bulundu",
                current.position
            )
        }
        return advance()
    }

    /** Denklem (veya tek taraflı ifade) ayrıştırır. */
    fun parseEquation(): Equation {
        val lhs = parseExpr()
        return if (current.type == TokenType.EQUALS) {
            advance()
            val rhs = parseExpr()
            ensureEnd()
            Equation(lhs, rhs)
        } else {
            ensureEnd()
            Equation(lhs, Expr.Num(0.0))
        }
    }

    /** Yalnızca bir ifade (= olmadan) ayrıştırır. */
    fun parseExpression(): Expr {
        val e = parseExpr()
        ensureEnd()
        return e
    }

    private fun ensureEnd() {
        if (current.type != TokenType.EOF) {
            throw MathParseException("Beklenmeyen ifade: '${current.text}'", current.position)
        }
    }

    private fun parseExpr(): Expr {
        var left = parseTerm()
        while (current.type == TokenType.PLUS || current.type == TokenType.MINUS) {
            val op = advance().type
            val right = parseTerm()
            left = if (op == TokenType.PLUS) Expr.Add(left, right) else Expr.Sub(left, right)
        }
        return left
    }

    private fun parseTerm(): Expr {
        var left = parseUnary()
        while (true) {
            when (current.type) {
                TokenType.STAR -> {
                    advance(); left = Expr.Mul(left, parseUnary())
                }
                TokenType.SLASH -> {
                    advance(); left = Expr.Div(left, parseUnary())
                }
                // Örtük çarpma: yeni bir atom doğrudan başlıyorsa
                TokenType.NUMBER, TokenType.IDENT, TokenType.LPAREN ->
                    left = Expr.Mul(left, parseUnary())
                else -> return left
            }
        }
    }

    private fun parseUnary(): Expr =
        if (current.type == TokenType.MINUS) {
            advance(); Expr.Neg(parseUnary())
        } else {
            parsePower()
        }

    private fun parsePower(): Expr {
        val base = parseAtom()
        return if (current.type == TokenType.CARET) {
            advance()
            Expr.Pow(base, parseUnary()) // sağ-ilişkili
        } else {
            base
        }
    }

    private fun parseAtom(): Expr = when (current.type) {
        TokenType.NUMBER -> {
            val t = advance()
            Expr.Num(t.text.toDoubleOrNull()
                ?: throw MathParseException("Geçersiz sayı: '${t.text}'", t.position))
        }
        TokenType.LPAREN -> {
            advance()
            val inner = parseExpr()
            expect(TokenType.RPAREN)
            inner
        }
        TokenType.IDENT -> parseIdentifier()
        else -> throw MathParseException(
            "Beklenmeyen sembol: '${current.text.ifEmpty { "<son>" }}'", current.position
        )
    }

    private fun parseIdentifier(): Expr {
        val name = advance().text
        return when (val lowerName = name.lowercase()) {
            "sqrt" -> {
                expect(TokenType.LPAREN)
                val arg = parseExpr()
                expect(TokenType.RPAREN)
                Expr.Pow(arg, Expr.Num(0.5))
            }
            "ln" -> {
                expect(TokenType.LPAREN)
                val arg = parseExpr()
                expect(TokenType.RPAREN)
                Expr.Ln(arg)
            }
            "log" -> {
                val base: Expr = if (current.type == TokenType.UNDERSCORE) {
                    advance()
                    parseAtom() // taban: sayı veya parantezli ifade
                } else {
                    Expr.Num(10.0)
                }
                expect(TokenType.LPAREN)
                val arg = parseExpr()
                expect(TokenType.RPAREN)
                Expr.Log(base, arg)
            }
            "d" -> parseDerivativeNotation()
            "deriv" -> {
                expect(TokenType.LPAREN)
                val expr = parseExpr()
                expect(TokenType.COMMA)
                val varToken = expect(TokenType.IDENT)
                expect(TokenType.RPAREN)
                Expr.Derivative(expr, varToken.text)
            }
            "lim", "limit" -> {
                expect(TokenType.LPAREN)
                val expr = parseExpr()
                expect(TokenType.COMMA)
                val varToken = expect(TokenType.IDENT)
                expect(TokenType.COMMA)
                val target = parseExpr()
                expect(TokenType.RPAREN)
                Expr.Limit(expr, varToken.text, target)
            }
            "int", "integral" -> {
                expect(TokenType.LPAREN)
                val expr = parseExpr()
                expect(TokenType.COMMA)
                val varToken = expect(TokenType.IDENT)
                if (current.type == TokenType.COMMA) {
                    advance()
                    val lower = parseExpr()
                    expect(TokenType.COMMA)
                    val upper = parseExpr()
                    expect(TokenType.RPAREN)
                    Expr.Integral(expr, varToken.text, lower, upper)
                } else {
                    expect(TokenType.RPAREN)
                    Expr.Integral(expr, varToken.text, null, null)
                }
            }
            "sin", "cos", "tan", "cot" -> {
                expect(TokenType.LPAREN)
                val arg = parseExpr()
                expect(TokenType.RPAREN)
                Expr.Trig(lowerName, arg)
            }
            else -> Expr.Variable(name)
        }
    }

    /**
     * `d/dx(...)` notasyonunu ayrıştırır.
     * `d` zaten tüketilmiş durumda; `/` `d` `x` `(` expr `)` beklenir.
     */
    private fun parseDerivativeNotation(): Expr {
        if (current.type != TokenType.SLASH) {
            // Basit 'd' değişkeni olabilir
            return Expr.Variable("d")
        }
        advance() // '/' tüket
        val dToken = expect(TokenType.IDENT) // 'd' harfi beklenir (dx'in d'si)
        if (dToken.text.length == 1 && dToken.text[0] == 'd') {
            // Sonraki token değişken adı olmalı (x, y, t vb.)
            // Eğer hemen IDENT geliyorsa: d/dx(...)
            throw MathParseException("Türev değişkeni bekleniyor", dToken.position)
        }
        // dToken.text "dx", "dy" gibi; ilk 'd' atlanır, kalan değişken adıdır
        val varName = if (dToken.text.startsWith("d") && dToken.text.length > 1) {
            dToken.text.substring(1)
        } else {
            throw MathParseException(
                "Türev notasyonu bekleniyor: d/dx(...), 'd${dToken.text}' alındı",
                dToken.position
            )
        }
        expect(TokenType.LPAREN)
        val expr = parseExpr()
        expect(TokenType.RPAREN)
        return Expr.Derivative(expr, varName)
    }
}
