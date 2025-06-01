package dev.kobura.evascript.globals;

import dev.kobura.evascript.runtime.context.Globals;
import dev.kobura.evascript.runtime.context.Scriptable;

import java.util.concurrent.ThreadLocalRandom;

@Globals
@Scriptable(defaultName = "random")
public class Random {

    @Scriptable
    public int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    @Scriptable
    public double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    @Scriptable
    public boolean nextBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
