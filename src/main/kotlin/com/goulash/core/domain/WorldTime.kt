package com.goulash.core.domain

import java.util.Locale

class WorldTime(
    var seconds: Int = 0,
    var minutes: Int = 0,
    var hours: Int = 0
) {

    override fun toString(): String {
        val sec = String.format(Locale.US, "%02d", seconds)
        val min = String.format(Locale.US, "%02d", minutes)
        val h = String.format(Locale.US, "%02d", hours)
        return "$h:$min:$sec"
    }
}
