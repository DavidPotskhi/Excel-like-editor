//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    println( Tokenizer("(").tokenize() )
    println(test("a", OpenBracketToken))
}

fun test(input: String, vararg ts: Token): List<Token> = listOf(*ts)