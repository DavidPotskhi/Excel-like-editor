interface Token

data class NumberToken(val value: Int) : Token
object OpenBracketToken : Token
object CloseBracketToken : Token
data class FunctionToken(val name: String) : Token
data class CellReferenceToken(val cellRef: String) : Token
object QuoteToken : Token
data class BinOperandToken(val operand: String) : Token


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


class Tokenizer(private var inputFormula: String) {

    private var iterator: PeekingIterator = PeekingIterator(this.inputFormula.iterator())
    private var currentToken: Token? = null

    init {
        this.currentToken = next()
    }

    private fun isEnd(): Boolean {
        return !iterator.hasNext()
    }

    private fun next(): Token? {
        if (!iterator.hasNext()) {
            return null;
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
                currentToken = QuoteToken
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
        return currentToken;
    }

    private fun getToken(): Token? {
        return currentToken
    }

    fun tokenize(): List<Token> {
        val result: MutableList<Token> = mutableListOf<Token>()
        while (!isEnd()) {
            result.add(getToken()!!)
            next()
        }
        if (getToken() != null) result.add(getToken()!!)
        return result
    }
}