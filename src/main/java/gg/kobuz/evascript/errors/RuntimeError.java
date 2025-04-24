package gg.kobuz.evascript.errors;

public class RuntimeError extends Exception {
    public RuntimeError(String message) {
        super(message);
    }

    public RuntimeError(String message, Throwable cause) {
        super(message, cause);
    }
}
