package evascript.test.sample;

import dev.kobura.evascript.runtime.context.ContextData;
import dev.kobura.evascript.runtime.context.Scriptable;

@Scriptable(defaultName = "test")
public class SampleObject {

    @Scriptable
    public String hello() {
        return "Hello World!";
    }

    @Scriptable
    public String what(@ContextData String text) {
        return text;
    }

    @Scriptable(permissions = {"hello.world"})
    public String who(@ContextData SampleUser user) {
        return user.getName();
    }

    @Scriptable
    public String where(String first, String second, String third) {

        return (first != null ? first : "")  + (second != null ? second : "") + (third != null ? third : "");
    }

}
