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
}
