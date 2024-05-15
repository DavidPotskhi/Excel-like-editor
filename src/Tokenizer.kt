


abstract class Token

data class NumberToken(val value: Int) : Token()
class OpenBracket : Token()
class CloseBracket : Token()
data class Function(val name: String) : Token()
data class CellReference(val cellReference: String) : Token()
class QuoteToken : Token()

class Tokenizer(val inputFormula: String, var iterator: CharIterator) {

    constructor(inputFormula: String) : this(inputFormula, inputFormula.iterator())

    fun isEnd() : Boolean {
        return !iterator.hasNext();
    }

    fun next() {

    }

    fun getToken() : Token {
        return OpenBracket()
    }
}