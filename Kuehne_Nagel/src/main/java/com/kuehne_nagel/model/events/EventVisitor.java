package com.kuehne_nagel.model.events;

import com.kuehne_nagel.model.Contestant;

public interface EventVisitor {
    int visit(Contestant contestant);
}
