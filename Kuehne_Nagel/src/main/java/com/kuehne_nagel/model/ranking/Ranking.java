package com.kuehne_nagel.model.ranking;

import static java.lang.String.valueOf;

public interface Ranking {

    String getContestant();

    int getRank();

    int getScore();

    default String getRankAsString() {
        return valueOf(getRank());
    }

    default Ranking with(Ranking rankingScore) {
        return new CompositeRankingScore(this, rankingScore);
    }
}
