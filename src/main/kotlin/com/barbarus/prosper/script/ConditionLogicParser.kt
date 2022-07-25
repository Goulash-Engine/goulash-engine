package com.barbarus.prosper.script

import com.barbarus.prosper.script.domain.ScriptedConditionLogic
import com.barbarus.prosper.script.tokenizer.Tokenizer
import org.slf4j.LoggerFactory

/**
 * Parses script files that define global blocking conditions for used in [ActivityLogic].
 */
class ConditionLogicParser(
    private val tokenizer: Tokenizer
) {

    fun parse(scriptData: String): ScriptedConditionLogic {
        val globalBlockingConditions = parseGlobalBlockingConditions(scriptData)
        return ScriptedConditionLogic(globalBlockingConditions)
    }

    private fun parseGlobalBlockingConditions(configData: String): List<String> {
        val matches = configData.trimIndent()
            .lines()
            .map { tokenizer.tokenize(it) }
            .filter { it.isNotEmpty() }

        if (matches.isEmpty()) {
            LOG.warn("No section [$GLOBAL_BLOCKING_CONDITION_SECTION] found")
            return emptyList()
        }
        if (matches[0] == GLOBAL_BLOCKING_CONDITION_SECTION) {
            return matches.drop(1)
        }
        return listOf()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("ConditionLogicParser")
        private const val GLOBAL_BLOCKING_CONDITION_SECTION = "GlobalBlocker"
    }
}
