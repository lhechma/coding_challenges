package com.kuehne_nagel.model.scoring;

public class ScoringStrategyFactory {

    public ScoringStrategy createFieldEventScoringStrategy(ScoringProfile scoringProfile) {
        return new FieldEventScoringStrategy(scoringProfile);
    }

    public ScoringStrategy createTrackEventScoringStrategy(ScoringProfile scoringProfile) {
        return new TrackEventScoringStrategy(scoringProfile);
    }
}
