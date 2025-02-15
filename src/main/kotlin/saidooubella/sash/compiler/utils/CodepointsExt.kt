@file:Suppress("NOTHING_TO_INLINE")

package saidooubella.sash.compiler.utils

internal inline val Int.isHighSurrogate: Boolean
    get() = this in 0xD800..0xDBFF

internal inline val Int.isLowSurrogate: Boolean
    get() = this in 0xDC00..0xDFFF

internal inline fun toCodePoint(high: Int, low: Int): Int {
    return ((high - 0xD800 shl 10) or (low - 0xDC00)) + 0x10000
}
