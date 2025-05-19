package dev.kobura.evascript.runtime.interpreter;

import dev.kobura.evascript.errors.RuntimeError;
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
import dev.kobura.evascript.parsing.ast.expression.data.DataExpression;
import dev.kobura.evascript.parsing.ast.expression.data.DataItemExpression;
import dev.kobura.evascript.parsing.ast.expression.data.DataKeyExpression;
import dev.kobura.evascript.parsing.ast.expression.data.RestExpression;
import dev.kobura.evascript.parsing.ast.expression.logical.BinaryExpression;
import dev.kobura.evascript.parsing.ast.expression.logical.ComparisonExpression;
import dev.kobura.evascript.parsing.ast.expression.logical.UnaryExpression;
import dev.kobura.evascript.parsing.ast.statement.*;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.value.Value;

public interface NodeVisitor {
    // Expressions - context
    Value visitAssignementExpression(AssignementExpression node, Execution execution) throws RuntimeError;
    Value visitContextAccessExpression(ContextAccessExpression node, Execution execution) throws RuntimeError;
    Value visitVariableDeclaration(VariableDeclaration node, Execution execution) throws RuntimeError;

    // Expressions - context.variable.args
    Value visitSequencedArgumentExpression(SequencedArgumentExpression node, Execution execution) throws RuntimeError;
    Value visitStructuredArgumentExpression(StructuredArgumentExpression node, Execution execution) throws RuntimeError;

    // Expressions - logical
    Value visitBinaryExpression(BinaryExpression node, Execution execution) throws RuntimeError;
    Value visitComparisonExpression(ComparisonExpression node, Execution execution) throws RuntimeError;
    Value visitUnaryExpression(UnaryExpression node, Execution execution) throws RuntimeError;
    Value visitLambdaExpression(LambdaExpression node, Execution execution) throws RuntimeError;
    Value visitLiteralExpression(LiteralExpression node, Execution execution) throws RuntimeError;

    // Method call
    Value visitMethodCallExpression(MethodCallExpression node, Execution execution) throws RuntimeError;
    Value visitReturnStatement(ReturnStatement node, Execution execution) throws RuntimeError;

    // Statements - loops
    Value visitBlockStatement(BlockStatement node, Execution execution) throws RuntimeError;
    Value visitDoWhileStatement(DoWhileStatement node, Execution execution) throws RuntimeError;
    Value visitForStatement(ForStatement node, Execution execution) throws RuntimeError;
    Value visitWhileStatement(WhileStatement node, Execution execution) throws RuntimeError;

    // Statements - others
    Value visitExpressionStatement(ExpressionStatement node, Execution execution) throws RuntimeError;
    Value visitIfStatement(IfStatement node, Execution execution) throws RuntimeError;
    Value visitSwitchStatement(SwitchStatement node, Execution execution) throws RuntimeError;
    Value visitTryStatement(TryStatement node, Execution execution) throws RuntimeError;

    Value visitDataExpression(DataExpression node, Execution execution) throws RuntimeError;
    Value visitDataKeyExpression(DataKeyExpression node, Execution execution);
    Value visitRestExpression(RestExpression node, Execution execution) throws RuntimeError;

    // Declarations and others
    Value visitFunctionDeclaration(FunctionDeclaration node, Execution execution) throws RuntimeError;
}
