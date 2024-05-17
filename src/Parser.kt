class Parser(val tokenList: List<Token>) {

    val it: ListIterator<Token> = tokenList.listIterator()
    val holdingStack: MutableList<Entity> = mutableListOf()
    val outputStack: MutableList<Entity> = mutableListOf()
    var currentToken: Token? = null
    val returnTokens = listOf(CloseBracketToken, QuoteToken)



    private fun throwIfEndOfTokenStream() {
        if (!it.hasNext()) {
            throw ParserException(("Parser expected next token after" + currentToken?.toString()))
        }
    }

    private fun expect(toExpect: List<Token>) {
        if (!toExpect.contains(currentToken)) {
            throw ParserException("Expected $toExpect token but got $currentToken")
        }
    }

    private fun functionRead() {
        throwIfEndOfTokenStream()
        currentToken = it.next()
        expect(listOf(OpenBracketToken))
        throwIfEndOfTokenStream()
        currentToken = it.next()
        if (currentToken == CloseBracketToken) {
            // TODO("STACK LOGIC")
            return
        } else {
            it.previous()
        }
        while (currentToken != CloseBracketToken) {
            expressionRead()
            expect(listOf(CloseBracketToken, QuoteToken))
            // TODO("stack logic")
        }
        // TODO("Stack logic")
    }

    private fun openBracketRead() {
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
        throwIfEndOfTokenStream()
        currentToken = it.next()
    }

    fun expressionRead() {
        if (!it.hasNext()) throw ParserException("Expression can't be empty")

        // TODO("Stack logic, put the open bracket on the stack")

        currentToken = it.next()

        if (returnTokens.contains(currentToken)) throw ParserException("Expression can't start with $currentToken")


        if (currentToken is BinOperandToken) firstBinOperandRead()
        when (currentToken) {
            is FunctionToken -> functionRead()
            is OpenBracketToken -> openBracketRead()
            is CellReferenceToken -> cellReferenceRead()
            is NumberToken -> numberRead()
            else -> throw ParserException("Parser expected token for the beginning of the expression but got: $currentToken")
        }


        // TODO("Stack logic, put the open bracket on the stack")

        if (!it.hasNext()) return

        currentToken = it.next()
        if (returnTokens.contains(currentToken)) return
        // TODO("STACK LOGIC")
        expect(listOf(BinOperandToken("+"), BinOperandToken("-"), BinOperandToken("*")))
        expressionRead()
    }


}