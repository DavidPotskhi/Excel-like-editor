


abstract class Token

data class NumberToken(val value: Int) : Token()
class OpenBracket : Token()
class CloseBracket : Token()
data class Function(val name: String) : Token()
data class CellReference(val cellReference: String) : Token()
class QuoteToken : Token()
data class BinOperand(val operand: String) : Token()


class PeekingIterator(private val iterator: CharIterator){
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

    fun peek() : Char {
        if (!hasNextPeeked) {
            hasNextPeeked = true
            peekedElement = iterator.next()
        }
        return peekedElement!!
    }
}




class Tokenizer(private var inputFormula: String) {

    private var iterator: PeekingIterator = PeekingIterator(this.inputFormula.iterator())
    private var currentToken: Token?

    init {
        this.currentToken = next()
    }

    fun isEnd() : Boolean {
        return !iterator.hasNext()
    }

    fun next() : Token? {
        if (!iterator.hasNext()) {
            return null;
        }

        while (iterator.hasNext() && (iterator.peek().isWhitespace() || iterator.peek() == '\n'
                    || iterator.peek() == '\t' || iterator.peek() == '\r')) {
            iterator.next();
        }

        when (iterator.peek()) {
            //else -> throw
        }

        while (iterator.hasNext() && (iterator.peek().isWhitespace() || iterator.peek() == '\n'
                    || iterator.peek() == '\t' || iterator.peek() == '\r')) {
            iterator.next();
        }
        return currentToken;
    }

    fun getToken() : Token? {
        return currentToken
    }
}