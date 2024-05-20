package interfaces

import java.util.Queue

interface InstructionGenerator {
    fun addToken(token: Token)
    val outputQueue: Queue<Instruction>
}