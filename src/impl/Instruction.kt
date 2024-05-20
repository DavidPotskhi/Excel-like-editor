package impl
import interfaces.Instruction

object OpenBracket : Instruction.OpenBracket

object CloseBracket : Instruction.CloseBracket

data class Number(override val number: Int) : Instruction.Number
data class CellReference(override val cellReference: String) : Instruction.CellReference
data class Function(override val functionName: String) : Instruction.Function {
    companion object {
        val plusFunc = Function("+")
        val minusFunc = Function("-")
        val multiplyFunc = Function("*")
    }
}
