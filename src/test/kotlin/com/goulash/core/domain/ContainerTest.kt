package com.goulash.core.domain

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class ContainerTest {

    @Test
    fun `should mutate state of actors`() {
        val actorMock: Actor = mockk(relaxed = true)
        val actorMock2: Actor = mockk(relaxed = true)
        val container = Container(actors = mutableListOf(actorMock, actorMock2))

        container.mutateActors { actors ->
            actors.forEach { it.urges.getAllUrges() }
        }

        verify { actorMock.urges.getAllUrges() }
        verify { actorMock2.urges.getAllUrges() }
    }
}
