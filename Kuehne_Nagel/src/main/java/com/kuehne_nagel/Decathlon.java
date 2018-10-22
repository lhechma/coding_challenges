package com.kuehne_nagel;


import com.kuehne_nagel.model.Contestant;
import com.kuehne_nagel.model.Output;
import com.kuehne_nagel.model.ranking.RankingByScore;
import com.kuehne_nagel.model.events.Event;
import com.kuehne_nagel.model.events.EventFactory;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toMap;

public class Decathlon {

    private final Map<String, Contestant> contestants = new HashMap<>();
    private final EnumSet<Event> events = EnumSet.noneOf(Event.class);
    private final EventFactory eventFactory;

    public Decathlon(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }


    public Decathlon withContestant(Contestant contestant) {
        if (contestants.containsKey(contestant.getName()))
            throw new IllegalArgumentException("A contestant with the same name already exists, try an alias instead");
        if (isNull(contestant))
            throw new IllegalArgumentException("Invalid contestant");
        contestants.put(contestant.getName(), contestant);
        return this;
    }

    public Decathlon withEvent(Event event) {
        events.add(event);
        return this;
    }

    public Decathlon withAllEvents() {
        Arrays.stream(Event.values()).forEach(events::add);
        return this;
    }

    public void getRankings(Output output) {
        Map<String, Integer> scoreByContestant = contestants.values().stream().collect(
                toMap(Contestant::getName, contestant -> events.stream().mapToInt(event -> event.eventVisitor(eventFactory).visit(contestant)).sum()));
        output.write(RankingByScore.descending(scoreByContestant));
    }


}
