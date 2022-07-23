package com.barbarus.prosper.simulation

import com.barbarus.prosper.core.domain.Village
import com.barbarus.prosper.core.domain.WorldDate
import com.barbarus.prosper.factories.ClanFactory
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import org.slf4j.LoggerFactory
import java.util.Locale
import java.util.concurrent.TimeUnit

class Simulation(
    val village: Village = Village(
        mutableListOf(
            ClanFactory.poorGathererClan()
            // ClanFactory.poorGathererClan(),
            // ClanFactory.poorGathererClan(),
            // ClanFactory.simpleGathererClan()
        )
    )
) {

    init {
        LOG.info("Initializing simulation")
        LOG.info("${village.clans.size} clans initialized")
    }

    fun run(maximumTicks: Int = -1, millisecondsPerTick: Long = 1000, render: Boolean = false) {
        AnsiConsole.systemInstall()
        print(ansi().eraseScreen())

        if (maximumTicks == -1) {
            while (true) {
                runSimulation(render, null, millisecondsPerTick, null)
            }
        } else {
            repeat(maximumTicks) { currentTick ->
                runSimulation(render, maximumTicks, millisecondsPerTick, currentTick)
            }
        }

        LOG.info("Simulation finished")
    }

    private fun runSimulation(
        render: Boolean,
        maximumTicks: Int?,
        millisecondsPerTick: Long,
        currentTick: Int?
    ) {
        WORLD_TIME.tick(WorldDate.HOUR)
        village.act()

        val builder = StringBuilder()

        if (render) {
            builder.append("Starting simulation\n")
            builder.append("Maximum ticks: ${maximumTicks ?: "\u221E"}\n")
            builder.append("Milliseconds per tick: $millisecondsPerTick\n\n")
        }

        builder.append("Tick ${currentTick?.plus(1) ?: "\u221E"}/${maximumTicks ?: "\u221E"}\n")

        if (render) {
            builder.append("Active clans: ${village.clans.size}\n\n")
            builder.append("Date: ${WORLD_TIME}\n\n")
            builder.append("Clan Details:\n")

            if (village.clans.isNotEmpty()) {
                renderClanDetails(builder)
            }

            print(ansi().cursor(1, 1).eraseScreen(Ansi.Erase.FORWARD))
            print(ansi().render(builder.toString()))
        }

        TimeUnit.MILLISECONDS.sleep(millisecondsPerTick)
    }

    private fun renderClanDetails(builder: StringBuilder) {
        village.clans.forEach { clan ->
            builder.append(
                """
                    @|red -- ${clan.name} --|@
                    @|yellow id: ${clan.id}|@
                    
                    @|green Activity: ${clan.currentActivity} |@
    
                    @|green ## State ## |@
                    health: ${clan.state.health}
                    
                    @|green ## Urges ## |@
                    ${
                clan.urges.getUrges().toList().sortedByDescending { it.second }.toMap().map { (key, value) ->
                    """${String.format(Locale.US, "%.2f", value)} | $key
                    """
                }.joinToString("")
                }
                    @|green ## Stash ## |@
                    ${
                clan.stash.joinToString("") {
                    """${String.format(Locale.US, "%.2f", it.weight)} | ${it.type}
                    """
                }
                }
                    @|green ## Conditions ## |@
                    ${clan.conditions}
                """.trimIndent()
            )
            builder.append("\n---")
            repeat(clan.name.length) { builder.append("-") }
            builder.append("---")
            builder.append("\n\n")
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation")
        val WORLD_TIME = WorldDate()
    }
}
