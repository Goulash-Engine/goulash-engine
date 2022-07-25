package com.barbarus.prosper.script

import kotlin.io.path.Path

/**
 * Parses script files that define global blocking conditions for used in [ActivityLogic].
 */
class GlobalBlockingConditionParser {

    fun parse(scriptPath: String): String {
        val path = Path(scriptPath)
        val resource = javaClass.getResource("$scriptPath/gbc_1.pros")
        return resource.readText()
    }
}
