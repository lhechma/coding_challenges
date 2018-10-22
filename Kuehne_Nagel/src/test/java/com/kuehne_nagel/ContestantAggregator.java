package com.kuehne_nagel;

import com.kuehne_nagel.model.Contestant;
import com.kuehne_nagel.model.NonNegativePerformanceMetricConstraint;
import com.kuehne_nagel.parser.DurationParser;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

public class ContestantAggregator implements ArgumentsAggregator {

    private static DurationParser parser = new DurationParser();

    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
        String name = accessor.get(0, String.class);
        double _100m = accessor.get(1, Float.class);
        double long_jump_height = accessor.get(2, Float.class);
        double shot_put = accessor.get(3, Float.class);
        double high_jump = accessor.get(4, Float.class);
        double _400m = accessor.get(5, Float.class);
        double _100m_hurdles = accessor.get(6, Float.class);
        double discus_throw = accessor.get(7, Float.class);
        double pole_vault = accessor.get(8, Float.class);
        double javelin_throw = accessor.get(9, Float.class);
        long duration = parser.apply(accessor.get(10, String.class));

        return new Contestant.ContestantBuilder().withName(name).with100mPerformance(_100m).withLongJumpPerformance(long_jump_height)
                .withShotPutPerforamnce(shot_put).withHighJumpPerformance(high_jump).with400mPerformance(_400m).with110mHurdlesPerformance(_100m_hurdles)
                .withDiscusThrowPerformance(discus_throw).withPoleVaultPerformance(pole_vault).withJavelinThrowPerformance(javelin_throw).with1500mPerformance(duration)
                .withConstraint(new NonNegativePerformanceMetricConstraint()).build();
    }
}
