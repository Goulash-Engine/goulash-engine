package com.barbarus.prosper

object ClanNameFactory {

    fun randomName(): String = listOf(
        "Ulfreng",
        "Onsaka",
        "Wulzheimer",
        "Noklaster",
        "Seibsker",
        "Merowanger",
        "Baldurner"
    ).random()
}
