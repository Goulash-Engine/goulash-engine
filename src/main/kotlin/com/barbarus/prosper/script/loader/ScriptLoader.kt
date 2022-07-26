package com.barbarus.prosper.script.loader

import com.barbarus.prosper.script.domain.GlobalBlockerCondition
import com.barbarus.prosper.script.domain.ListConfiguration
import com.barbarus.prosper.script.parser.GlobalBlockerConditionGrammar
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.parser.ParseException
import org.slf4j.LoggerFactory
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.readText

object ScriptLoader {
    private const val logicScriptPath = "/logic"
    private val LOG = LoggerFactory.getLogger("ScriptLoader")
    private var globalBlockingConditions: List<String>? = null
    private val grammars: List<Grammar<ListConfiguration>> = listOf(
        GlobalBlockerConditionGrammar()
    )

    internal fun load() {
        val files = Path(javaClass.getResource("/logic").path).listDirectoryEntries("*.pros")
        var loadingError = 0
        LOG.info("Loading logic scripts from: $logicScriptPath...")
        files.asSequence().map { LOG.info("Reading script file : ${it.fileName}..."); it.readText() }
            .forEach { scriptData ->
                grammars.map {
                    try {
                        it.parseToEnd(scriptData)
                    } catch (e: ParseException) {
                        LOG.error("[Parsing Error] ${e.message}")
                        loadingError++
                    } catch (e: Exception) {
                        LOG.error("[Syntax Error] ${e.message}")
                        loadingError++
                    }
                }.forEach { listConfiguration ->
                    when (listConfiguration) {
                        is GlobalBlockerCondition -> globalBlockingConditions = listConfiguration.configurations
                    }
                }
            }
        LOG.info("Finished loading logic scripts")
        if (loadingError > 0) {
            LOG.error("$loadingError scripts failed to load")
        }
    }

    internal fun getGlobalBlockingConditionsLogicOrDefault(default: List<String> = listOf()): List<String> =
        globalBlockingConditions ?: default
}
