package saidooubella.sash.compiler.tokens.cases.normal

import saidooubella.sash.compiler.input.MutableIntInput
import saidooubella.sash.compiler.input.consume
import saidooubella.sash.compiler.tokens.RawToken
import saidooubella.sash.compiler.tokens.TokenType
import saidooubella.sash.compiler.tokens.TokenizerContext
import saidooubella.sash.compiler.tokens.cases.TokenCase
import saidooubella.sash.compiler.tokens.utils.collectWhile
import saidooubella.sash.compiler.tokens.utils.consume
import saidooubella.sash.compiler.tokens.utils.matches

internal object NumberCase : TokenCase {

    override fun tryTokenize(context: TokenizerContext, input: MutableIntInput): RawToken? {
        return if (input.current.isLatinDigit()) build(context, input) else null
    }

    private fun build(context: TokenizerContext, input: MutableIntInput): RawToken {
        val start = context.positionBuilder.build()
        input.collectWhile(context.builder) { it.isLatinDigit() }
        return if (input.matches('.') && input.peek(1).isLatinDigit()) {
            context.builder.appendCodePoint(input.consume())
            input.collectWhile(context.builder) { it.isLatinDigit() }
            val end = context.positionBuilder.build()
            RawToken(context.builder.consume(), TokenType.DecimalLiteral, start, end)
        } else {
            val end = context.positionBuilder.build()
            RawToken(context.builder.consume(), TokenType.IntegerLiteral, start, end)
        }
    }

    private fun Int.isLatinDigit(): Boolean = this in '0'.code..'9'.code
}