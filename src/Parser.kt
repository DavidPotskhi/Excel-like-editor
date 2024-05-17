



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
        val currentToken = it.next()

        when {
            (currentToken is BinOperandToken) -> {

                TODO("Stack logic")
                throwIfEndOfTokenStream(currentToken)

            }
        }

    }


}