package saidooubella.sash.compiler.input.provider

import saidooubella.sash.compiler.utils.isHighSurrogate
import saidooubella.sash.compiler.utils.isLowSurrogate
import saidooubella.sash.compiler.utils.toCodePoint

private const val EOF = -1

public class StringProvider(private val source: String) : IntInputProvider {

    private var index: Int = 0

    override fun isDone(item: Int): Boolean = item == EOF

    override fun next(): Int {

        if (index !in source.indices) {
            return EOF
        }

        val high = source[index++].code

        if (high.isHighSurrogate) {
            check(index in source.indices)
            val low = source[index++].code
            check(low.isLowSurrogate)
            return toCodePoint(high, low)
        }

        return high
    }

    override fun close(): Unit = Unit
}
