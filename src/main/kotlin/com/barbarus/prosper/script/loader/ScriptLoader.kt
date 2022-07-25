package com.barbarus.prosper.script.loader

import com.barbarus.prosper.script.ConditionLogicParser
import org.slf4j.LoggerFactory
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.readText

object ScriptLoader {
    private const val logicScriptPath = "/logic"
    private val LOG = LoggerFactory.getLogger("ScriptLoader")
    private var loadedGlobalBlockingConditions: List<String>? = null

    internal var conditionLogicParser = ConditionLogicParser(TokenizerFactory.conditionLogicTokenizer())

    internal fun load() {
        val files = Path(javaClass.getResource("/logic").path).listDirectoryEntries("*")
        LOG.info("Loading logic scripts from: $logicScriptPath...")
        files.forEach {
            LOG.info("Loading script file : ${it.fileName}...")
            LOG.info("Parsing condition logic scripts...")
            val conditionLogic = conditionLogicParser.parse(it.readText())
            loadedGlobalBlockingConditions = conditionLogic.globalBlockingConditions
        }
        LOG.info("Finished loading logic scripts")
    }

    internal fun getGlobalConditionLogicOrDefault(default: List<String>): List<String> =
        loadedGlobalBlockingConditions ?: default
}
