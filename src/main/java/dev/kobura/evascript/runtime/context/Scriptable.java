package dev.kobura.evascript.runtime.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
        ElementType.TYPE,           // classes, interfaces, enums
        ElementType.FIELD,          // fields
        ElementType.METHOD,         // methods
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Scriptable {

    boolean constant() default true;
    String[] permissions() default {};
    String type() default "";
    String defaultName() default "";

}
