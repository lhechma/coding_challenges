package com.kuehne_nagel.model.ranking;

class CompositeRankingScore implements Ranking {

    Ranking ranking1;
    Ranking ranking2;

    public CompositeRankingScore(Ranking ranking1, Ranking ranking2) {
        assert ranking1.getScore() == ranking2.getScore();
        this.ranking1 = ranking1;
        this.ranking2 = ranking2;
    }

    @Override
    public String getContestant() {
        //Could be made better
        return ranking1.getContestant().compareTo(ranking2.getContestant()) < 0 ?
                ranking1.getContestant()+"-"+ranking2.getContestant() : ranking2.getContestant()+"-"+ranking1.getContestant();
    }

    @Override
    public int getRank() {
        return Math.min(ranking1.getRank(), ranking2.getRank());
    }

    @Override
    public int getScore() {
        return ranking1.getScore();
    }


    @Override
    public String getRankAsString() {
        //Could be made better
        int min = getRank();
        return min == ranking1.getRank() ? min + "-" + ranking2.getRankAsString() : min + "-" + ranking1.getRankAsString();
    }
}
