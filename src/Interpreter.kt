import java.util.*
import kotlin.math.max

class Interpreter {

    val functions = hashMapOf<String, (MutableList<Int>) -> Unit>(
        "+" to { it.add(it.removeLast() + it.removeLast()) },
        "*" to { it.add(it.removeLast() * it.removeLast()) },
        "-" to { it.add(-it.removeLast() + it.removeLast()) },
        "max" to { it.add(max(it.removeLast(), it.removeLast())) },
    )

    fun eval(instruction: Instruction, stack: MutableList<Int>) {
        when (instruction) {
            is Number -> stack.add(instruction.number)
            is CellReference -> {}
            is Function -> {
                functions[instruction.functionName]!!.invoke(stack)
            }
            else -> throw InterpreterException("Illegal instruction in Interpreter: $instruction")
        }
    }
    fun executeInstructions(instructions: Queue<Instruction>): Int {
        val solveStack = mutableListOf<Int>()
        for (instruction in instructions) {
            eval(instruction, solveStack)
        }
        return solveStack.removeLast()
    }



}