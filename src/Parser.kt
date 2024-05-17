class Parser(val tokenList: List<Token>) {

    val it: Iterator<Token> = tokenList.iterator()
    val holdingStack: MutableList<Entity> = mutableListOf()
    val outputStack: MutableList<Entity> = mutableListOf()

    private fun throwIfEndOfTokenStream(token: Token?) {
        if (!it.hasNext()) {
            throw ParserException(("Parser expected next token after" + token?.toString()))
        }
    }

    fun subExpressionRead() {

    }

    fun expressionRead() {
        if (!it.hasNext()) {
            return
        }
        TODO("Stack logic, put the open bracket on the stack")

        val currentToken = it.next()

        if (currentToken is BinOperandToken) {
            if (currentToken != BinOperandToken("-")) {
                throw ParserException("Parser expects unary minus, not $currentToken")
            }
            TODO("Stack logic")
            throwIfEndOfTokenStream(currentToken)
            currentToken = it.next()
        }
        when (currentToken) {
            is FunctionToken -> {}
            is OpenBracketToken -> {}
            is CellReferenceToken -> {}
            is NumberToken -> {}
            else -> throw ParserException("Parser expected token for the beginning of the expression but got: $currentToken")
        }


        TODO("Stack logic, put the open bracket on the stack")
        TODO("Return or BinOperand logic")
    }


}