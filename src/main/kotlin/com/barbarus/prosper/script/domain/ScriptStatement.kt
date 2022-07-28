package com.barbarus.prosper.script.domain

data class ScriptStatement(
    val context: String,
    val filter: String,
    val mutationType: String,
    val mutationTarget: String,
    val mutationOperation: String,
    val mutationOperationArgument: String
)
