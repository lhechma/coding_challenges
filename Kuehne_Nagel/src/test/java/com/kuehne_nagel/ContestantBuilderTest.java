package com.kuehne_nagel;


import com.kuehne_nagel.model.Contestant;
import com.kuehne_nagel.model.events.EventFactory;
import com.kuehne_nagel.model.scoring.ScoringStrategyFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContestantBuilderTest {


    @BeforeAll
    public static void init() {
        ScoringStrategyFactory scoringStrategyFactory = new ScoringStrategyFactory();
        EventFactory eventFactory = new EventFactory(scoringStrategyFactory);
    }

    @DisplayName("Should Load The contestants details")
    @ParameterizedTest(name = "Contenstant={0}: " +
            "100 Meters={1} seconds, Long jump={2} meters, Shot put={3} meters, " +
            "High jump={4} meters, 400 Meters={5} seconds, 100 Meters Hurdles={6} seconds" +
            "Discus throw={7}, Pole vault={8}, Javelin throw ={9}, 1500 Meters={10}")
    @CsvFileSource(resources = "/results.csv", delimiter = ';')
    void testBuildingContestants(@AggregateWith(ContestantAggregator.class) Contestant contestant) {
        assertNotNull(contestant);
        assertThat(contestant).hasNoNullFieldsOrProperties();
        assertThat(contestant).extracting(Contestant::get100m, Contestant::get110mHurdles, Contestant::get400m,
                Contestant::getDiscusThrow, Contestant::getHighJump, Contestant::getJavelinThrow,
                Contestant::getLongJumpHeight, Contestant::getPoleVault, Contestant::getShotPut, Contestant::get1500m).noneMatch(x -> (Double) x < 0);
    }


}
