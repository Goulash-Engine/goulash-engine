package com.barbarus.prosper.core.domain

class WorldDate(
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0,
    val time: WorldTime = WorldTime()
) {
    override fun toString(): String {
        val y = String.format("%04d", year)
        val m = String.format("%02d", month)
        val d = String.format("%02d", day)
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
}
