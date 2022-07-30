package com.barbarus.prosper.factories

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
