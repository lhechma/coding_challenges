package com.kuehne_nagel.model.scoring;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class AbstractScoringStrategy implements ScoringStrategy {

    private ScoringProfile scoringProfile;

    public AbstractScoringStrategy(ScoringProfile profile){
        scoringProfile = profile;
    }

    public final int score(double measurement) {
        if(measurement==0)
            return 0;
        return BigDecimal.valueOf(
                (multiply(scoringProfile.getAFactor(), raiseToThePower(difference(measurement, scoringProfile.getBFactor()), scoringProfile.getCFactor()))
                /100.0)).setScale(1, RoundingMode.UP).intValue()*100;
    }

    private double raiseToThePower(double base, double power) {
        return Math.pow(base, power);
    }

    private double multiply(double aFactor, double raisedToThePower) {
        return aFactor * raisedToThePower;
    }

    protected abstract double difference(double performance, double bFactor);
}
