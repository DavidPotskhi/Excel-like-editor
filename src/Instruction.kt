import Function


interface Instruction

object OpenBracket : Instruction

object CloseBracket : Instruction

data class Number(val number: Int) : Instruction
data class CellReference(val cellReference: String) : Instruction
data class Function(val functionName: String) : Instruction {
    companion object {
        val plusFunc = Function("+")
        val minusFunc = Function("-")
        val multiplyFunc = Function("*")
    }


}


// MutableList<Instruction>, Int) -> Instruction