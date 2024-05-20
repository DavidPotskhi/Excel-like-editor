package interfaces

interface  Token

class TokenizerException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
interface Tokenizer {
    fun tokenize(): List<Token>
}

interface TokenizerFactory {
    fun create(inputFormula: String): Tokenizer
}