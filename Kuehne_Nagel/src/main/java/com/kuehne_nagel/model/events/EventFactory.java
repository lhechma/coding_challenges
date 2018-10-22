package com.kuehne_nagel.model.events;

import com.kuehne_nagel.model.Contestant;
import com.kuehne_nagel.model.scoring.ScoringProfile;
import com.kuehne_nagel.model.scoring.ScoringStrategyFactory;

/**
 * A       B        C
 * 100 m	     25.4347	18	    1.81
 * Long jump	 0.14354	220	    1.4
 * Shot put	     51.39	    1.5	    1.05
 * High jump	 0.8465	    75	    1.42
 * 400 m	     1.53775	82	    1.81
 * 110 m hurdles 5.74352	28.5	1.92
 * Discus throw	 12.91	    4	    1.1
 * Pole vault	 0.2797	    100	    1.35
 * Javelin throw 10.14	    7	    1.08
 * 1500 m	     0.03768	480	    1.85
 */
public class EventFactory {

    private final ScoringStrategyFactory scoringStrategyFactory;

    public EventFactory(ScoringStrategyFactory scoringStrategyFactory) {
        this.scoringStrategyFactory = scoringStrategyFactory;
    }

    public EventVisitor create100mEvent() {
        return new ConcreteEventVisitor(Contestant::get100m, scoringStrategyFactory.createTrackEventScoringStrategy(ScoringProfile.for100m()));
    }

    public EventVisitor createLongJumpEvent() {
        return new ConcreteEventVisitor(Contestant::getLongJumpHeight, scoringStrategyFactory.createFieldEventScoringStrategy(ScoringProfile.forLongJump()));
    }

    public EventVisitor createJavelinThrowEvent() {
        return new ConcreteEventVisitor(Contestant::getJavelinThrow, scoringStrategyFactory.createFieldEventScoringStrategy(ScoringProfile.forJavelinThrow()));
    }

    public EventVisitor create1500mEvent() {
        return new ConcreteEventVisitor(Contestant::get1500m, scoringStrategyFactory.createTrackEventScoringStrategy(ScoringProfile.for1500m()));
    }

    public EventVisitor createPoleVaultEvent() {
        return new ConcreteEventVisitor(Contestant::getPoleVault, scoringStrategyFactory.createFieldEventScoringStrategy(ScoringProfile.forPoleVault()));
    }

    public EventVisitor createShotPotEvent() {
        return new ConcreteEventVisitor(Contestant::getShotPut, scoringStrategyFactory.createFieldEventScoringStrategy(ScoringProfile.forShotPut()));
    }

    public EventVisitor create400mEvent() {
        return new ConcreteEventVisitor(Contestant::get400m, scoringStrategyFactory.createTrackEventScoringStrategy(ScoringProfile.for400m()));
    }

    public EventVisitor create110mHurdlesEvent() {
        return new ConcreteEventVisitor(Contestant::get110mHurdles, scoringStrategyFactory.createTrackEventScoringStrategy(ScoringProfile.for110mHurdles()));

    }

    public EventVisitor createDiscusThrowEvent() {
        return new ConcreteEventVisitor(Contestant::getDiscusThrow, scoringStrategyFactory.createFieldEventScoringStrategy(ScoringProfile.forDiscusThrow()));
    }
}
