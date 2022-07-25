package com.barbarus.prosper.script

import com.barbarus.prosper.script.domain.ScriptedConditionLogic
import com.barbarus.prosper.script.tokenizer.Tokenizer

/**
 * Parses script files that define global blocking conditions for used in [ActivityLogic].
 */
class ConditionLogicParser(
    private val tokenizer: Tokenizer
) {

    fun parse(scriptPath: String): ScriptedConditionLogic {
        val resource = javaClass.getResource(scriptPath)
        val configData = resource.readText()
        val globalBlockingConditions = parseGlobalBlockingConditions(configData)
        return ScriptedConditionLogic(globalBlockingConditions)
    }

    private fun parseGlobalBlockingConditions(configData: String): List<String> {
        val matches = configData.lines().map { tokenizer.tokenize(it) }.filter { it.isNotEmpty() }
        if (matches[0] == GLOBAL_BLOCKING_CONDITION_SECTION) {
            return matches.drop(1)
        }
        return listOf()
    }

    companion object {
        private const val GLOBAL_BLOCKING_CONDITION_SECTION = "GlobalBlocker"
    }
}
