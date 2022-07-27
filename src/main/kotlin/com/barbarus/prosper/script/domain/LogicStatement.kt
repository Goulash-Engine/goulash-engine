package com.barbarus.prosper.script.domain

data class LogicStatement(
    val context: String,
    val mutationType: String,
    val mutationTarget: String,
    val mutationOperation: String,
    val mutationOperationArgument: String
)
