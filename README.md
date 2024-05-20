### How to launch the project:
* Just run the ./src/Main.kt file

### Project specifications:
* Classes and their methods with main functionality:
    * Tokenizer -> tokenize() // tokenizes the input formula
    * Parser -> expressionRead() // parses the input formula
    * InstructionGenerator -> addToken(token: Token) // with usage of shunting yard algorithm adds tokens (later converted to instruction) 
  which will be later executed
    * Interpreter -> executeInstructions(instructions: Queue<Instruction>) // interprets the stack with instructions
    * Table -> modifyCell(cellRef: String, formula String) // modifies the cell content, checks all cell dependencies with Top Sort + Cycle Detection dfs
* How to edit cell:
  * Click on the cell, edit it and press enter (or click to another cell)