package dev.kobura.evascript.runtime.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
        ElementType.TYPE,           // classes, interfaces, enums
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Globals {
}
