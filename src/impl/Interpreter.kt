package impl

import interfaces.Instruction
import interfaces.Interpreter
import interfaces.InterpreterException
import interfaces.Table
//import impl.Instruction


import java.util.*
import kotlin.Function
import kotlin.Number
import kotlin.math.max

class Interpreter(val table: Table) : Interpreter {
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

    override fun eval(instruction: Instruction, stack: MutableList<Int>) {
        when (instruction) {
            is Instruction.Number -> stack.add(instruction.number)
            is Instruction.CellReference -> stack.add(table.cells[instruction.cellReference]!!.value)
            is Instruction.Function -> {
                if (!functions.containsKey(instruction.functionName)) {
                    throw InterpreterException("Interpreter doesn't have function called: $instruction")
                }
                functions[instruction.functionName]!!.invoke(stack)
            }

            else -> throw InterpreterException("Illegal instruction in Interpreter: $instruction")
        }
    }

    override fun executeInstructions(instructions: Queue<Instruction>): Int {
        val solveStack = mutableListOf<Int>()
        for (instruction in instructions) {
            eval(instruction, solveStack)
        }
        if (solveStack.size > 1) {
            throw InterpreterException("More that 1 instruction is left on the solving stack, you might've added extra arguments")
        }
        return solveStack.removeLast()
    }

}