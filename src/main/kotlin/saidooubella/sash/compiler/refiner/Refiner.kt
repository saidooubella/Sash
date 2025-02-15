package saidooubella.sash.compiler.refiner

import saidooubella.sash.compiler.parser.nodes.RawProgram
import saidooubella.sash.compiler.refiner.context.RefinerContext
import saidooubella.sash.compiler.refiner.nodes.Program
import saidooubella.sash.compiler.refiner.symbols.UnitType
import saidooubella.sash.compiler.refiner.symbols.withBuiltins
import saidooubella.sash.compiler.utils.fastMap

@Suppress("FunctionName")
public fun Refine(program: RawProgram, context: RefinerContext): Program = context.withBuiltins {
    val statements = program.statements.fastMap { refineStatement(context, it) }
    checkReturnPaths(context, statements, UnitType)
    Program(statements)
}
