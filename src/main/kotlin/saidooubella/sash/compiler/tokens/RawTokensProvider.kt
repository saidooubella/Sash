package saidooubella.sash.compiler.tokens

import saidooubella.sash.compiler.input.MutableIntInput
import saidooubella.sash.compiler.input.provider.InputProvider
import saidooubella.sash.compiler.tokens.cases.normal.BlockCommentCase
import saidooubella.sash.compiler.tokens.cases.normal.EndOfFileCase
import saidooubella.sash.compiler.tokens.cases.normal.IdentifierCase
import saidooubella.sash.compiler.tokens.cases.normal.IllegalCharacterCase
import saidooubella.sash.compiler.tokens.cases.normal.LineBreakCase
import saidooubella.sash.compiler.tokens.cases.normal.LineCommentCase
import saidooubella.sash.compiler.tokens.cases.normal.NumberCase
import saidooubella.sash.compiler.tokens.cases.normal.PunctuationCase
import saidooubella.sash.compiler.tokens.cases.normal.StringQuoteCase
import saidooubella.sash.compiler.tokens.cases.normal.WhitespaceCase
import saidooubella.sash.compiler.tokens.cases.string.StringLiteralCase
import saidooubella.sash.compiler.tokens.cases.string.StringUnquoteCase

private val normalModeCases = listOf(
    EndOfFileCase,
    BlockCommentCase,
    LineCommentCase,
    LineBreakCase,
    WhitespaceCase,
    IdentifierCase,
    NumberCase,
    StringQuoteCase,
    PunctuationCase,
    IllegalCharacterCase,
)

private val stringModeCases = listOf(
    StringUnquoteCase,
    StringLiteralCase,
)

public class RawTokensProvider(
    private val input: MutableIntInput,
    private val context: TokenizerContext,
) : InputProvider<RawToken> {

    override fun next(): RawToken {
        while (true) {
            return when (context.currentMode) {
                TokenizerMode.Normal -> normalModeToken()
                TokenizerMode.String -> stringModeToken()
            } ?: continue
        }
    }

    private fun stringModeToken(): RawToken? {
        return stringModeCases.firstNotNullOfOrNull { it.tryTokenize(context, input) }
    }

    private fun normalModeToken(): RawToken {
        return normalModeCases.firstNotNullOf { it.tryTokenize(context, input) }
    }

    override fun isDone(item: RawToken): Boolean {
        return item.type == TokenType.EndOfFile
    }

    override fun close(): Unit = input.close()
}
