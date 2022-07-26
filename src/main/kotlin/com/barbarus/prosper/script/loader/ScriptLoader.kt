package com.barbarus.prosper.script.loader

import com.barbarus.prosper.script.domain.GlobalBlockerCondition
import com.barbarus.prosper.script.parser.ListConfigurationParserGrammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.parser.ParseException
import org.slf4j.LoggerFactory
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.readText

object ScriptLoader {
    private val LOG = LoggerFactory.getLogger("ScriptLoader")
    internal var globalBlockingConditions: List<String>? = null
    private val grammars: List<ListConfigurationParserGrammar> = listOf(
        ListConfigurationParserGrammar()
    )

    internal fun load(scriptDirectory: String = javaClass.getResource("/logic").path) {
        val files = Path(scriptDirectory).listDirectoryEntries("*.pros")
        var loadingError = 0
        LOG.info("Loading logic scripts from: $scriptDirectory...")
        files.asSequence().map { LOG.info("Reading script file : ${it.fileName}..."); it.readText() }
            .forEach { scriptData ->
                grammars.map {
                    try {
                        it.parseToEnd(scriptData)
                    } catch (e: ParseException) {
                        LOG.error("[Parser Error] ${e.message}")
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

    internal fun getGlobalBlockingConditionsOrDefault(default: List<String> = listOf()): List<String> =
        globalBlockingConditions ?: default
}
