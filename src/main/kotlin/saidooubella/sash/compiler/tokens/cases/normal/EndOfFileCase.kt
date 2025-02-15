package saidooubella.sash.compiler.tokens.cases.normal

import saidooubella.sash.compiler.input.MutableIntInput
import saidooubella.sash.compiler.tokens.RawToken
import saidooubella.sash.compiler.tokens.TokenType
import saidooubella.sash.compiler.tokens.TokenizerContext
import saidooubella.sash.compiler.tokens.cases.TokenCase

internal object EndOfFileCase : TokenCase {

    override fun tryTokenize(context: TokenizerContext, input: MutableIntInput): RawToken? {
        return if (input.isDone) build(context) else null
    }

    private fun build(context: TokenizerContext): RawToken {
        val position = context.positionBuilder.build()
        return RawToken("end of file", TokenType.EndOfFile, position, position)
    }
}