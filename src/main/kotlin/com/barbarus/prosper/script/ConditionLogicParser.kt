package com.barbarus.prosper.script

import com.barbarus.prosper.script.domain.ScriptedConditionLogic

/**
 * Parses script files that define global blocking conditions for used in [ActivityLogic].
 */
class ConditionLogicParser {

    fun parse(scriptPath: String): ScriptedConditionLogic {
        val resource = javaClass.getResource(scriptPath)
        val configData = resource.readText()
        val globalBlockingConditions = parseGlobalBlockingConditions(configData)
        return ScriptedConditionLogic(globalBlockingConditions)
    }

    private fun parseGlobalBlockingConditions(configData: String): List<String> {
        return listOf<String>()
    }
}
