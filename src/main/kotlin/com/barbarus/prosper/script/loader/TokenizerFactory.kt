package com.barbarus.prosper.script.loader

import com.barbarus.prosper.script.tokenizer.Tokenizer

object TokenizerFactory {
    fun conditionLogicTokenizer() = Tokenizer(
        listOf(
            sectionToken(),
            listValueToken()
        )
    )

    private fun sectionToken() = Regex("^\\[(.*)\\]\$")
    private fun listValueToken() = Regex("^- (.*)$")
}
