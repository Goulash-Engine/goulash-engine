package com.barbarus.prosper.actor.logic

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import com.barbarus.prosper.factory.ActorFactory
import org.junit.jupiter.api.Test

internal class ConditionLogicTest {
    private val conditionLogic = ConditionLogic()

    @Test
    fun `should give dead condition if health is below 0`() {
        val actor = ActorFactory.simpleGathererActor()
        actor.state["health"] = -0.1

        conditionLogic.process(actor)

        assertThat(actor.conditions).contains("dead")
    }

    @Test
    fun `should make remove underfed condition if eat urge is below 100`() {
        val actor = ActorFactory.poorActor()
        actor.urges.increaseUrge("eat", 99.0)
        actor.conditions.add("underfed")

        conditionLogic.process(actor)

        assertThat(actor.conditions).doesNotContain("underfed")
    }
}
