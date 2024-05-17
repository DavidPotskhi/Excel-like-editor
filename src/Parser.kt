class Parser(val tokenList: List<Token>) {

    val it: ListIterator<Token> = tokenList.listIterator()
    val holdingStack: MutableList<Entity> = mutableListOf()
    val outputStack: MutableList<Entity> = mutableListOf()
    var currentToken: Token? = null
    val returnTokens = listOf(CloseBracketToken, QuoteToken)

    private fun checkIfReturn(): Boolean {
        return returnTokens.contains(currentToken) || !it.hasNext()
    }

    private fun throwIfEndOfTokenStream(token: Token?) {
        if (!it.hasNext()) {
            throw ParserException(("Parser expected next token after" + token?.toString()))
        }
    }

    private fun expect(toExpect: List<Token>) {
        throwIfEndOfTokenStream(currentToken)
        if (!toExpect.contains(currentToken)) {
            throw ParserException("Expected $toExpect token but got $currentToken")
        }
    }

    private fun functionRead() {
        throwIfEndOfTokenStream(currentToken)
        currentToken = it.next()
        throwIfEndOfTokenStream(currentToken)
        currentToken = it.next()
        if (currentToken == CloseBracketToken) {
            // TODO("STACK LOGIC")
            return
        } else {
            currentToken = it.previous()
        }
        expect(listOf(OpenBracketToken))
        while (currentToken != CloseBracketToken) {
            expressionRead()
            expect(listOf(CloseBracketToken, QuoteToken))
            // TODO("stack logic")
        }
        // TODO("Stack logic")
    }

    private fun openBracketRead() {
        throwIfEndOfTokenStream(currentToken)
        expressionRead()
        expect(listOf(CloseBracketToken))
    }

    private fun cellReferenceRead() {
        // TODO("STACK LOGIC")
    }

    private fun numberRead() {
        // TODO("STACK LOGIC")
    }

    private fun firstBinOperandRead() {
        if (currentToken != BinOperandToken("-")) {
            throw ParserException("Parser expects unary minus, not $currentToken")
        }
        // TODO("Stack logic")
        throwIfEndOfTokenStream(currentToken)
        currentToken = it.next()
    }

    fun expressionRead() {
        if (!it.hasNext()) throw ParserException("Expression can't be empty")

        // TODO("Stack logic, put the open bracket on the stack")

        currentToken = it.next()

        if (checkIfReturn()) throw ParserException("Expression can't start with $currentToken")


        if (currentToken is BinOperandToken) firstBinOperandRead()
        when (currentToken) {
            is FunctionToken -> functionRead()
            is OpenBracketToken -> openBracketRead()
            is CellReferenceToken -> cellReferenceRead()
            is NumberToken -> numberRead()
            else -> throw ParserException("Parser expected token for the beginning of the expression but got: $currentToken")
        }


        // TODO("Stack logic, put the open bracket on the stack")

        if (checkIfReturn()) return

        currentToken = it.next()
        // TODO("STACK LOGIC")
        expect(listOf(BinOperandToken("+"), BinOperandToken("-"), BinOperandToken("*")))
        throwIfEndOfTokenStream(currentToken)
        expressionRead()
    }


}