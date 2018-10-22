package com.kuehne_nagel.model.ranking;

public class RankingScore implements Ranking{

    private final int score;
    private final String contestant;
    private final int rank;

    public RankingScore(int score, String contestant, int rank) {
        this.score = score;
        this.contestant = contestant;
        this.rank = rank;
    }

    public String getContestant() {
        return contestant;
    }

    public int getRank() {
        return rank;
    }

    public int getScore() {
        return score;
    }
}
