package impl
import interfaces.InstructionGenerator
import interfaces.ParserException
import java.util.*
import interfaces.Token
import interfaces.Instruction

class InstructionGenerator() :  InstructionGenerator {
    private val holdingStack: MutableList<Instruction> = mutableListOf<Instruction>()
    override val outputQueue: Queue<Instruction> = LinkedList<Instruction>()


    companion object {
        val precedenceMap =
            hashMapOf<Instruction, Int>(Function.minusFunc to 2, Function.plusFunc to 2, Function.multiplyFunc to 3)
    }

    private fun greaterPrecedence(lhv: Function, rhv: Function): Boolean {
        if (!precedenceMap.containsKey(lhv)) return true
        if (!precedenceMap.containsKey(rhv)) return false
        return precedenceMap[lhv]!! >= precedenceMap[rhv]!!
    }

    private fun addFuncInstruction(functionInstruction: Function) {

        while ((holdingStack.isNotEmpty()) && (holdingStack.last() != OpenBracket) && (greaterPrecedence(
                holdingStack.last() as Function,
                functionInstruction
            ))
        ) {
            outputQueue.add(holdingStack.removeLast())
        }
        holdingStack.add(functionInstruction)
    }

    private fun addCloseBracketInstruction() {
        while ((holdingStack.isNotEmpty()) && (holdingStack.last() != OpenBracket)) {
            outputQueue.add(holdingStack.removeLast())
        }
        holdingStack.removeLast()
    }

    private fun drainFullyHoldingStack() {
        while (holdingStack.isNotEmpty()) outputQueue.add(holdingStack.removeLast())
    }

    override fun addToken(token: Token) {
        when (token) {
            is NumberToken -> outputQueue.add(Number(token.value))
            is CellReferenceToken -> outputQueue.add(CellReference(token.cellRef))
            is FunctionToken -> addFuncInstruction(Function(token.name))
            is OpenBracketToken -> holdingStack.add(OpenBracket)
            is CloseBracketToken -> addCloseBracketInstruction()
            is EofToken -> drainFullyHoldingStack()
            is BinOperandToken -> addFuncInstruction(Function(token.operand))
            else -> throw ParserException("Parser passed the wrong token to the InstructionGenerator class, got: $token")
        }
    }
}