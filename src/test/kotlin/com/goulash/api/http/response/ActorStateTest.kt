package com.goulash.api.http.response

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.goulash.factory.BaseActorFactory
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class ActorStateTest {

    @Disabled
    @Test
    fun `should return actor state with urges sorted by highest urge at top`() {
        val testActor = BaseActorFactory.testActor()
        testActor.urges.increaseUrge("eat", 100.0)
        testActor.urges.increaseUrge("sleep", 30.0)
        testActor.urges.increaseUrge("shat", 10.0)

        val actorState = testActor.toResponse()

        assertThat(actorState.urges.values.toList()[0]).isEqualTo(100.0)
        assertThat(actorState.urges.values.toList()[1]).isEqualTo(30.0)
        assertThat(actorState.urges.values.toList()[2]).isEqualTo(10.0)
        assertThat(actorState.urges.values.toList()[3]).isEqualTo(1.0)
    }
}
