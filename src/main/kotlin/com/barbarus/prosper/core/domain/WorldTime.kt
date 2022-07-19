package com.barbarus.prosper.core.domain

class WorldTime(
    var seconds: Int = 0,
    var minutes: Int = 0,
    var hours: Int = 0
) {
    fun tick() {
        seconds++
        if (seconds == 60) {
            seconds = 0
            minutes++
        }
        if (minutes == 60) {
            minutes = 0
            hours++
        }
    }
    override fun toString(): String {
        val sec = String.format("%02d", seconds)
        val min = String.format("%02d", minutes)
        val h = String.format("%02d", hours)
        return "$h:$min:$sec"
    }
}
