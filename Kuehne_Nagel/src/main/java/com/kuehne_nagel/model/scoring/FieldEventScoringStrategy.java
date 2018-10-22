package com.kuehne_nagel.model.scoring;

class FieldEventScoringStrategy extends AbstractScoringStrategy {

    public FieldEventScoringStrategy(ScoringProfile profile) {
        super(profile);
    }

    @Override
    protected double difference(double performance, double bFactor) {
        return performance - bFactor;
    }
}
