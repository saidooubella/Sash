package saidooubella.sash.compiler.tokens.cases.normal

import saidooubella.sash.compiler.input.MutableIntInput
import saidooubella.sash.compiler.tokens.MetaTokenType
import saidooubella.sash.compiler.tokens.RawToken
import saidooubella.sash.compiler.tokens.TokenizerContext
import saidooubella.sash.compiler.tokens.cases.TokenCase
import saidooubella.sash.compiler.tokens.utils.collect
import saidooubella.sash.compiler.tokens.utils.consume
import saidooubella.sash.compiler.tokens.utils.matches
import saidooubella.sash.compiler.tokens.utils.matchesNewLine

internal object LineBreakCase : TokenCase {

    override fun tryTokenize(context: TokenizerContext, input: MutableIntInput): RawToken? {
        return if (input.matchesNewLine()) build(context, input) else null
    }

    private fun build(context: TokenizerContext, input: MutableIntInput): RawToken {
        val start = context.positionBuilder.build()
        val length = if (input.matches("\r\n")) 2 else 1
        input.collect(context.builder, length)
        val end = context.positionBuilder.build()
        return RawToken(context.builder.consume(), MetaTokenType.LineBreak, start, end)
    }
}
