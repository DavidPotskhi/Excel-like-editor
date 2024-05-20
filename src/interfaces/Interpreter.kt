package interfaces

import java.util.Queue

class InterpreterException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)

}

interface Interpreter {
    fun eval(instruction: Instruction, stack: MutableList<Int>)
    fun executeInstructions(instructions: Queue<Instruction>): Int
}