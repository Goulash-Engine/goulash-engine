package com.barbarus.prosper.script.logic

import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.script.domain.ScriptedLogic

/**
 * Transpiles [ScriptContext] to [ScriptedLogic]
 */
class ScriptTranspiler(
    private val scriptExecuter: ScriptExecuter = ScriptExecuter()
) {
    fun transpile(scriptContext: ScriptContext): ScriptedLogic<Civilisation> {
        return ScriptedLogic<Civilisation>(scriptContext.head.name) { context ->
            scriptExecuter.execute(context, scriptContext.statements)
        }
    }
}
