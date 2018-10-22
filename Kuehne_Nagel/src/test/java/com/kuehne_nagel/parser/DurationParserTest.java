package com.kuehne_nagel.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("duration parser test")
public class DurationParserTest {

    private DurationParser parser = new DurationParser();

    private static Stream<Arguments> invalidInputProvider() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("xx"),
                Arguments.of("00"),
                Arguments.of("time is money"),
                Arguments.of("00.61.00"),
                Arguments.of(".59.00"),
                Arguments.of(".."),
                Arguments.of("2.02.02x"),
                Arguments.of("A2.59.55"),
                Arguments.of("-10.11.00"),
                Arguments.of("66.10.00")
        );
    }

    @DisplayName("parsing input not matching mm.ss.SS where 0<=mm<=59, 0<=ss<=59, 0<=SS<=99")
    @ParameterizedTest(name = "testing for input \"{0}\"")
    @MethodSource("invalidInputProvider")
    public void testInvalidInput(final String malformedInput) {
        assertThrows(IllegalArgumentException.class, () -> parser.apply(malformedInput), "Invalid Input should have failed");
    }

    @Test
    @DisplayName("a 00.00.00 duration is possible if the participant decides not to join an event")
    public void test0DurationInput() {
        assertEquals(0L, parser.apply("00.00.00").longValue());
    }


    @Test
    @DisplayName("Duration minutes separators : and . accepted")
    public void testMinutesRepresentation() {
        assertEquals(parser.apply("1.00.00").longValue(),parser.apply("1:00.00").longValue());
    }
}
