package com.barbarus.prosper

import com.barbarus.prosper.core.domain.Village
import com.barbarus.prosper.core.domain.WorldDate
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
    private val date = WorldDate()

    init {
        LOG.info("Initializing simulation")
        LOG.info("${village.clans.size} clans initialized")
    }

    fun run(ticks: Int, millisecondsPerTick: Long = 1000, render: Boolean = false) {
        AnsiConsole.systemInstall()
        print(ansi().eraseScreen())

        repeat(ticks) {
            date.tick()
            village.act()

            val builder = StringBuilder()

            if (render) {
                builder.append("Starting simulation\n")
                builder.append("Maximum ticks: $ticks\n")
                builder.append("Milliseconds per tick: $millisecondsPerTick\n\n")
            }

            builder.append("Tick ${it.plus(1)}/$ticks\n")

            if (render) {
                builder.append("Active clans: ${village.clans.size}\n\n")
                builder.append("Clan Details:\n")

                if (village.clans.isNotEmpty()) {
                    renderClanDetails(builder)
                }

                print(ansi().cursor(1, 1).eraseScreen(Ansi.Erase.FORWARD))
                print(ansi().render(builder.toString()))
            }

            TimeUnit.MILLISECONDS.sleep(millisecondsPerTick)
        }

        LOG.info("Simulation finished")
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
                    stamina: ${clan.state.stamina}
                    hunger: ${clan.state.hunger}
                    
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
    }
}
