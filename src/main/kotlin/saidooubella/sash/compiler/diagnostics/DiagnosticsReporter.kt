package saidooubella.sash.compiler.diagnostics

import saidooubella.sash.compiler.span.Position

public class DiagnosticsReporter(private val fileName: String) {

    private val reports = mutableListOf<Diagnostic>()

    private val drafts = ArrayDeque<MutableList<Diagnostic>>()

    private fun report(start: Position, end: Position, message: String) {
        val storage = drafts.lastOrNull() ?: reports
        storage += Diagnostic(fileName, message, start, end)
    }

    internal fun draft() {
        drafts.addLast(ArrayList())
    }

    internal fun retain() {
        val draft = removeDraft()
        val storage = drafts.lastOrNull() ?: reports
        storage += draft
    }

    internal fun discard() {
        removeDraft()
    }

    private fun removeDraft(): List<Diagnostic> {
        return drafts.removeLastOrNull() ?: error("`collect()` must be called first")
    }

    public fun build(): List<Diagnostic> = reports.toList()

    internal fun reportInvalidEscaping(start: Position, end: Position, esc: String) {
        report(start, end, "Invalid escaping \\$esc")
    }

    internal fun reportIncompleteEscaping(start: Position, end: Position) {
        report(start, end, "Expected and escaping sequence")
    }

    internal fun reportUnclosedComment(start: Position, end: Position) {
        report(start, end, "Unclosed comment")
    }

    internal fun reportIllegalCharacter(start: Position, end: Position, text: String) {
        report(start, end, "Invalid character `$text`")
    }

    internal fun reportUnexpectedToken(start: Position, end: Position, expected: String, actual: String?) {
        val message = when (actual) {
            null -> "Expected $expected"
            else -> "Expected `$expected` but got `$actual`"
        }
        report(start, end, message)
    }

    internal fun reportUnterminatedString(start: Position, end: Position) {
        report(start, end, "Unterminated string")
    }

    internal fun reportRedundantSemicolon(start: Position, end: Position, count: Int) {
        report(start, end, "Redundant " + if (count > 1) "semicolons" else "semicolon")
    }
}
