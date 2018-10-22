package com.kuehne_nagel.model.scoring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Test Data from wikipedia
 * Event	       1,000 pts	900 pts	 800 pts	700 pts	   Unit
 * Long jump	    7.76	    7.36	 6.94	     6.51	  Metres
 * Shot put	        18.4	    16.79	 15.16	     13.53	  Metres
 * High jump	    2.20	    2.10	 1.99	     1.88	  Metres
 * Discus throw	    56.17	    51.4	 46.59	     41.72	  Metres
 * Pole vault	    5.28	    4.96	 4.63	     4.29	  Metres
 * Javelin throw	77.19	    70.67	 64.09	     57.45	  Metres
 */
public class FieldEventScoringTest {


    @DisplayName("Long Jump Scoring Test")
    @ParameterizedTest(name = " performance: {0} {1} must yield a score should of {2} points")
    @MethodSource("longJumpData")
    public void testLongJumpScoring(double meters, String unit, int points) {
        assertThat(points).isEqualTo(new FieldEventScoringStrategy(ScoringProfile.forLongJump()).score(meters*100));
    }

    @DisplayName("Shot put Scoring Test")
    @ParameterizedTest(name = " performance: {0} {1} must yield a score should of {2} points")
    @MethodSource("shotPutData")
    public void testShotPutScoring(double meters, String unit, int points) {
        assertThat(points).isEqualTo(new FieldEventScoringStrategy(ScoringProfile.forShotPut()).score(meters));
    }

    @DisplayName("High jump Scoring Test")
    @ParameterizedTest(name = " performance: {0} {1} must yield a score should of {2} points")
    @MethodSource("highJumpData")
    public void test1HighJumpScoring(double meters, String unit, int points) {
        assertThat(points).isEqualTo(new FieldEventScoringStrategy(ScoringProfile.forHighJump()).score(meters*100));
    }

    @DisplayName("Discus throw Scoring Test")
    @ParameterizedTest(name = " performance: {0} {1} must yield a score should of {2} points")
    @MethodSource("discusThrowData")
    public void test1DiscusThrowScoring(double meters, String unit, int points) {
        assertThat(points).isEqualTo(new FieldEventScoringStrategy(ScoringProfile.forDiscusThrow()).score(meters));
    }

    @DisplayName("Pole vault Scoring Test")
    @ParameterizedTest(name = " performance: {0} {1} must yield a score should of {2} points")
    @MethodSource("poleVaultData")
    public void test1PoleVaultScoring(double meters, String unit, int points) {
        assertThat(points).isEqualTo(new FieldEventScoringStrategy(ScoringProfile.forPoleVault()).score(meters*100));
    }

    @DisplayName("Javelin throw Scoring Test")
    @ParameterizedTest(name = " performance: {0} {1} must yield a score should of {2} points")
    @MethodSource("javelinThrowData")
    public void test1JavelinThrowScoring(double meters, String unit, int points) {
        assertThat(points).isEqualTo(new FieldEventScoringStrategy(ScoringProfile.forJavelinThrow()).score(meters));
    }

    private static Stream<Arguments> longJumpData() {
        return Stream.of(
                Arguments.of("7.76", "metres", "1000"),
                Arguments.of("7.36", "metres", "900"),
                Arguments.of("6.94", "metres", "800"),
                Arguments.of("6.51", "metres", "700")
        );
    }

    private static Stream<Arguments> shotPutData() {
        return Stream.of(
                Arguments.of("18.4", "metres", "1000"),
                Arguments.of("16.79", "metres", "900"),
                Arguments.of("15.16", "metres", "800"),
                Arguments.of("13.53", "metres", "700")
        );
    }

    private static Stream<Arguments> highJumpData() {
        return Stream.of(
                Arguments.of("2.20", "metres", "1000"),
                Arguments.of("2.10", "metres", "900"),
                Arguments.of("1.99", "metres", "800"),
                Arguments.of("1.88", "metres", "700")
        );
    }

    private static Stream<Arguments> discusThrowData() {
        return Stream.of(
                Arguments.of("56.17", "metres", "1000"),
                Arguments.of("51.4", "metres", "900"),
                Arguments.of("46.59", "metres", "800"),
                Arguments.of("41.72", "metres", "700")
        );
    }

    private static Stream<Arguments> poleVaultData() {
        return Stream.of(
                Arguments.of("5.28", "metres", "1000"),
                Arguments.of("4.96", "metres", "900"),
                Arguments.of("4.63", "metres", "800"),
                Arguments.of("4.29", "metres", "700")
        );
    }

    private static Stream<Arguments> javelinThrowData() {
        return Stream.of(
                Arguments.of("77.19", "metres", "1000"),
                Arguments.of("70.67", "metres", "900"),
                Arguments.of("64.09", "metres", "800"),
                Arguments.of("57.45", "metres", "700")
        );
    }



}
