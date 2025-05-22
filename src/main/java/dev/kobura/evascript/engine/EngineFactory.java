package dev.kobura.evascript.engine;

public class EngineFactory {

    public static RootedEngine.RootedEngineBuilder createRootedEngine() {
        return new RootedEngine.RootedEngineBuilder();
    }

    public static ShellEngine.ShellEngineBuilder createShellEngine() {
        return new ShellEngine.ShellEngineBuilder();
    }

}
