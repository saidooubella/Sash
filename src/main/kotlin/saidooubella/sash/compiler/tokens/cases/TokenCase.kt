package saidooubella.sash.compiler.tokens.cases

import saidooubella.sash.compiler.input.MutableIntInput
import saidooubella.sash.compiler.tokens.RawToken
import saidooubella.sash.compiler.tokens.TokenizerContext

internal interface TokenCase {
    fun tryTokenize(context: TokenizerContext, input: MutableIntInput): RawToken?
}
