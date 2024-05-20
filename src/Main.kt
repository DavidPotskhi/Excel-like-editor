import impl.ParserFactory
import impl.Table
import impl.TokenizerFactory


fun main() {
    val tokenizerFactory = TokenizerFactory()
    val parserFactory = ParserFactory()

    val table = Table(tokenizerFactory, parserFactory)
    val t = TableGUI(table)
    table.tableGUI = t

    t.createAndShowGUI()
}
