package com.kuehne_nagel.model.scoring;

class TrackEventScoringStrategy extends AbstractScoringStrategy {

    public TrackEventScoringStrategy(ScoringProfile profile) {
        super(profile);
    }

    @Override
    protected double difference(double performance, double bFactor) {
        return bFactor - performance;
    }
}
