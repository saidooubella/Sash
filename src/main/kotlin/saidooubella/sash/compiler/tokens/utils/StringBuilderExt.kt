package saidooubella.sash.compiler.tokens.utils

internal fun StringBuilder.consume() = toString().also { clear() }
