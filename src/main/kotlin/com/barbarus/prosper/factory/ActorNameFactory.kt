package com.barbarus.prosper.factory

object ActorNameFactory {

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
