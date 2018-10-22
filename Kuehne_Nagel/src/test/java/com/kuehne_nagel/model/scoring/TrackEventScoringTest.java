package com.kuehne_nagel.model.scoring;

import com.kuehne_nagel.parser.DurationParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test Data from wikipedia
 * Event	       1,000 pts	900 pts	 800 pts	700 pts	   Unit
 * 100 m	        10.395	    10.827	 11.278	     11.756	  Seconds
 * 400 m	        46.17	    48.19	 50.32	     52.58	  Seconds
 * 110 m hurdles	13.8	    14.59	 15.419	     16.29	  Seconds
 * 1500 m	        3:53.79	    4:07.42	 4:21.77	4:36.96	  Minutes:Seconds
 */
public class TrackEventScoringTest {


    private static Stream<Arguments> _100mData() {
        return Stream.of(
                Arguments.of("10.395", "seconds", "1000"),
                Arguments.of("10.827", "seconds", "900"),
                Arguments.of("11.287", "seconds", "800"),
                Arguments.of("11.756", "seconds", "700")
        );
    }

    private static Stream<Arguments> _400mData() {
        return Stream.of(
                Arguments.of("46.17", "seconds", "1000"),
                Arguments.of("48.19", "seconds", "900"),
                Arguments.of("50.32", "seconds", "800"),
                Arguments.of("52.58", "seconds", "700")
        );
    }

    private static Stream<Arguments> _100mHurdlesData() {
        return Stream.of(
                Arguments.of("13.8", "seconds", "1000"),
                Arguments.of("14.59", "seconds", "900"),
                Arguments.of("15.419", "seconds", "800"),
                Arguments.of("16.29", "seconds", "700")
        );
    }

    private static Stream<Arguments> _1500mData() {
        return Stream.of(
                Arguments.of("3:53.79", "seconds", "1000"),
                Arguments.of("4:07.42", "seconds", "900"),
                Arguments.of("4:21.77", "seconds", "800"),
                Arguments.of("4:36.96", "seconds", "700")
        );
    }


    @DisplayName("100m Event Scoring Test")
    @ParameterizedTest(name = " performance: {0} {1} must yield a score should of {2} points")
    @MethodSource("_100mData")
    public void test100mEventScoring(double performance, String unit, int points) {
        assertThat(new TrackEventScoringStrategy(ScoringProfile.for100m()).score(performance)).isEqualTo(points);
    }

    @DisplayName("400m Event Scoring Test")
    @ParameterizedTest(name = " performance: {0} {1} must yield a score should of {2} points")
    @MethodSource("_400mData")
    public void test400mEventScoring(double performance, String unit, int points) {
        assertThat(new TrackEventScoringStrategy(ScoringProfile.for400m()).score(performance)).isEqualTo(points);
    }

    @DisplayName("110 m hurdles Event Scoring Test")
    @ParameterizedTest(name = " performance: {0} {1} must yield a score should of {2} points")
    @MethodSource("_100mHurdlesData")
    public void test100mHurdlesEventScoring(double performance, String unit, int points) {
        assertThat(new TrackEventScoringStrategy(ScoringProfile.for110mHurdles()).score(performance)).isEqualTo(points);
    }


    @DisplayName("1500 m Event Scoring Test")
    @ParameterizedTest(name = " performance: {0} {1} must yield a score should of {2} points")
    @MethodSource("_1500mData")
    public void test1500mEventScoring(String timing, String unit, int points) {
        DurationParser parser = new DurationParser();
        assertThat(points).isEqualTo(new TrackEventScoringStrategy(ScoringProfile.for1500m()).score(parser.apply(timing)/1000.0));
    }




}

