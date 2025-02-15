package saidooubella.sash.evaluator

import saidooubella.sash.compiler.refiner.nodes.Program
import saidooubella.sash.compiler.utils.fastForEach

@Suppress("FunctionName")
public fun Evaluator(program: Program, env: Environment = builtInEnvironment()) {
    program.statements.fastForEach { evalStatement(env, it) }
}
