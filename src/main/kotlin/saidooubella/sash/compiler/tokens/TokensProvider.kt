package saidooubella.sash.compiler.tokens

import saidooubella.sash.compiler.input.MutableInput
import saidooubella.sash.compiler.input.consume
import saidooubella.sash.compiler.input.isNotDone
import saidooubella.sash.compiler.input.provider.InputProvider

public class TokensProvider(
    private val input: MutableInput<RawToken>,
) : InputProvider<Token> {

    override fun isDone(item: Token): Boolean = item.type == TokenType.EndOfFile

    override fun next(): Token {
        val leading = metaTokens(input, false)
        val token = input.consume()
        val trailing = metaTokens(input, true)
        return token.run { Token(text, type as TokenType, start, end, leading, trailing) }
    }

    override fun close(): Unit = input.close()
}

private fun metaTokens(input: MutableInput<RawToken>, isTrailing: Boolean) = buildList {
    while (input.isNotDone && input.current.type is MetaTokenType) {
        val token = input.consume()
        add(MetaToken(token.text, token.type as MetaTokenType, token.start, token.end))
        if (isTrailing && token.type == MetaTokenType.LineBreak) break
    }
}
