import com.sun.jdi.connect.Connector.Argument
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class TokenizerTest {

    private val tokenList: MutableList<Token?> = mutableListOf<Token?>()

    @BeforeEach
    fun setup() {
        tokenList.clear();
    }


    @ParameterizedTest
    @MethodSource("testCases")
    fun shouldReturnCorrectResult(input: String, tokenList: List<Token>) {
        assertEquals(tokenList, Tokenizer(input).tokenize())
    }

    companion object {
        @JvmStatic
        fun testCases(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("(", listOf(OpenBracketToken)),
                Arguments.of("-", listOf(BinOperandToken("-"))),
                Arguments.of("   (  ", listOf(OpenBracketToken)),
                Arguments.of("+  ", listOf(BinOperandToken("+"))),
                Arguments.of(" 5  ", listOf(NumberToken(5))),
                Arguments.of("12345", listOf(NumberToken(12345))),
                Arguments.of("  Func ", listOf(FunctionToken("Func"))),
                Arguments.of("  ,", listOf(QuoteToken)),
                Arguments.of(
                    "()+-",
                    listOf(OpenBracketToken, CloseBracketToken, BinOperandToken("+"), BinOperandToken("-"))
                ),
                Arguments.of(
                    "(A2+A1)",
                    listOf(
                        OpenBracketToken,
                        CellReferenceToken("A2"),
                        BinOperandToken("+"),
                        CellReferenceToken("A1"),
                        CloseBracketToken
                    )
                ),
                Arguments.of(
                    "( A2000   + A1)",
                    listOf(
                        OpenBracketToken,
                        CellReferenceToken("A2000"),
                        BinOperandToken("+"),
                        CellReferenceToken("A1"),
                        CloseBracketToken
                    )
                ),
                Arguments.of(
                    "  pow (1,f()) + B100", listOf(
                        FunctionToken("pow"),
                        OpenBracketToken,
                        NumberToken(1),
                        QuoteToken,
                        FunctionToken("f"),
                        OpenBracketToken,
                        CloseBracketToken,
                        CloseBracketToken,
                        BinOperandToken("+"),
                        CellReferenceToken("B100")
                    )
                ),
                Arguments.of (
                    "(1 + 2 * P1) * exp(sq(X1) * 5)", listOf(
                        OpenBracketToken,
                        NumberToken(1),
                        BinOperandToken("+"),
                        NumberToken(2),
                        BinOperandToken("*"),
                        CellReferenceToken("P1"),
                        CloseBracketToken,
                        BinOperandToken("*"),
                        FunctionToken("exp"),
                        OpenBracketToken,
                        FunctionToken("sq"),
                        OpenBracketToken,
                        CellReferenceToken("X1"),
                        CloseBracketToken,
                        BinOperandToken("*"),
                        NumberToken(5),
                        CloseBracketToken
                    )
                )
            )
        }
    }


    @Test
    fun expectTokenizerExceptionsTests() {
        val input1 = "A2?A3"
        org.junit.jupiter.api.assertThrows<TokenizerException> { Tokenizer(input1).tokenize() }
        val input2 = "pow(  %   )"
        org.junit.jupiter.api.assertThrows<TokenizerException> { Tokenizer(input2).tokenize() }
        val input3 = "$"
        org.junit.jupiter.api.assertThrows<TokenizerException> { Tokenizer(input3).tokenize() }
        val input4 = "Func$"
        org.junit.jupiter.api.assertThrows<TokenizerException> { Tokenizer(input4).tokenize() }
    }
}