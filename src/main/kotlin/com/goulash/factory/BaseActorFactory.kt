package com.goulash.factory

import com.goulash.core.domain.BaseActor

object BaseActorFactory {

    fun testActor() = BaseActor(
        key = "",
        state = mutableMapOf()
    )

    fun newActor(key: String) = BaseActor(
        key = key
    )
}
