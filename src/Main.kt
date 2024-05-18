//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val s = "1 + 2 * 3 - 4 + -max(1, max(1, max(1,2)))"
    val tokens = Tokenizer(s).tokenize()
    val parser = Parser(tokens)
    parser.expressionRead()
    val i = Interpreter()
    println(parser.instructionGenerator.outputQueue)
    println(i.executeInstructions(parser.instructionGenerator.outputQueue))
}
