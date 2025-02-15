package saidooubella.sash.compiler.input.provider

public interface IntInputProvider : AutoCloseable {
	public fun isDone(item: Int): Boolean
	public fun next(): Int
}
