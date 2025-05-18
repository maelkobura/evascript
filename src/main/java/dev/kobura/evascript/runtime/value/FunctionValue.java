package dev.kobura.evascript.runtime.value;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.parsing.ast.statement.BlockStatement;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.ContextIdentity;
import dev.kobura.evascript.security.PermissiveUser;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
public class FunctionValue extends Value {

    private BlockStatement body;
    private boolean async;
    private List<String> arguments;

    @Override
    public Object unwrap() {
        return null;
    }

    @Override
    public ValueType getType() {
        return ValueType.FUNCTION;
    }

    @Override
    public boolean isEqual(Value other) {
        return false;
    }

    @Override
    public Value execute(Execution execution, PermissiveUser user, Value args) throws RuntimeError {
        //TODO Async

        Execution funcexe = new Execution(execution.getEngine(), execution, user);

        if(args.getType() == ValueType.ARRAY) {
            ArrayValue av = (ArrayValue) args;
            for(int i = 0; i < av.getValues().size(); i++) {
                funcexe.let(new ContextIdentity(arguments.get(i), false, Instant.now(), 0), av.getValues().get(i));
            }
        }else if(args.getType() == ValueType.OBJECT) {
            ObjectValue ov = (ObjectValue) args;
            for(String arg : arguments) {
                funcexe.let(new ContextIdentity(arg, false, Instant.now(), 0), ov.values.getOrDefault(arg, NullValue.INSTANCE));
            }
        }

        return funcexe.runAsFunction(body);
    }
}
