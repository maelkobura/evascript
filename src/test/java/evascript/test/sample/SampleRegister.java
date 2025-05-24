package evascript.test.sample;

import dev.kobura.evascript.runtime.context.registerable.Register;

public class SampleRegister implements Register {


    @Override
    public Object require(String name) {
        if(name.equals("helloworld")) {
            return new SampleObject();
        }
        return null;
    }
}
