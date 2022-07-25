package com.barbarus.prosper.script.tokenizer

class Tokenizer(
    private val token: List<Regex> = listOf()
) {

    fun tokenize(input: String): String {
        return token
            .mapNotNull { token -> token.matchEntire(input) }
            .map { it.groupValues[1] }
            .firstOrNull() ?: ""
    }
}
