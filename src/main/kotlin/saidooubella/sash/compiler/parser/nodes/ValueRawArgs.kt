package saidooubella.sash.compiler.parser.nodes

import saidooubella.sash.compiler.span.Position
import saidooubella.sash.compiler.span.Spanned
import saidooubella.sash.compiler.tokens.Token
import saidooubella.sash.compiler.utils.DelimitedList

public sealed interface ValueRawArgs : Spanned

public data class SimpleRawArgs internal constructor(
    val openParent: Token,
    val valueArgs: DelimitedList<RawExpression, Token>,
    val closeParent: Token,
) : ValueRawArgs {
    override val start: Position get() = openParent.start
    override val end: Position get() = closeParent.end
}

public data class TailFunctionRawArgs internal constructor(
    val args: SimpleRawArgs?,
    val trailingFn: FunctionRawExpression,
) : ValueRawArgs {
    override val start: Position get() = args?.start ?: trailingFn.start
    override val end: Position get() = trailingFn.end
}
