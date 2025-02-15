package saidooubella.sash.compiler.tokens.cases.normal

import saidooubella.sash.compiler.input.MutableIntInput
import saidooubella.sash.compiler.tokens.RawToken
import saidooubella.sash.compiler.tokens.TokenType
import saidooubella.sash.compiler.tokens.TokenizerContext
import saidooubella.sash.compiler.tokens.TokenizerMode
import saidooubella.sash.compiler.tokens.cases.TokenCase
import saidooubella.sash.compiler.tokens.utils.consumeCharToken
import saidooubella.sash.compiler.tokens.utils.matches

internal object StringQuoteCase : TokenCase {

    override fun tryTokenize(context: TokenizerContext, input: MutableIntInput): RawToken? {
        return if (input.matches('"')) build(context, input) else null
    }

    private fun build(context: TokenizerContext, input: MutableIntInput): RawToken {
        context.enterMode(TokenizerMode.String)
        return input.consumeCharToken(context, TokenType.DoubleQuote)
    }
}
