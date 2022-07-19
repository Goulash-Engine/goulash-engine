package com.barbarus.prosper

import com.barbarus.prosper.core.domain.Village
import com.barbarus.prosper.core.domain.WorldDate
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class Simulation() {
    private val village = Village()
    private val date = WorldDate()

    init {
        LOG.info("Initializing simulation")
        village.clans.addAll(
            listOf(
                ClanFactory.simpleGathererClan(),
                ClanFactory.simpleGathererClan(),
                ClanFactory.simpleGathererClan(),
                ClanFactory.simpleGathererClan()
            )
        )
        LOG.info("${village.clans.size} clans initialized")
    }

    fun run(tickrate: Long = 1000) {
        LOG.info("Starting simulation")
        while (true) {
            TimeUnit.MILLISECONDS.sleep(tickrate)
            date.tick()
            village.act()
        }
    }

    companion object {
        private val LOG = Logger.getLogger(this::class.java.name)
    }
}
