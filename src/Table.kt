class Table(val tableGUI: TableGUI) {
    data class Cell(var formula: String = "", var value: Int = 0)

    val cells = hashMapOf<String, Cell>()
    var outDependencies = hashMapOf<String, HashSet<String>>()
    var inDependencies = hashMapOf<String, HashSet<String>>()
    val interpreter: Interpreter = Interpreter(this)

    fun  HashMap<String, HashSet<String>>.getOrCreate(key: String): HashSet<String> {
        this.getOrPut(key) { HashSet<String>() }
        return this[key]!!
    }

    fun  HashMap<String, Cell>.getOrCreate(key: String): Cell {
        this.getOrPut(key) { Cell() }
        return this[key]!!
    }

    fun updateDependencies(dependings: HashSet<String>, cellRef: String) {
        for (cell in dependings) {
            if (!cells.containsKey(cell)) {
                throw TableException("There is no value in $cell in order to perform formula in $cellRef")
            }
        }
        clearDependencies(cellRef)

        for (cell in dependings) {
            outDependencies.getOrCreate(cell).add(cellRef)
            inDependencies.getOrCreate(cellRef).add(cell)
        }

    }


    fun makeCellEmpty(cellRef: String) {
//        try {
//

            if (!cells.containsKey(cellRef)) {
                return
            }
            if (outDependencies.getOrCreate(cellRef).isNotEmpty()) {
                var dependings = ""
                for (cell in outDependencies.getOrCreate(cellRef)) {
                    dependings += "$cell, "
                }
                throw TableException("$cellRef can't become empty since $dependings depend on its value")
            }
            for (cell in inDependencies.getOrCreate(cellRef)) {
                outDependencies.getOrCreate(cell).remove(cellRef)
            }
            cells.remove(cellRef)
//        } catch (e: RuntimeException) {
//            println(e.javaClass.toString().substring(6) + ":  " + e.message)
//        }
    }

    fun dfs(currentCell: String, topologicalOrder: MutableList<String>, visited: HashMap<String, State>): Boolean {
        visited[currentCell] = State.InProcess

        for (neighbour in outDependencies.getOrCreate(currentCell)) {
            when {
                visited[neighbour] == State.NonVisited -> if (dfs(neighbour, topologicalOrder, visited)) return true
                visited[neighbour] == State.Visited -> continue
                visited[neighbour] == State.InProcess -> return true
            }
        }

        visited[currentCell] = State.Visited

        topologicalOrder.add(currentCell)
        return false;
    }

    enum class State {
        NonVisited, InProcess, Visited
    }

    fun clearDependencies(cellRef: String) {
        for (cell in inDependencies.getOrCreate(cellRef)) {
            outDependencies.getOrCreate(cell).remove(cellRef)
        }
        inDependencies.getOrCreate(cellRef).clear()
    }

    fun makeTopologicalTraversal(cellRef: String, formula: String) {
        val topologicalOrder = mutableListOf<String>()
        val visited = hashMapOf<String, State>()
        for (cell in cells.keys) {
            visited[cell] = State.NonVisited
        }
        val isCycle: Boolean = dfs(cellRef, topologicalOrder, visited)
        if (isCycle) {
            clearDependencies(cellRef)
            val parser = Parser(Tokenizer(cells.getOrCreate(cellRef).formula).tokenize())
            parser.expressionRead()
            updateDependencies(parser.dependingOnCells, cellRef)
            println("Cycle detected: $cellRef")
        } else {
            cells.getOrCreate(cellRef).formula = formula
            for (cell in topologicalOrder.reversed()) {
                val parser = Parser(Tokenizer(cells.getOrCreate(cell).formula).tokenize())
                val instructions = parser.parseAndGetInstructions()
                cells.getOrCreate(cell).value = interpreter.executeInstructions(instructions)
                println("Updating $cell as dep of $cellRef to ${cells.getOrCreate(cell).value}")
                tableGUI.setValueAt(cell, cells.getOrCreate(cell).value)
            }
        }
    }

    fun modifyCellWithNonEmptyFormula(cellRef: String, formula: String) {
//        try {
            val parser = Parser(Tokenizer(formula).tokenize())
            parser.expressionRead()
            Interpreter(this).executeInstructions(parser.instructionGenerator.outputQueue)
            updateDependencies(parser.dependingOnCells, cellRef)
            makeTopologicalTraversal(cellRef, formula)
//        } catch (e: RuntimeException) {
//            println(e.javaClass.toString().substring(6) + ":  " + e.message)
//        }
    }

    fun modifyCell(cellRef: String, formula: String) {
        when {
            formula.isEmpty() -> makeCellEmpty(cellRef)
            else -> modifyCellWithNonEmptyFormula(cellRef, formula)
        }
    }
}