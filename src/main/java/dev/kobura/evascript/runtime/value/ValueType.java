package dev.kobura.evascript.runtime.value;

public enum ValueType {
    NUMBER("number"),
    STRING("string"),
    BOOLEAN("boolean"),
    OBJECT("object"),
    ARRAY("array"),
    FUNCTION("function"),
    NULL("null"),
    UNDEFINED("undefined");

    private final String name;

    ValueType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {return name;}
}
