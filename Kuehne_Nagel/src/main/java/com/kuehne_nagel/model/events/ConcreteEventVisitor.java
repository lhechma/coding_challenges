package com.kuehne_nagel.model.events;

import com.kuehne_nagel.model.Contestant;
import com.kuehne_nagel.model.scoring.ScoringStrategy;

import java.util.function.Function;

class ConcreteEventVisitor implements EventVisitor {

    private final Function<Contestant, Double> valueProvider;
    private ScoringStrategy scoringStrategy;


    public ConcreteEventVisitor(Function<Contestant, Double> valueProvider, ScoringStrategy scoringStrategy) {
        this.valueProvider = valueProvider;
        this.scoringStrategy = scoringStrategy;
    }


    @Override
    public int visit(Contestant contestant) {
        try {
            return scoringStrategy.score(valueProvider.apply(contestant));
        } catch (Exception e) {
            throw new IllegalArgumentException(" The measurements are out of bounds, please ensure that you provide measurements are in seconds for running, in the format (mm:ss:SS) for 1500m, metres for throwing and jumping)");
        }
    }


}
