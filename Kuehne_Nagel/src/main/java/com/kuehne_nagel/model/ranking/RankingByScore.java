package com.kuehne_nagel.model.ranking;

import java.util.*;
import java.util.function.Supplier;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public class RankingByScore {


    private final List<Map.Entry<String, Integer>> orderedByScore;

    private RankingByScore(List<Map.Entry<String, Integer>> orderedByScore) {
        this.orderedByScore = orderedByScore;
    }

    public static List<Ranking> descending(Map<String, Integer> scoreByContestant) {
        Supplier<Integer> rankSupplier = new Supplier<Integer>() {
            int rank = 0;

            @Override
            public Integer get() {
                return ++rank;
            }
        };
        List<Ranking> scorersInDescendingOrder = new ArrayList<>(scoreByContestant.entrySet()).stream().
                sorted(Comparator.<Map.Entry<String, Integer>, Integer>comparing(entry -> entry.getValue()).reversed()).
                map(entry -> new RankingScore(entry.getValue(), entry.getKey(), rankSupplier.get())).
                collect(toList());

        Stack<Ranking> sharedRanks = scorersInDescendingOrder.stream().collect(Stack<Ranking>::new, (stack, rankingScore) -> {
                    if (!stack.empty() && stack.peek().getScore() == rankingScore.getScore()) {
                        stack.push(rankingScore.with(stack.pop()));
                    } else {
                        stack.push(rankingScore);
                    }
                }
                , (left, right) -> left.addAll(right));

        return unmodifiableList(sharedRanks);
    }

}
