package com.barbarus.prosper

import com.barbarus.prosper.core.domain.Village
import com.barbarus.prosper.core.domain.WorldDate
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class Simulation {
    private val village = Village()
    private val date = WorldDate()

    fun run() {
        while (true) {
            TimeUnit.MILLISECONDS.sleep(1000)
            date.tick()
            LOG.info(date.toString())
        }
    }

    companion object {
        private val LOG = Logger.getLogger(this::class.java.name)
    }
}
