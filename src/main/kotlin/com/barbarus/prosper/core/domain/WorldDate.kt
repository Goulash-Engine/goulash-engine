package com.barbarus.prosper.core.domain

import java.util.Locale

class WorldDate(
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0,
    val time: WorldTime = WorldTime()
) {
    override fun toString(): String {
        val y = String.format(Locale.US, "%04d", year)
        val m = String.format(Locale.US, "%02d", month)
        val d = String.format(Locale.US, "%02d", day)
        return "$d-$m-$y $time"
    }

    fun tick() {
        time.tick()
        if (time.hours == 24) {
            time.hours = 0
            day++
        }
        if (day == 30) {
            day = 0
            month++
        }
        if (month == 12) {
            month = 0
            year++
        }
    }

    fun isDay(): Boolean {
        return this.time.hours in 6..18
    }

    fun isNight(): Boolean {
        return !isDay()
    }
}
