package dev.kobura.evascript.parsing.ast;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;

public abstract class ASTNode {

    public int line = 0;

    public abstract Value accept(NodeVisitor visitor, Execution execution, Value...values) throws RuntimeError;

}
