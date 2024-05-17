import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ParserTest {

    @ParameterizedTest
    @MethodSource("testCasesForExceptions")
    fun shouldThrowParserException(input: String) {
        org.junit.jupiter.api.assertThrows<ParserException> {
            Parser(Tokenizer(input).tokenize()).expressionRead()
        }
    }

    @ParameterizedTest
    @MethodSource("testCasesNoExceptions")
    fun shouldNotThrowParserException(input: String) {
        org.junit.jupiter.api.assertDoesNotThrow {
            Parser(Tokenizer(input).tokenize()).expressionRead()
        }
    }


    companion object {
        @JvmStatic
        fun testCasesForExceptions(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("*1"),
                Arguments.of("1 + "),
                Arguments.of("(1 + )"),
                Arguments.of("((((1 + 2)"),
                Arguments.of(""),
                Arguments.of(")"),
                Arguments.of("())"),
                Arguments.of("1 + ()"),
                Arguments.of("Func(())"),
                Arguments.of("Func(1, 2 + 3,)"),
                Arguments.of("Func)"),
                Arguments.of("--3"),
                Arguments.of("Func(1,2,3) + Func((1), (1 + )))"),

                Arguments.of("(1 + 2")
            )
        }
        @JvmStatic
        fun testCasesNoExceptions(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("1"),
                Arguments.of("1 + (A1)"),

                Arguments.of("1 + (A1 + 3)"),
                Arguments.of("1 + (A1 + 3) + func((1 + 2), 3)"),
                Arguments.of("(1 + (A1 + 3) + func((1 + 2), 3)) * (1 - 45)"),
                Arguments.of("(1 + (A1 + 3) + func(sq(1 + 2), 3)) * (1 - 45)"),
                Arguments.of("(1 + (A1 + 3) + func(sq(1 + 2), 3)) * (1 - 45)"),
                Arguments.of("((((42))))"),
                Arguments.of("((((0 -- func(1,2,3,4,5)))))"),
                Arguments.of("pow(2 - 3)"),
                Arguments.of("pow(2, - 3)"),

                Arguments.of("pow(f(f(f(f(42)))), - 3)"),

            )
        }
    }
}