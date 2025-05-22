package evascript.test.sample;

import dev.kobura.evascript.runtime.context.Scriptable;

@Scriptable
public class SampleObject {

    @Scriptable
    public String hello() {
        return "Hello World!";
    }

}
