package saidooubella.sash.compiler.parser

import saidooubella.sash.compiler.input.MutableInput
import saidooubella.sash.compiler.parser.context.ParserContext
import saidooubella.sash.compiler.parser.nodes.RawProgram
import saidooubella.sash.compiler.parser.utils.consumeWhile
import saidooubella.sash.compiler.tokens.Token

@Suppress("FunctionName")
public fun Parse(input: MutableInput<Token>, context: ParserContext): RawProgram {
    val statements = buildList {
        input.consumeWhile(context) { add(statement(context, input)) }
    }
    return RawProgram(statements, endOfFile = input.current)
}
