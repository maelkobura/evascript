package evascript.test.sample;

import dev.kobura.evascript.runtime.context.registerable.Register;

public class SampleRegister implements Register {


    @Override
    public Object require(String name) {
        return switch (name) {
            case "helloworld" -> new SampleObject();
            default -> null;
        };
    }
}
