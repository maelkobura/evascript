package dev.kobura.evascript.runtime.interpreter;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.lexer.token.ArithmeticToken;
import dev.kobura.evascript.lexer.token.SyntaxToken;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.parsing.ast.ASTStatement;
import dev.kobura.evascript.parsing.ast.FunctionDeclaration;
import dev.kobura.evascript.parsing.ast.expression.LambdaExpression;
import dev.kobura.evascript.parsing.ast.expression.LiteralExpression;
import dev.kobura.evascript.parsing.ast.expression.context.AssignementExpression;
import dev.kobura.evascript.parsing.ast.expression.context.ContextAccessExpression;
import dev.kobura.evascript.parsing.ast.expression.context.MethodCallExpression;
import dev.kobura.evascript.parsing.ast.expression.context.ReturnStatement;
import dev.kobura.evascript.parsing.ast.expression.context.variable.VariableDeclaration;
import dev.kobura.evascript.parsing.ast.expression.context.variable.args.SequencedArgumentExpression;
import dev.kobura.evascript.parsing.ast.expression.context.variable.args.StructuredArgumentExpression;
import dev.kobura.evascript.parsing.ast.expression.logical.BinaryExpression;
import dev.kobura.evascript.parsing.ast.expression.logical.ComparisonExpression;
import dev.kobura.evascript.parsing.ast.expression.logical.UnaryExpression;
import dev.kobura.evascript.parsing.ast.statement.*;
import dev.kobura.evascript.parsing.ast.statement.loop.BreakStatement;
import dev.kobura.evascript.parsing.ast.statement.loop.ContinueStatement;
import dev.kobura.evascript.runtime.value.*;
import dev.kobura.evascript.parsing.ast.*;
import dev.kobura.evascript.parsing.ast.expression.*;
import dev.kobura.evascript.parsing.ast.expression.context.*;
import dev.kobura.evascript.parsing.ast.expression.context.variable.args.*;
import dev.kobura.evascript.parsing.ast.expression.context.variable.*;
import dev.kobura.evascript.parsing.ast.expression.logical.*;
import dev.kobura.evascript.parsing.ast.expression.tokenable.ThisExpression;
import dev.kobura.evascript.parsing.ast.statement.*;
import dev.kobura.evascript.parsing.ast.statement.loop.*;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.ContextIdentity;
import dev.kobura.evascript.runtime.interpreter.control.BreakSignal;
import dev.kobura.evascript.runtime.interpreter.control.ContinueSignal;
import dev.kobura.evascript.runtime.interpreter.control.ReturnSignal;
import dev.kobura.evascript.runtime.value.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter implements NodeVisitor {

    @Override
    public Value visitAssignementExpression(AssignementExpression node, Execution execution) throws RuntimeError {
        Value value = node.getValue().accept(this, execution);
        if(node.getTarget() != null) {

            if(node.getTarget() instanceof ContextAccessExpression) {
                Value target = ((ContextAccessExpression) node.getTarget()).getTarget().accept(this, execution);
                target.setField(execution, execution.getUser(), ((ContextAccessExpression) node.getTarget()).getIdentifier(), value);
            }

            Value target = node.getTarget().accept(this, execution);
            target.setField(execution, execution.getUser(), "", value);
        }else {
            throw new RuntimeError("Target of assignment is null");
        }

        return null;
    }

    @Override
    public Value visitContextAccessExpression(ContextAccessExpression node, Execution execution) throws RuntimeError {
        if(node.getTarget() == null || node.getTarget() instanceof ThisExpression) {
            return execution.get(node.getIdentifier());
        } else {
            return node.getTarget().accept(this, execution).getField(execution, execution.getUser(), node.getIdentifier());
        }
    }

    @Override
    public Value visitVariableDeclaration(VariableDeclaration node, Execution execution) throws RuntimeError {

        Value value = node.getValue().accept(this, execution);

        switch (node.getMode()) {
            case LET: {
                ContextIdentity id = new ContextIdentity(node.name, false, Instant.now(), 0);
                execution.let(id, value);
                break;
            }
            case VAR: {
                ContextIdentity id = new ContextIdentity(node.name, false, Instant.now(), execution.getEngine().getDefaultExpirationTime());
                execution.var(id, value);
                break;
            }
            case CONST: {
                ContextIdentity id = new ContextIdentity(node.name, false, Instant.now(), execution.getEngine().getDefaultConstExpirationTime());
                execution.var(id, value);
                break;
            }
            default: {
                throw new RuntimeError("Unknown variable mode");
            }
        }
        return value;
    }

    @Override
    public Value visitSequencedArgumentExpression(SequencedArgumentExpression node, Execution execution) throws RuntimeError {
        List<Value> evaluatedArguments = new ArrayList<>();

        for (ASTExpression argument : node.getArguments()) {
            Value evaluatedValue = argument.accept(this, execution);

            evaluatedArguments.add(evaluatedValue);
        }

        return new ArrayValue(evaluatedArguments);
    }

    @Override
    public Value visitStructuredArgumentExpression(StructuredArgumentExpression node, Execution execution) throws RuntimeError {
        // Create a map to store evaluated argument results
        Map<String, Value> evaluatedArguments = new HashMap<>();

        // Iterate through arguments from Map<String, ASTExpression>
        for (Map.Entry<String, ASTExpression> entry : node.getArguments().entrySet()) {
            String key = entry.getKey();
            ASTExpression expression = entry.getValue();

            // Evaluate each expression by calling its accept method
            Value evaluatedValue = expression.accept(this, execution);

            // Add the evaluated result to the map with the corresponding key
            evaluatedArguments.put(key, evaluatedValue);
        }

        // Return an ObjectValue that encapsulates the structured arguments
        return new ObjectValue(evaluatedArguments);
    }

    @Override
    public Value visitBinaryExpression(BinaryExpression node, Execution execution) throws RuntimeError {
        Value leftValue = node.getLeft().accept(this, execution);
        Value rightValue = node.getRight().accept(this, execution);

        switch (node.getOperator()) {
            case PLUS:
                return leftValue.add(rightValue);
            case MINUS:
                return leftValue.subtract(rightValue);
            case MULT:
                return leftValue.multiply(rightValue);
            case DIVIDE:
                return leftValue.divide(rightValue);
            case MODULO:
                return leftValue.modulo(rightValue);
            default:
                throw new RuntimeError("Unsupported binary operator: " + node.getOperator());
        }
    }

    @Override
    public Value visitComparisonExpression(ComparisonExpression node, Execution execution) throws RuntimeError {
        Value leftValue = node.getLeft().accept(this, execution);
        Value rightValue = node.getRight().accept(this, execution);

        switch (node.getOperator()) {
            case EQUAL:
                return new BooleanValue(leftValue.isEqual(rightValue));
            case NOT_EQUAL:
                return new BooleanValue(!leftValue.isEqual(rightValue));
            case GREATER:
                if (leftValue.unwrap() instanceof Number && rightValue.unwrap() instanceof Number) {
                    return new BooleanValue(((Number) leftValue.unwrap()).doubleValue() > ((Number) rightValue.unwrap()).doubleValue());
                }
                break;
            case LESS:
                if (leftValue.unwrap() instanceof Number && rightValue.unwrap() instanceof Number) {
                    return new BooleanValue(((Number) leftValue.unwrap()).doubleValue() < ((Number) rightValue.unwrap()).doubleValue());
                }
                break;
            case GREATER_EQUAL:
                if (leftValue.unwrap() instanceof Number && rightValue.unwrap() instanceof Number) {
                    return new BooleanValue(((Number) leftValue.unwrap()).doubleValue() >= ((Number) rightValue.unwrap()).doubleValue());
                }
                break;
            case LESS_EQUAL:
                if (leftValue.unwrap() instanceof Number && rightValue.unwrap() instanceof Number) {
                    return new BooleanValue(((Number) leftValue.unwrap()).doubleValue() <= ((Number) rightValue.unwrap()).doubleValue());
                }
                break;
            case AND:
                return BooleanValue.isTruthy(leftValue) || BooleanValue.isTruthy(rightValue) ? new BooleanValue(true) : new BooleanValue(false);
            case OR:
                return BooleanValue.isTruthy(leftValue) && BooleanValue.isTruthy(rightValue) ? new BooleanValue(true) : new BooleanValue(false);
            default:
                throw new RuntimeError("Unsupported comparison operator: " + node.getOperator());
        }

        throw new RuntimeError("Comparison is only supported for numeric types or equal operations.");
    }

    @Override
    public Value visitUnaryExpression(UnaryExpression node, Execution execution) throws RuntimeError {
        Value operand = node.getExpression().accept(this, execution);

        if(node.getOperator().type instanceof ArithmeticToken tk) {
            switch (tk) {
                case ADD -> operand.add(new NumberValue(1));
                case MINUS -> operand.subtract(new NumberValue(1));
                default -> throw new RuntimeError("Unsupported unary operator: " + node.getOperator());
            }
            return operand;
        }else if(node.getOperator().type.equals(SyntaxToken.BANG) && operand.getType() == ValueType.BOOLEAN) {
            return new BooleanValue(!(Boolean) operand.unwrap());
        }else {
            throw new RuntimeError("Unsupported unary operator: " + node.getOperator());
        }
    }


    @Override
    public Value visitLambdaExpression(LambdaExpression node, Execution execution) throws RuntimeError {
        List<String> args = node.getExpression().getArgs();
        return new FunctionValue(node.getBody(), node.isAsync(), args);
    }

    @Override
    public Value visitLiteralExpression(LiteralExpression node, Execution execution) {
        return Value.from(node.getValue());
    }


    @Override
    public Value visitMethodCallExpression(MethodCallExpression node, Execution execution) throws RuntimeError {

        if(node.getTarget() == null) {
            return execution.get(node.getMethodName()).execute(execution, execution.getUser(), node.getArguments().accept(this, execution));
        }

        Value target = node.getTarget().accept(this, execution);

        Value args = node.getArguments().accept(this, execution);
        
        return target.execute(execution, execution.getUser(), node.getMethodName(), args);
    }

    @Override
    public Value visitReturnStatement(ReturnStatement node, Execution execution) throws RuntimeError {
        return node.getExpression().accept(this, execution);
    }

    @Override
    public Value visitBlockStatement(BlockStatement node, Execution execution) throws RuntimeError {
        for(ASTStatement statement : node.getStatements()) {
            if(statement instanceof BreakStatement) {
                throw new BreakSignal();
            }else if(statement instanceof ContinueStatement) {
                throw new ContinueSignal();
            }else if(statement instanceof ReturnStatement) {
                throw new ReturnSignal(statement.accept(this, execution));
            }
            statement.accept(this, execution);
        }
        return UndefinedValue.INSTANCE;
    }

    @Override
    public Value visitDoWhileStatement(DoWhileStatement node, Execution execution) throws RuntimeError {
        do {
            try {
                node.accept(this, execution);
            }catch (ContinueSignal signal) {
                continue;
            }catch (BreakSignal signal) {
                break;
            }catch (ReturnSignal signal) {
                throw signal;
            } catch (RuntimeError e) {
                throw e;
            }
        }while (node.getCondition().accept(this, execution).equals(new BooleanValue(true)));
        return UndefinedValue.INSTANCE;
    }

    @Override
    public Value visitForStatement(ForStatement node, Execution execution) throws RuntimeError {
        String varname = node.getExpression().getVarName();
        Value rawIterable = node.getExpression().getIterable().accept(this, execution);
        if(rawIterable.getType() != ValueType.ARRAY) {
            throw new RuntimeError("Iterable must be an array");
        }
        List<Value> iterable = (List<Value>) rawIterable.unwrap();
        for(Value value : iterable) {
            try {
                node.accept(this, execution, rawIterable);
            }catch (ContinueSignal signal) {
                continue;
            }catch (BreakSignal signal) {
                break;
            }catch (ReturnSignal signal) {
                throw signal;
            } catch (RuntimeError e) {
                throw e;
            }
        }

        return UndefinedValue.INSTANCE;
    }

    @Override
    public Value visitWhileStatement(WhileStatement node, Execution execution) throws RuntimeError {
        while (node.getCondition().accept(this, execution).equals(new BooleanValue(true))) {
            try {
                node.getBody().accept(this, execution);
            } catch (BreakSignal signal) {
                break;
            } catch (ContinueSignal signal) {
                continue;
            } catch (ReturnSignal signal) {
                throw signal;
            }
        }
        return UndefinedValue.INSTANCE;
    }

    @Override
    public Value visitExpressionStatement(ExpressionStatement node, Execution execution) throws RuntimeError {
        return node.getExpression().accept(this, execution);
    }

    @Override
    public Value visitIfStatement(IfStatement node, Execution execution) throws RuntimeError {
        Value value = node.getCondition().accept(this, execution);
        if (BooleanValue.isTruthy(value)) {
            node.getBody().accept(this, execution);
        } else if (node.getElseBody() != null) {
            node.getElseBody().accept(this, execution);
        }
        return UndefinedValue.INSTANCE;
    }


    @Override
    public Value visitSwitchStatement(SwitchStatement node, Execution execution) throws RuntimeError {
        Value v = node.getCondition().accept(this, execution);
        for(SwitchCase c : node.getCases()) {
            if(!c.isDefault() && !c.getExpression().accept(this, execution).isEqual(v)) {
                continue;
            }
            for(ASTStatement s : c.getStatements()) {
                try {
                    s.accept(this, execution);
                } catch (BreakSignal signal) {
                    break;
                } catch (ReturnSignal signal) {
                    throw signal;
                } catch (RuntimeError e) {
                    throw e;
                }
            }
        }
        return UndefinedValue.INSTANCE;
    }

    @Override
    public Value visitTryStatement(TryStatement node, Execution execution) throws RuntimeError {
        try {
            node.getTryBlock().accept(this, execution);
        }catch (ReturnSignal signal) {
            throw signal;
        }catch (RuntimeError e) {
            execution.let(new ContextIdentity(node.getCatchVariable(), false, Instant.now(), 0), new StringValue(e.getMessage()));
            node.getCatchBlock().accept(this, execution);
        }finally {
            node.getFinallyBlock().accept(this, execution);
        }
        return UndefinedValue.INSTANCE;
    }

    @Override
    public Value visitFunctionDeclaration(FunctionDeclaration node, Execution execution) throws RuntimeError {
        List<String> args = node.getExpression().getArgs();

        ContextIdentity id = new ContextIdentity(node.getName(), true, Instant.now(), 0);
        FunctionValue value = new FunctionValue(node.getBody(), node.isAsync(), args);
        execution.var(id, value);
        return value;
    }

}
