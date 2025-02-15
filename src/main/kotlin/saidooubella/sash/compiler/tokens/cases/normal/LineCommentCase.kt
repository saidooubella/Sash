package saidooubella.sash.compiler.tokens.cases.normal

import saidooubella.sash.compiler.input.MutableIntInput
import saidooubella.sash.compiler.tokens.MetaTokenType
import saidooubella.sash.compiler.tokens.RawToken
import saidooubella.sash.compiler.tokens.TokenizerContext
import saidooubella.sash.compiler.tokens.cases.TokenCase
import saidooubella.sash.compiler.tokens.utils.collectWhile
import saidooubella.sash.compiler.tokens.utils.consume
import saidooubella.sash.compiler.tokens.utils.matches
import saidooubella.sash.compiler.tokens.utils.notMatchesNewLine

internal object LineCommentCase : TokenCase {

    override fun tryTokenize(context: TokenizerContext, input: MutableIntInput): RawToken? {
        return if (input.matches("//")) build(context, input) else null
    }

    private fun build(context: TokenizerContext, input: MutableIntInput): RawToken {
        val start = context.positionBuilder.build()
        input.collectWhile(context.builder) { input.notMatchesNewLine() }
        val end = context.positionBuilder.build()
        return RawToken(context.builder.consume(), MetaTokenType.LineComment, start, end)
    }
}
