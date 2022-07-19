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
}
