package dev.kobura.evascript.errors;

public class LoadingBuildinException extends Exception {

    public LoadingBuildinException() {
        super();
    }

    public LoadingBuildinException(String message) {
        super(message);
    }

    public LoadingBuildinException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadingBuildinException(Throwable cause) {
        super(cause);
    }
}
