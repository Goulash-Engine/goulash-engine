package com.barbarus.prosper.core.domain

class WorldDate(
    val day: Int = 0,
    val month: Int = 0,
    val year: Int = 0,
    val time: WorldTime = WorldTime()
) {
    override fun toString(): String {
        return "$day-$month-$year, ${time.hours}:${time.minutes}${time.seconds})"
    }

    fun tickSecond() {
        time.seconds++
    }
}
