package com.goulash.script.extension

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.goulash.factory.ActorFactory
import com.goulash.script.extension.TranspilerExtensions.tryScriptFilter
import org.junit.jupiter.api.Test

internal class TranspilerExtensionsTest {

    @Test
    fun `should filter actor if nourishment is below 30`() {
        val actor = ActorFactory.testActor()
        actor.state["nourishment"] = 20.0

        assertThat(actor.tryScriptFilter("state.nourishment < 30")).isNotNull()
    }

    @Test
    fun `should exclude actor if foo is below 30`() {
        val actor = ActorFactory.testActor()
        actor.state["foo"] = 50.0

        assertThat(actor.tryScriptFilter("state.foo < 30")).isNull()
    }

    @Test
    fun `should filter 3 actors out of 5`() {
        val actors = generateSequence { ActorFactory.testActor() }.take(5).toList()
        actors[0].state["foo"] = 10.0
        actors[1].state["foo"] = 10.0
        actors[2].state["foo"] = 10.0
        actors[3].state["foo"] = 1.0
        actors[4].state["foo"] = 1.0

        assertThat(actors.tryScriptFilter("state.foo >= 10")).hasSize(3)
    }

    @Test
    fun `should filter actor if foo is below 30`() {
        val actor = ActorFactory.testActor()
        actor.state["foo"] = 2.0

        assertThat(actor.tryScriptFilter("state.foo < 30")).isNotNull()
    }

    @Test
    fun `should filter actor if health is below 30`() {
        val actor = ActorFactory.testActor()
        actor.state["health"] = 2.0

        assertThat(actor.tryScriptFilter("state.health < 30")).isNotNull()
    }

    @Test
    fun `should exclude actor if health is above 30`() {
        val actor = ActorFactory.testActor()
        actor.state["health"] = 40.0

        assertThat(actor.tryScriptFilter("state.health < 30")).isNull()
    }
}
