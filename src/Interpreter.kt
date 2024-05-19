import java.util.*
import kotlin.math.max

class Interpreter {

    val functions = hashMapOf<String, (MutableList<Int>) -> Unit>(
        "+" to {
            if (it.size < 2) {
                throw InterpreterException("+ operator should have exactly two arguments")
            }
            it.add(it.removeLast() + it.removeLast())
        },
        "*" to {
            if (it.size < 2) {
                throw InterpreterException("* operator should have exactly two arguments")
            }
            it.add(it.removeLast() * it.removeLast())
        },
        "-" to {
            if (it.size < 2) {
                throw InterpreterException("- operator should have exactly two arguments")
            }
            it.add(-it.removeLast() + it.removeLast())
        },
        "max" to {
            if (it.size < 2) {
                throw InterpreterException("max function should have exactly two arguments")
            }
            it.add(max(it.removeLast(), it.removeLast()))
        },
        "inv" to {
            if (it.size < 1) {
                throw InterpreterException("+ operator should have exactly two arguments")
            }
            it.add(it.removeLast().inv())
        }
    )

    fun eval(instruction: Instruction, stack: MutableList<Int>) {
        when (instruction) {
            is Number -> stack.add(instruction.number)
            is CellReference -> {}
            is Function -> {
                if (!functions.containsKey(instruction.functionName)) {
                    throw InterpreterException("Interpreter doesn't have function called: $instruction")
                }
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