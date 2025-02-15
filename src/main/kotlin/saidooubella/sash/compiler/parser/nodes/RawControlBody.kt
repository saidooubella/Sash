package saidooubella.sash.compiler.parser.nodes

import saidooubella.sash.compiler.span.Position
import saidooubella.sash.compiler.span.Spanned
import saidooubella.sash.compiler.tokens.Token

public data class RawControlBody(
    val openBrace: Token,
    val statements: List<RawStatement>,
    val closeBrace: Token,
) : Spanned {
    override val start: Position get() = openBrace.start
    override val end: Position get() = closeBrace.end
}
