package com.goulash.core.activity

import com.goulash.core.domain.WorldDate

class Duration(
    private val ticks: Double
) {
    fun asDouble(): Double {
        return ticks * WorldDate.TICK_BASELINE
    }
}
