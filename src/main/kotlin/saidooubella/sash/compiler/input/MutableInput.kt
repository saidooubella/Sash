package saidooubella.sash.compiler.input

import saidooubella.sash.compiler.input.provider.InputProvider

public fun <T> MutableInput(provider: InputProvider<T>): MutableInput<T> {
    return MutableInputImpl(provider)
}

public abstract class MutableInput<T> internal constructor() : Input<T>() {
    internal abstract fun advance()
    internal abstract fun mark()
    internal abstract fun rewind()
    internal abstract fun done()
}

internal fun <T> MutableInput<T>.consume(): T = current.also { _ -> advance() }

private class MutableInputImpl<T>(private val provider: InputProvider<T>) : MutableInput<T>() {

    private val backtrack = ArrayDeque<ArrayDeque<T>>()
    private val cache = ArrayDeque<T>()

    override var current = nextElement()
    override var isDone = false

    override fun advance() {
        if (isDone) return
        backtrack.lastOrNull()?.addLast(current)
        current = nextElement()
    }

    override fun mark() {
        backtrack.addLast(ArrayDeque())
    }

    override fun done() {
        val last = removeBacktrack()
        if (backtrack.isNotEmpty()) {
            backtrack.last().addAll(last)
        }
    }

    override fun rewind() {
        val last = removeBacktrack()
        last.addLast(current)
        cache.addAll(0, last)
        current = nextElement()
    }

    override fun peek(offset: Int): T {
        require(offset >= 0) { "offset < 0" }
        if (offset == 0 || isDone) return current
        if (offset <= cache.size) return cache[offset - 1]
        repeat(offset - cache.size) { _ ->
            val next = provider.next()
            if (provider.isDone(next)) return next
            cache.addLast(next)
        }
        return cache.last()
    }

    override fun close() = provider.close()

    private fun removeBacktrack(): ArrayDeque<T> {
        return backtrack.removeLastOrNull() ?: error("`mark()` must be called first")
    }

    private fun nextElement(): T {
        if (cache.isNotEmpty())
            return cache.removeFirst()
        val next = provider.next()
        isDone = provider.isDone(next)
        return next
    }
}
