package impl
import interfaces.Tokenizer
import interfaces.Token
import interfaces.TokenizerException
import interfaces.TokenizerFactory


data class NumberToken(val value: Int) : Token {
    companion object {
        val ZERO = NumberToken(0)
    }
}
object OpenBracketToken : Token
object CloseBracketToken : Token
data class FunctionToken(val name: String) : Token
data class CellReferenceToken(val cellRef: String) : Token
object CommaToken : Token
data class BinOperandToken(val operand: String) : Token {
    companion object {
        val MINUS = BinOperandToken("-")
        val PLUS = BinOperandToken("+")
        val MULTIPLY = BinOperandToken("*")
        val ALL = listOf(MINUS, PLUS, MULTIPLY)
    }
}

object EofToken : Token

class PeekingIterator(private val iterator: CharIterator) {
    private var hasNextPeeked: Boolean = false
    private var peekedElement: Char? = null

    fun hasNext(): Boolean {
        return hasNextPeeked || iterator.hasNext()
    }

    fun next(): Char {
        if (!hasNextPeeked) {
            return iterator.next()
        }
        val result = peekedElement
        hasNextPeeked = false
        peekedElement = null
        return result!!
    }

    fun peek(): Char {
        if (!hasNextPeeked) {
            hasNextPeeked = true
            peekedElement = iterator.next()
        }
        return peekedElement!!
    }
}


class Tokenizer(private var inputFormula: String) : Tokenizer{
    private var iterator: PeekingIterator = PeekingIterator(this.inputFormula.iterator())
    private lateinit var currentToken: Token

    init {
        next()
    }

    private fun isEnd(): Boolean {
        return !iterator.hasNext()
    }

    private fun next() {

        if (!iterator.hasNext()) {
            return
        }
        while (iterator.hasNext() && (iterator.peek().isWhitespace() || iterator.peek() == '\n'
                    || iterator.peek() == '\t' || iterator.peek() == '\r')
        ) {
            iterator.next();
        }

        when {
            iterator.peek() == '(' -> {
                currentToken = OpenBracketToken
                iterator.next()
            }

            iterator.peek() == ')' -> {
                currentToken = CloseBracketToken
                iterator.next()
            }

            iterator.peek() == ',' -> {
                currentToken = CommaToken
                iterator.next()
            }

            iterator.peek() == '+' || iterator.peek() == '-' || iterator.peek() == '*' -> {
                currentToken = BinOperandToken(
                    iterator.next().toString()
                )
            }

            iterator.peek().isDigit() -> {
                var integer: String = ""
                while (iterator.hasNext() && iterator.peek().isDigit()) {
                    integer += iterator.next()
                }
                val result: Int = integer.toInt()
                currentToken = NumberToken(result)
            }

            iterator.peek().isLetter() -> {
                var result: String = iterator.next().toString()
                if (!iterator.hasNext()) {
                    currentToken = FunctionToken(result)
                } else {
                    when {
                        iterator.peek().isDigit() -> {
                            while (iterator.hasNext() && iterator.peek().isDigit()) {
                                result += iterator.next()
                            }
                            currentToken = CellReferenceToken(result)
                        }

                        iterator.peek().isLetter() -> {
                            while (iterator.hasNext() && iterator.peek().isLetter()) {
                                result += iterator.next()
                            }
                            currentToken = FunctionToken(result)
                        }

                        else -> {
                            currentToken = FunctionToken(result)
                        }
                    }
                }
            }

            else -> throw TokenizerException("Tokenizer didn't find an acceptable token, token can't start with:" + iterator.peek())
        }

        while (iterator.hasNext() && (iterator.peek().isWhitespace() || iterator.peek() == '\n'
                    || iterator.peek() == '\t' || iterator.peek() == '\r')
        ) {
            iterator.next();
        }
    }

    private fun getToken(): Token {
        return currentToken
    }

    override fun tokenize(): List<Token> {
        val result: MutableList<Token> = mutableListOf<Token>()
        while (!isEnd()) {
            result.add(getToken())
            next()
        }
        result.add(getToken())
        result.add(EofToken)
        return result
    }
}

class TokenizerFactory : TokenizerFactory {
    override fun create(inputFormula: String): Tokenizer {
        return impl.Tokenizer(inputFormula)
    }

}