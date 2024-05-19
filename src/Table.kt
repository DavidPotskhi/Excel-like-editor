class Table {
    data class Cell(var formula: String = "", var value: Int = 0)

    val cells = hashMapOf<String, Cell>()
    var outDependencies = hashMapOf<String, HashSet<String>>()
    var inDependencies = hashMapOf<String, HashSet<String>>()
    val interpreter: Interpreter = Interpreter(this)


    fun updateDependencies(dependings: HashSet<String>, cellRef: String) {
        for (cell in dependings) {
            if (!cells.containsKey(cell)) {
                throw TableException("There are no value in $cell in order perform formula in $cellRef")
            }
            outDependencies[cell]!!.add(cellRef)
            inDependencies[cellRef]!!.add(cell)
        }
    }


    fun makeCellEmpty(cellRef: String) {
        if (!cells.containsKey(cellRef)) {
            return
        }
        if (outDependencies[cellRef]!!.isNotEmpty()) {
            var dependings = ""
            for (cell in outDependencies[cellRef]!!) {
                dependings += "$cell, "
            }
            throw TableException("$cellRef can't become empty since $dependings depend on its value")
        }
        for (cell in inDependencies[cellRef]!!) {
            outDependencies[cell]!!.remove(cellRef)
        }
        cells.remove(cellRef)
    }

    fun dfs(currentCell: String, topologicalOrder: MutableList<String>, visited: HashMap<String, State>): Boolean {
        visited[currentCell] = State.InProcess

        for (neighbour in outDependencies[currentCell]!!) {
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
        for (cell in inDependencies[cellRef]!!) {
            outDependencies[cell]!!.remove(cellRef)
        }
        inDependencies[cellRef]!!.clear()
    }

    fun makeTopologicalTraversal(cellRef: String, formula: String) {
        var topologicalOrder = mutableListOf<String>()
        val visited = hashMapOf<String, State>()
        for (cell in cells.keys) {
            visited[cell] = State.NonVisited
        }
        val isCycle: Boolean = dfs(cellRef, topologicalOrder, visited)
        if (isCycle) {
            clearDependencies(cellRef)
            val parser = Parser(Tokenizer(cells[cellRef]!!.formula).tokenize())
            parser.expressionRead()
            updateDependencies(parser.dependingOnCells, cellRef)
        } else {
            cells[cellRef]!!.formula = formula
            for (cell in topologicalOrder.reversed()) {
                val parser = Parser(Tokenizer(cells[cell]!!.formula).tokenize())
                val instructions = parser.parseAndGetInstructions()
                cells[cell]!!.value = interpreter.executeInstructions(instructions)
            }
        }
    }

    fun modifyCellWithNonEmptyFormula(cellRef: String, formula: String) {
        val parser = Parser(Tokenizer(formula).tokenize())
        parser.expressionRead()
        updateDependencies(parser.dependingOnCells, cellRef)
        makeTopologicalTraversal(cellRef, formula)
    }

    fun modifyCell(cellRef: String, formula: String) {
        when {
            formula.isEmpty() -> makeCellEmpty(cellRef)
            else -> modifyCellWithNonEmptyFormula(cellRef, formula)
        }
    }
}