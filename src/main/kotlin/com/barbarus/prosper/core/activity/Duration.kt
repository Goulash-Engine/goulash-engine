package com.barbarus.prosper.core.activity

import com.barbarus.prosper.core.domain.WorldDate

class Duration(
    private val ticks: Double
) {
    fun asDouble(): Double {
        return ticks * WorldDate.TICK_BASELINE
    }
}
