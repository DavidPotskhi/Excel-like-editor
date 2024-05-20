import java.util.Queue
import interfaces.ParserException

class Parser(private val tokenList: List<Token>) {

    private val it: ListIterator<Token> = tokenList.listIterator()
    val instructionGenerator: InstructionGenerator = InstructionGenerator()
    private lateinit var currentToken: Token
    private val returnTokens = listOf(CloseBracketToken, CommaToken, EofToken)
    val dependingOnCells = HashSet<String>()



    fun parseAndGetInstructions(): Queue<Instruction> {
        expressionRead()
        return instructionGenerator.outputQueue
    }


    private fun acceptToken() {
        if (!it.hasNext()) {
            throw ParserException(("Parser expected next token after $currentToken"))
        }
        currentToken = it.next()
    }

    private fun expect(toExpect: List<Token>) {
        if (!toExpect.contains(currentToken)) {
            throw ParserException("Expected $toExpect token but got $currentToken")
        }
    }

    private fun functionRead() {
        val functionToken: FunctionToken = currentToken as FunctionToken
        acceptToken()
        expect(listOf(OpenBracketToken))
        acceptToken()
        if (currentToken != CloseBracketToken) {
            it.previous()
            while (currentToken != CloseBracketToken) {
                instructionGenerator.addToken(OpenBracketToken)
                expressionRead()
                instructionGenerator.addToken(CloseBracketToken)
                expect(listOf(CloseBracketToken, CommaToken))
            }
        }
        instructionGenerator.addToken(functionToken)
    }

    private fun openBracketRead() {
        instructionGenerator.addToken(currentToken) // adding OpenBracket
        expressionRead()
        expect(listOf(CloseBracketToken))
        instructionGenerator.addToken(currentToken) // adding CloseBracket
    }

    private fun cellReferenceRead() {
        instructionGenerator.addToken(currentToken)
        dependingOnCells.add((currentToken as CellReferenceToken).cellRef)
    }

    private fun numberRead() {
        instructionGenerator.addToken(currentToken)
    }

    private fun firstBinOperandRead() {
        if (currentToken != BinOperandToken.MINUS) {
            throw ParserException("Parser expects unary minus, not $currentToken")
        }
        instructionGenerator.addToken(NumberToken.ZERO)
        instructionGenerator.addToken(BinOperandToken.MINUS)
        acceptToken()
    }

    fun expressionRead() {

        instructionGenerator.addToken(OpenBracketToken)
        acceptToken()

        if (returnTokens.contains(currentToken)) throw ParserException("Expression can't start with $currentToken")

        if (currentToken is BinOperandToken) firstBinOperandRead()
        when (currentToken) {
            is FunctionToken -> functionRead()
            is OpenBracketToken -> openBracketRead()
            is CellReferenceToken -> cellReferenceRead()
            is NumberToken -> numberRead()
            else -> throw ParserException("Parser expected token for the beginning of the expression but got: $currentToken")
        }


        instructionGenerator.addToken(CloseBracketToken)

        acceptToken()

        if (returnTokens.contains(currentToken)) {
            if (currentToken == EofToken) {
                instructionGenerator.addToken(EofToken)
            }
            return
        }
        expect(BinOperandToken.ALL)
        instructionGenerator.addToken(currentToken) // adding BinOperandToken
        expressionRead()
    }


}