package com.barbarus.prosper.core.activity

import com.barbarus.prosper.core.domain.WorldDate

class Duration(
    private val ticks: Int
) {
    fun getDuration(): Int {
        return ticks * WorldDate.TICK_BASELINE
    }
}
