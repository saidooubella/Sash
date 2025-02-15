package saidooubella.sash.compiler.input

public abstract class Input<T> internal constructor() : AutoCloseable {
	internal abstract val isDone: Boolean
	internal abstract val current: T
	internal abstract fun peek(offset: Int): T
}

internal val <T> Input<T>.isNotDone: Boolean
	get() = !isDone
