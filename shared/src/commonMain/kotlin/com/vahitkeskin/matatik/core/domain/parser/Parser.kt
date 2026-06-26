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
        return when (name.lowercase()) {
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
            else -> Expr.Variable(name)
        }
    }
}
