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
* Where you can see error messages:
  * All error messages are printed to the standard output
* Functions and operator list:
  * There is support of only integer numbers in editor
  * The +, - and * binary operators are supported
  * unary minus operator is supported
  * the max(a,b) function is supported, it takes exactly two arguments and return the maximum of a and b
  * the inv(a) function is supported, it takes exactly one argument and returns the bitwise inverted a 