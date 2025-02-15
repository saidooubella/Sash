package saidooubella.sash.compiler.refiner

import saidooubella.sash.compiler.refiner.context.RefinerContext
import saidooubella.sash.compiler.refiner.nodes.AssignmentExpression
import saidooubella.sash.compiler.refiner.nodes.BinaryExpression
import saidooubella.sash.compiler.refiner.nodes.BooleanExpression
import saidooubella.sash.compiler.refiner.nodes.BreakStatement
import saidooubella.sash.compiler.refiner.nodes.ContinueStatement
import saidooubella.sash.compiler.refiner.nodes.DecimalExpression
import saidooubella.sash.compiler.refiner.nodes.EmptyStatement
import saidooubella.sash.compiler.refiner.nodes.ErrorExpression
import saidooubella.sash.compiler.refiner.nodes.ErrorStatement
import saidooubella.sash.compiler.refiner.nodes.Expression
import saidooubella.sash.compiler.refiner.nodes.ExpressionStatement
import saidooubella.sash.compiler.refiner.nodes.FunctionExpression
import saidooubella.sash.compiler.refiner.nodes.IdentifierExpression
import saidooubella.sash.compiler.refiner.nodes.IfExpression
import saidooubella.sash.compiler.refiner.nodes.IntegerExpression
import saidooubella.sash.compiler.refiner.nodes.InvokeExpression
import saidooubella.sash.compiler.refiner.nodes.LogicalBinaryExpression
import saidooubella.sash.compiler.refiner.nodes.ParenthesizedExpression
import saidooubella.sash.compiler.refiner.nodes.RecordStatement
import saidooubella.sash.compiler.refiner.nodes.ReturnStatement
import saidooubella.sash.compiler.refiner.nodes.DefinitionStatement
import saidooubella.sash.compiler.refiner.nodes.Statement
import saidooubella.sash.compiler.refiner.nodes.StringExpression
import saidooubella.sash.compiler.refiner.nodes.UnaryExpression
import saidooubella.sash.compiler.refiner.nodes.WhileStatement
import saidooubella.sash.compiler.refiner.nodes.YieldStatement
import saidooubella.sash.compiler.refiner.symbols.ErrorType
import saidooubella.sash.compiler.refiner.symbols.NothingType
import saidooubella.sash.compiler.refiner.symbols.Type
import saidooubella.sash.compiler.refiner.symbols.UnitType
import saidooubella.sash.compiler.utils.fastForEach

internal fun checkReturnPaths(context: RefinerContext, statements: List<Statement>, returnType: Type): Boolean {
    return checkReturnPaths(context, statements) || returnType.assignableTo(UnitType) || returnType == ErrorType
}

private fun checkReturnPaths(context: RefinerContext, statements: List<Statement>): Boolean {
    var result = false
    statements.fastForEach {
        if (result) {
            context.reporter.reportUnreachableCode(it.start, it.end)
        } else {
            result = checkReturnPaths(context, it)
        }
    }
    return result
}

private fun checkReturnPaths(context: RefinerContext, statement: Statement): Boolean {
    return when (statement) {
        is WhileStatement, is EmptyStatement, is ErrorStatement, is YieldStatement, is RecordStatement -> false
        is ReturnStatement, is BreakStatement, is ContinueStatement -> true
        is DefinitionStatement -> checkReturnPaths(context, statement.expression)
        is ExpressionStatement -> checkReturnPaths(context, statement.expression)
    }
}

private fun checkReturnPaths(context: RefinerContext, expression: Expression): Boolean {
    return when (expression) {
        is BooleanExpression, is DecimalExpression, is IntegerExpression, is StringExpression,
        is ErrorExpression, is FunctionExpression, is IdentifierExpression -> false
        is LogicalBinaryExpression -> checkReturnPaths(context, expression.left) or checkReturnPaths(context, expression.right)
        is BinaryExpression -> checkReturnPaths(context, expression.left) or checkReturnPaths(context, expression.right)
        is ParenthesizedExpression -> checkReturnPaths(context, expression.expression)
        is AssignmentExpression -> checkReturnPaths(context, expression.value)
        is UnaryExpression -> checkReturnPaths(context, expression.operand)
        is IfExpression -> checkReturnPaths(context, expression)
        is InvokeExpression -> {
            expression.args.fastForEach { argument ->
                if (checkReturnPaths(context, argument)) {
                    context.reporter.reportUnreachableCode(expression.start, argument.start)
                    context.reporter.reportUnreachableCode(argument.end, expression.end)
                    return true
                }
            }
            expression.type.assignableTo(NothingType)
        }
    }
}

private fun checkReturnPaths(context: RefinerContext, expression: IfExpression): Boolean {
    val ifBlock = checkReturnPaths(context, expression.ifBody)
    val elseIfBlocks = expression.elseIfClauses.all { checkReturnPaths(context, it.body) }
    val elseBlock = expression.elseBody != null && checkReturnPaths(context, expression.elseBody)
    return ifBlock && elseIfBlocks && elseBlock
}
