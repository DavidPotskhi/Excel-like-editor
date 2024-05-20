package interfaces

import java.util.Queue

class ParserException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)

}
interface Parser {
    val dependingOnCells: HashSet<String>
    val instructionGenerator: InstructionGenerator
    fun parseAndGetInstructions(): Queue<Instruction>
    fun expressionRead()
}

interface ParserFactory {
    fun create(tokenList: List<Token>): Parser
}