package com.kuehne_nagel.model.events;

import com.kuehne_nagel.model.Contestant;

import java.util.Collections;
import java.util.List;

public class CompositeEventVisitor implements EventVisitor {

    private List<EventVisitor> events = Collections.emptyList();

    public CompositeEventVisitor(List<EventVisitor> events) {
        this.events = events;
    }

    @Override
    public int visit(Contestant contestant) {
        return events.stream().mapToInt(event -> event.visit(contestant)).sum();
    }
}
