package com.barbarus.prosper.simulation

import com.barbarus.prosper.core.domain.Container
import com.barbarus.prosper.core.domain.DemoActor
import com.barbarus.prosper.core.domain.WorldDate
import com.barbarus.prosper.factory.ActorFactory
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
            ActorFactory.poorActor()
            // ActorFactory.poorGathererActor(),
            // ActorFactory.poorGathererActor(),
            // ActorFactory.simpleGathererActor()
        )
    )
) {

    init {
        LOG.info("Initializing simulation")
        LOG.info("${container.actors.size} actors initialized")
    }

    fun run() {
        if (render) {
            AnsiConsole.systemInstall()
            print(ansi().eraseScreen())
        }

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
            builder.append("Active actors: ${container.actors.size}\n\n")
            builder.append("Date: ${WORLD_TIME}\n\n")
            builder.append("Actor Monitor:\n")

            if (container.actors.isNotEmpty()) {
                renderActorDetails(builder)
            }

            print(ansi().cursor(1, 1).eraseScreen(Ansi.Erase.FORWARD))
            print(ansi().render(builder.toString()))
        }

        TimeUnit.MILLISECONDS.sleep(millisecondsPerTick)
    }

    private fun renderActorDetails(builder: StringBuilder) {
        container.actors.filterIsInstance(DemoActor::class.java).forEach { actor ->
            builder.append(
                """
                    @|red -- ${actor.name} --|@
                    @|yellow id: ${actor.id}|@
                    
                    @|green Activity: ${actor.currentActivity} |@
    
                    @|green ## State ## |@
                    health: ${String.format(Locale.US, "%.2f", actor.state.health)}
                    nourishment: ${String.format(Locale.US, "%.2f", actor.state.nourishment)}
                    
                    @|green ## Urges ## |@
                    ${
                actor.urges.getAllUrges().toList().sortedByDescending { it.second }.toMap().map { (key, value) ->
                    """${String.format(Locale.US, "%.2f", value)} | $key
                    """
                }.joinToString("")
                }
                    @|green ## Stash ## |@
                    ${
                actor.stash.joinToString("") {
                    """${String.format(Locale.US, "%.2f", it.weight)} | ${it.type}
                    """
                }
                }
                    @|green ## Conditions ## |@
                    ${actor.conditions}
                """.trimIndent()
            )
            builder.append("\n---")
            repeat(actor.name.length) { builder.append("-") }
            builder.append("---")
            builder.append("\n\n")
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation")
        val WORLD_TIME = WorldDate()
    }
}
