package interfaces


class TableException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)

}



interface Table {
    val cells: HashMap<String, Cell>
    fun modifyCell(cellRef: String, formula: String)

    interface Cell {
        var formula: String
        var value: Int
    }
}