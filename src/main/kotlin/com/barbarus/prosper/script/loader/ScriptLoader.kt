import com.barbarus.prosper.core.domain.Container
import com.barbarus.prosper.script.domain.GlobalBlockerCondition
import com.barbarus.prosper.script.domain.ListConfiguration
import com.barbarus.prosper.script.domain.ScriptedActivity
import com.barbarus.prosper.script.domain.ScriptedLogic
import com.barbarus.prosper.script.grammar.ActivityScriptGrammar
import com.barbarus.prosper.script.grammar.ListConfigurationGrammar
import com.barbarus.prosper.script.grammar.LogicScriptGrammar
import com.barbarus.prosper.script.loader.ScriptedActivityBuilder
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
    private val LOG = LoggerFactory.getLogger("ScriptLoader")
    private var globalBlockingConditions: List<String>? = null
    private var logicScripts: List<ScriptedLogic<Container>> = listOf()
    private var activityScripts: List<ScriptedActivity> = listOf()

    internal fun load() {
        val logicDir = "logic"
        val configDir = "$logicDir/config"
        val activityDir = "$logicDir/activity"

        listOf(logicDir, configDir, activityDir).forEach {
            if (Path(it).notExists()) Files.createDirectory(Path(it))
        }

        loadLogicScripts(logicDir)
        loadActivityScripts(logicDir)
        loadConfigurations(configDir)
    }

    fun resetLoader() {
        globalBlockingConditions = null
        logicScripts = listOf()
    }

    internal fun loadActivityScripts(scriptDirectory: String) {
        val activityGrammar = ActivityScriptGrammar()
        val scriptedActivityBuilder = ScriptedActivityBuilder()
        val files = Path(scriptDirectory).listDirectoryEntries("*.pros")
        var loadingError = 0
        LOG.info("Loading activities from: $scriptDirectory...")
        val scriptedActivities =
            files.asSequence().map { LOG.info("Reading activity script file: ${it.fileName}..."); it.readText() }
                .mapNotNull { scriptData ->
                    try {
                        activityGrammar.parseToEnd(scriptData)
                    } catch (e: ParseException) {
                        LOG.error("[Parser Error] ${e.message}")
                        loadingError++
                        null
                    } catch (e: Exception) {
                        LOG.error("[Syntax Error] ${e.message}")
                        loadingError++
                        null
                    }
                }.map(scriptedActivityBuilder::parse)
                .toList()

        activityScripts = scriptedActivities

        LOG.info("Finished loading ${scriptedActivities.count()} activity scripts")
        if (loadingError > 0) {
            LOG.error("$loadingError scripts failed to load")
        }
    }

    internal fun loadLogicScripts(scriptDirectory: String) {
        val scriptGrammar = LogicScriptGrammar()
        val containerScriptTranspiler = ContainerScriptTranspiler()
        val files = Path(scriptDirectory).listDirectoryEntries("*.pros")
        var loadingError = 0
        LOG.info("Loading scripts from: $scriptDirectory...")
        val scriptLogics = files.asSequence().map { LOG.info("Reading script file: ${it.fileName}..."); it.readText() }
            .mapNotNull { scriptData ->
                try {
                    scriptGrammar.parseToEnd(scriptData)
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

        logicScripts = scriptLogics

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

    internal fun getLogicScripts(): List<ScriptedLogic<Container>> {
        return logicScripts
    }

    internal fun getActivityScripts(): List<ScriptedLogic<Container>> {
        return logicScripts
    }
}
