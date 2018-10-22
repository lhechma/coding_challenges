package n26.web.exception;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.type.PlaceholderForType;
import com.n26.exceptions.TransactionTooLateException;
import com.n26.web.exception.ExceptionResolver;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

public class ExceptionResolverTest {

    private static JsonParserDelegate parser;

    ExceptionResolver exceptionResolver = new ExceptionResolver();

    @BeforeAll
    public static void init() {
        parser = Mockito.mock(JsonParserDelegate.class);
    }

    private static Stream<Arguments> mappingsProvider() {
        //TODO complete
        return Stream.of(
                Arguments.of(MismatchedInputException.from(parser, new PlaceholderForType(0), ""),422),
                Arguments.of(new TransactionTooLateException(""),204)
        );
    }

    @ParameterizedTest(name = "Exception {0} yields {1}")
    @MethodSource("mappingsProvider")
    @DisplayName("exception mappings")
    void testMapping( Throwable throwable, int statusCode) throws Exception {
        Assert.assertEquals(exceptionResolver.from(throwable), HttpStatus.valueOf(statusCode));
    }
}
