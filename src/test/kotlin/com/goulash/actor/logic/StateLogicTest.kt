package com.goulash.actor.logic

import assertk.assertThat
import assertk.assertions.isLessThan
import com.goulash.factory.ActorFactory
import org.junit.jupiter.api.Test

internal class StateLogicTest {

    private val stateLogic = StateLogic()

    @Test
    fun `should decrease health if bad nourished`() {
        val actor = ActorFactory.poorActor()
        actor.state["nourishment"] = 50.0

        stateLogic.process(actor)

        assertThat(actor.state["health"]!!).isLessThan(100.0)
    }
}
