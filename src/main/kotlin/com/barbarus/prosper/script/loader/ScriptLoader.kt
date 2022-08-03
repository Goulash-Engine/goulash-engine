package com.barbarus.prosper.script.loader
import com.barbarus.prosper.script.domain.ActivityScript
import com.barbarus.prosper.script.domain.ContainerScript
import com.barbarus.prosper.script.domain.GlobalBlockerCondition
import com.barbarus.prosper.script.domain.ListConfiguration
import com.barbarus.prosper.script.grammar.ActivityScriptGrammar
import com.barbarus.prosper.script.grammar.ContainerScriptGrammar
import com.barbarus.prosper.script.grammar.ListConfigurationGrammar
import com.barbarus.prosper.script.logic.ActivityScriptTranspiler
import com.barbarus.prosper.script.logic.ContainerScriptTranspiler
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.parser.ParseException
import org.slf4j.LoggerFactory
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.notExists
import kotlin.io.path.readText

object ScriptLoader {
    private val LOG = LoggerFactory.getLogger("com.barbarus.prosper.script.loader.ScriptLoader")
    private var globalBlockingConditions: List<String>? = null
    private var containerScripts: List<ContainerScript> = listOf()
    private var activityScripts: List<ActivityScript> = listOf()

    internal fun load() {
        val root = "scripts"
        val configDir = "$root/configurations"
        val activityDir = "$root/activities"
        val containerDir = "$root/container"

        listOf(root, configDir, activityDir, containerDir).forEach {
            if (Path(it).notExists()) Files.createDirectory(Path(it))
        }

        loadContainerScripts(containerDir)
        loadActivityScripts(activityDir)
        loadConfigurations(configDir)
    }

    fun resetLoader() {
        globalBlockingConditions = null
        containerScripts = listOf()
    }

    internal fun loadActivityScripts(scriptDirectory: String) {
        val grammar = ActivityScriptGrammar()
        val transpiler = ActivityScriptTranspiler()
        val files = Path(scriptDirectory).listDirectoryEntries("*.pros")
        var loadingError = 0
        LOG.info("Loading activities from: $scriptDirectory...")
        val scriptedActivities =
            files.asSequence().map { LOG.info("Reading activity script file: ${it.fileName}..."); it.readText() }
                .mapNotNull {
                    try {
                        grammar.parseToEnd(it)
                    } catch (e: ParseException) {
                        LOG.error("[Parser Error] ${e.message}")
                        loadingError++
                        null
                    } catch (e: Exception) {
                        LOG.error("[Syntax Error] ${e.message}")
                        loadingError++
                        null
                    }
                }.map(transpiler::transpile)
                .toList()

        activityScripts = scriptedActivities

        LOG.info("Finished loading ${scriptedActivities.count()} activity scripts")
        if (loadingError > 0) {
            LOG.error("$loadingError scripts failed to load")
        }
    }

    internal fun loadContainerScripts(scriptDirectory: String) {
        val grammar = ContainerScriptGrammar()
        val containerScriptTranspiler = ContainerScriptTranspiler()
        val files = Path(scriptDirectory).listDirectoryEntries("*.pros")
        var loadingError = 0
        LOG.info("Loading scripts from: $scriptDirectory...")
        val scriptLogics = files.asSequence().map { LOG.info("Reading script file: ${it.fileName}..."); it.readText() }
            .mapNotNull {
                try {
                    grammar.parseToEnd(it)
                } catch (e: ParseException) {
                    LOG.error("[Parser Error] ${e.message}")
                    loadingError++
                    null
                } catch (e: Exception) {
                    LOG.error("[Syntax Error] ${e.message}")
                    loadingError++
                    null
                }
            }.map(containerScriptTranspiler::transpile)
            .toList()

        containerScripts = scriptLogics

        LOG.info("Finished loading ${scriptLogics.count()} scripts")
        if (loadingError > 0) {
            LOG.error("$loadingError scripts failed to load")
        }
    }

    internal fun loadConfigurations(configDirectory: String) {
        val configGrammars = listOf(ListConfigurationGrammar())
        val files = Path(configDirectory).listDirectoryEntries("*.pros")
        var loadingError = 0
        var loadingCount = 0
        LOG.info("Loading configurations from: $configDirectory...")
        files.asSequence().map { LOG.info("Reading config file: ${it.fileName}..."); it.readText() }
            .forEach { configData ->
                configGrammars.flatMap {
                    try {
                        it.parseToEnd(configData)
                    } catch (e: ParseException) {
                        LOG.error("[Parser Error] ${e.message}")
                        loadingError++
                        emptyList<ListConfiguration>()
                    } catch (e: Exception) {
                        LOG.error("[Syntax Error] ${e.message}")
                        loadingError++
                        emptyList<ListConfiguration>()
                    }
                }.forEach { listConfiguration ->
                    loadingCount++
                    when (listConfiguration) {
                        is GlobalBlockerCondition -> globalBlockingConditions = listConfiguration.configurations
                        else -> LOG.warn("List configuration not supported: $listConfiguration")
                    }
                }
            }

        LOG.info("Finished loading $loadingCount configs")
        if (loadingError > 0) {
            LOG.error("$loadingError configurations failed to load")
        }
    }

    internal fun getGlobalBlockingConditionsOrDefault(default: List<String> = listOf()): List<String> =
        globalBlockingConditions ?: default

    internal fun getContainerScripts(): List<ContainerScript> {
        return containerScripts
    }

    internal fun getActivityScripts(): List<ActivityScript> {
        return activityScripts
    }
}
