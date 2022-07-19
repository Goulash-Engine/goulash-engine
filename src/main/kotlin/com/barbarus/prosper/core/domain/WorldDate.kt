package com.barbarus.prosper.core.domain

class WorldDate(
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0,
    val time: WorldTime = WorldTime()
) {
    override fun toString(): String {
        return "$day-$month-$year, ${time.hours}:${time.minutes}${time.seconds})"
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
