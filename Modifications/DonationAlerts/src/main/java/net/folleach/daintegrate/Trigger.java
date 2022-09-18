package net.folleach.daintegrate;

import net.folleach.daintegrate.sensitives.ISensitive;

import java.util.ArrayList;
import java.util.Iterator;

public class Trigger {
    private final ArrayList<ISensitive> triggers;

    public Trigger(Iterator<ISensitive> triggers) {
        this.triggers = new ArrayList<>();
        triggers.forEachRemaining(this.triggers::add);
    }
}
