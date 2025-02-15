package saidooubella.sash.compiler.refiner

import saidooubella.sash.compiler.parser.nodes.RawFunctionType
import saidooubella.sash.compiler.parser.nodes.RawSimpleType
import saidooubella.sash.compiler.parser.nodes.RawType
import saidooubella.sash.compiler.refiner.context.RefinerContext
import saidooubella.sash.compiler.refiner.symbols.ErrorType
import saidooubella.sash.compiler.refiner.symbols.FunctionType
import saidooubella.sash.compiler.refiner.symbols.Type
import saidooubella.sash.compiler.tokens.TokenType
import saidooubella.sash.compiler.utils.map

internal fun refineType(context: RefinerContext, type: RawType): Type {
    return when (type) {
        is RawFunctionType -> refineFunctionType(context, type)
        is RawSimpleType -> refineSimpleType(context, type)
    }
}

private fun refineFunctionType(context: RefinerContext, type: RawFunctionType): Type {
    val valueParams = type.valueParams.map { refineType(context, it) }
    return FunctionType(listOf(), valueParams, refineType(context, type.returnType))
}

private fun refineSimpleType(context: RefinerContext, type: RawSimpleType): Type {
    if (type.name.type == TokenType.Injected) return ErrorType
    type.typeArgs?.args?.map { refineType(context, it) }
    return context.getType(type.name.text) ?: run {
        context.reporter.reportUndefinedSymbol(type.start, type.end, type.name.text)
        ErrorType
    }
}
