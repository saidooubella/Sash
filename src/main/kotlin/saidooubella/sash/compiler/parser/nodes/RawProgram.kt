package saidooubella.sash.compiler.parser.nodes

import saidooubella.sash.compiler.tokens.Token

public data class RawProgram internal constructor(
    val statements: List<RawStatement>,
    val endOfFile: Token,
)
