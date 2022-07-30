package com.barbarus.prosper.simulation

import com.barbarus.prosper.core.domain.Clan
import com.barbarus.prosper.core.domain.Container
import com.barbarus.prosper.core.domain.WorldDate
import com.barbarus.prosper.factories.ClanFactory
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import org.slf4j.LoggerFactory
import java.util.Locale
import java.util.concurrent.TimeUnit

class Simulation(
    private val maximumTicks: Int? = null,
    private val millisecondsPerTick: Long = 1000,
    private val render: Boolean = false,
    private val tickBase: Int = WorldDate.SECOND,
    val container: Container = Container(
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
        LOG.info("${container.actors.size} clans initialized")
    }

    fun run() {
        AnsiConsole.systemInstall()
        print(ansi().eraseScreen())

        if (maximumTicks != null) {
            repeat(maximumTicks) { currentTick ->
                runSimulation(currentTick)
            }
        } else {
            while (true) {
                runSimulation()
            }
        }

        LOG.info("Simulation finished")
    }

    private fun runSimulation(currentTick: Int? = null) {
        WORLD_TIME.tick(tickBase)
        container.act()

        val builder = StringBuilder()

        if (render) {
            builder.append("Starting simulation\n")
            builder.append("Maximum ticks: ${maximumTicks ?: "\u221E"}\n")
            builder.append("Milliseconds per tick: $millisecondsPerTick\n\n")
        }

        builder.append("Tick ${currentTick?.plus(1) ?: "\u221E"}/${maximumTicks ?: "\u221E"}\n")

        if (render) {
            builder.append("Active clans: ${container.actors.size}\n\n")
            builder.append("Date: ${WORLD_TIME}\n\n")
            builder.append("Actor Monitor:\n")

            if (container.actors.isNotEmpty()) {
                renderClanDetails(builder)
            }

            print(ansi().cursor(1, 1).eraseScreen(Ansi.Erase.FORWARD))
            print(ansi().render(builder.toString()))
        }

        TimeUnit.MILLISECONDS.sleep(millisecondsPerTick)
    }

    private fun renderClanDetails(builder: StringBuilder) {
        container.actors.filterIsInstance(Clan::class.java).forEach { clan ->
            builder.append(
                """
                    @|red -- ${clan.name} --|@
                    @|yellow id: ${clan.id}|@
                    
                    @|green Activity: ${clan.currentActivity} |@
    
                    @|green ## State ## |@
                    health: ${String.format(Locale.US, "%.2f", clan.state.health)}
                    nourishment: ${String.format(Locale.US, "%.2f", clan.state.nourishment)}
                    
                    @|green ## Urges ## |@
                    ${
                clan.urges.getAllUrges().toList().sortedByDescending { it.second }.toMap().map { (key, value) ->
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
