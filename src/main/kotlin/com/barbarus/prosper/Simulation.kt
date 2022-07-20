package com.barbarus.prosper

import com.barbarus.prosper.core.domain.Village
import com.barbarus.prosper.core.domain.WorldDate
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class Simulation {
    private val village = Village()
    private val date = WorldDate()

    init {
        LOG.info("Initializing simulation")
        village.clans.addAll(
            listOf(
                ClanFactory.simpleGathererClan()
                // ClanFactory.simpleGathererClan(),
                // ClanFactory.simpleGathererClan(),
                // ClanFactory.simpleGathererClan()
            )
        )
        LOG.info("${village.clans.size} clans initialized")
    }

    fun run(ticks: Int, millisecondsPerTick: Long = 1000) {
        AnsiConsole.systemInstall()
        print(ansi().eraseScreen())

        repeat(ticks) {
            date.tick()
            village.act()

            val builder = StringBuilder()
            builder.append("Starting simulation\n")
            builder.append("Maximum ticks: $ticks\n")
            builder.append("Milliseconds per tick: $millisecondsPerTick\n\n")
            builder.append("Tick ${it.plus(1)}/$ticks\n")
            builder.append("Active clans: ${village.clans.size}\n\n")
            builder.append("Clan Details:\n")
            village.clans.forEach { clan ->
                builder.append(
                    """
                @|red -- ${clan.name} --|@
                @|yellow id: ${clan.id}|@

                @|green ## State ## |@
                health: ${clan.state.health}
                stamina: ${clan.state.stamina}
                hunger: ${clan.state.hunger}

                @|green ## Conditions ## |@
                ${clan.conditions}
                    """.trimIndent()
                )
                builder.append("\n---")
                repeat(clan.name.length) { builder.append("-") }
                builder.append("---")
                builder.append("\n\n")
            }

            print(ansi().cursorToColumn(1))
            print(ansi().cursorUpLine(20).eraseScreen(Ansi.Erase.FORWARD))
            print(ansi().render(builder.toString()))

            TimeUnit.MILLISECONDS.sleep(millisecondsPerTick)
        }

        LOG.info("Simulation finished")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation")
    }
}
