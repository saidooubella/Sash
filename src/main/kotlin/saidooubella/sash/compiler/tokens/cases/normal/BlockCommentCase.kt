package saidooubella.sash.compiler.tokens.cases.normal

import saidooubella.sash.compiler.input.MutableIntInput
import saidooubella.sash.compiler.input.consume
import saidooubella.sash.compiler.input.isNotDone
import saidooubella.sash.compiler.tokens.MetaTokenType
import saidooubella.sash.compiler.tokens.RawToken
import saidooubella.sash.compiler.tokens.TokenizerContext
import saidooubella.sash.compiler.tokens.cases.TokenCase
import saidooubella.sash.compiler.tokens.utils.collect
import saidooubella.sash.compiler.tokens.utils.consume
import saidooubella.sash.compiler.tokens.utils.matches

internal object BlockCommentCase : TokenCase {

    override fun tryTokenize(context: TokenizerContext, input: MutableIntInput): RawToken? {
        return if (input.matches("/*")) build(context, input) else null
    }

    private fun build(context: TokenizerContext, input: MutableIntInput): RawToken {

        var level = 0
        val start = context.positionBuilder.build()

        do {
            level += handleCommentsNesting(input, context)
        } while (input.isNotDone && level != 0)

        val end = context.positionBuilder.build()

        if (level != 0) context.reporter.reportUnclosedComment(end, end)

        return RawToken(context.builder.consume(), MetaTokenType.BlockComment, start, end)
    }

    private fun handleCommentsNesting(input: MutableIntInput, context: TokenizerContext): Int {
        return when {
            input.matches("/*") -> input.collect(context.builder, 2).let { 1 }
            input.matches("*/") -> input.collect(context.builder, 2).let { -1 }
            else -> context.builder.appendCodePoint(input.consume()).let { 0 }
        }
    }
}
