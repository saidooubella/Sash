package saidooubella.sash.compiler.refiner

import saidooubella.sash.compiler.parser.nodes.RawControlBody
import saidooubella.sash.compiler.parser.nodes.RawExpression
import saidooubella.sash.compiler.refiner.context.RefinerContext
import saidooubella.sash.compiler.refiner.nodes.Expression
import saidooubella.sash.compiler.refiner.nodes.Statement
import saidooubella.sash.compiler.refiner.symbols.BooleanType
import saidooubella.sash.compiler.utils.fastMap

internal fun refineControlBody(context: RefinerContext, block: RawControlBody): List<Statement> {
    return block.statements.fastMap { refineStatement(context, it) }
}

internal fun refineControlFlowCondition(context: RefinerContext, condition: RawExpression): Expression {

    @Suppress("NAME_SHADOWING")
    val condition = context.withContextualType(BooleanType) {
        refineExpression(context, condition, ExpressionMode.Standalone)
    }

    if (!condition.type.assignableTo(BooleanType)) {
        context.reporter.reportInvalidConditionType(condition.start, condition.end)
    }

    return condition
}
