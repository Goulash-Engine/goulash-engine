package com.barbarus.prosper.core.domain

import java.util.Locale

class WorldDate(
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0,
    val time: WorldTime = WorldTime()
) {
    var tickBaseline: Int = 1
    override fun toString(): String {
        val y = String.format(Locale.US, "%04d", year)
        val m = String.format(Locale.US, "%02d", month)
        val d = String.format(Locale.US, "%02d", day)
        return "$d-$m-$y $time"
    }

    fun tick(base: Int = 1) {
        tickBaseline = base
        time.seconds += base

        while (time.seconds >= 60) {
            time.minutes++
            time.seconds -= WorldDate.MINUTE
        }

        while (time.minutes >= 60) {
            time.hours++
            time.minutes -= 60
        }

        while (time.hours >= 24) {
            day++
            time.hours -= 24
        }

        while (day >= 30) {
            month++
            day -= 30
        }

        while (month >= 12) {
            year++
            month -= 12
        }
    }

    fun isDay(): Boolean {
        return this.time.hours in 6..18
    }

    fun isNight(): Boolean {
        return !isDay()
    }

    companion object {
        var TICK_BASELINE: Int = 1
        const val MINUTE: Int = 60
        const val HOUR: Int = MINUTE * 60
        const val DAY: Int = HOUR * 24
        const val MONTH: Int = DAY * 30
        const val YEAR: Int = MONTH * 12
    }
}
