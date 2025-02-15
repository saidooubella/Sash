package saidooubella.sash.compiler.parser

import saidooubella.sash.compiler.input.MutableInput
import saidooubella.sash.compiler.parser.context.ParserContext
import saidooubella.sash.compiler.parser.nodes.RawControlBody
import saidooubella.sash.compiler.parser.utils.consumeToken
import saidooubella.sash.compiler.parser.utils.consumeWhile
import saidooubella.sash.compiler.tokens.Token
import saidooubella.sash.compiler.tokens.TokenType

internal fun controlBody(context: ParserContext, input: MutableInput<Token>): RawControlBody {

    val openBrace = input.consumeToken(context, TokenType.OpenBrace, "{")

    val statements = buildList {
        input.consumeWhile(context, { input.current.type != TokenType.CloseBrace }) {
            add(statement(context, input))
        }
    }

    val closeBrace = input.consumeToken(context, TokenType.CloseBrace, "}")
    return RawControlBody(openBrace, statements, closeBrace)
}
