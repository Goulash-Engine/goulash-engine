package com.barbarus.prosper

import assertk.assertThat
import assertk.assertions.isBetween
import com.barbarus.prosper.core.domain.ResourceType
import com.barbarus.prosper.factories.ActorFactory
import org.junit.jupiter.api.RepeatedTest

internal class DemoActorFactoryTest {

    @RepeatedTest(5)
    fun `should create a poor actor with 1 to 3 food`() {
        val poorGathererActor = ActorFactory.poorActor()
        assertThat(poorGathererActor.stash.filter { it.type == ResourceType.FOOD }.size).isBetween(1, 3)
    }
}
