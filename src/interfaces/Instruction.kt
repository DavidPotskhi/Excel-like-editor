package interfaces

sealed interface Instruction {
    interface OpenBracket : Instruction
    interface CloseBracket : Instruction
    interface Number : Instruction {
        val number: Int
    }

    interface CellReference : Instruction {
        val cellReference: String
    }

    interface Function : Instruction {
        val functionName: String
    }
}