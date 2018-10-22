package com.kuehne_nagel.model;

import com.kuehne_nagel.model.ranking.Ranking;

import java.util.List;

public interface Output {
    void write(List<Ranking> contestantByPosition);
}
