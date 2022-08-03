package com.barbarus.prosper.actor.logic

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import com.barbarus.prosper.core.DecisionEngine
import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.extension.toDuration
import com.barbarus.prosper.factories.ActorFactory
import com.barbarus.prosper.factories.ResourceFactory
import com.barbarus.prosper.script.domain.ActivityScript
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test

internal class DecisionEngineTest {
    private val decisionEngine = DecisionEngine()

    @Test
    fun `should properly use script`() {
        val scriptedSleepActivity = spyk(
            ActivityScript(
                "sleeping",
                mapOf("trigger_urges" to listOf("sleep")),
                { _ -> true },
                { _ -> },
                { _ -> }
            )
        )

        val testActor = ActorFactory.testActor(listOf(scriptedSleepActivity))
        testActor.urges.increaseUrge("sleep", 10.0)

        repeat(1) { decisionEngine.process(testActor) }

        verifyOrder {
            repeat(1) { scriptedSleepActivity.act(any()) }
        }
    }

    @Test
    fun `should abort activity if has been exited within act`() {
        val exitingActivity = mockk<Activity>("foo", relaxed = true)
        every { exitingActivity.triggerUrges() } returns listOf("foo")
        every { exitingActivity.duration() } returns 5.0.toDuration()
        every { exitingActivity.act(any()) } returnsMany listOf(true, true, true, true, false)

        val testActor = ActorFactory.testActor(listOf(exitingActivity))
        testActor.urges.increaseUrge("foo", 10.0)

        repeat(10) { decisionEngine.process(testActor) }

        verifyOrder {
            repeat(4) { exitingActivity.act(any()) }
            exitingActivity.onAbort(any())
        }
    }

    @Test
    fun `should not execute a wildcard activity if there is an urge activity present`() {
        val idleActivity = mockk<Activity>("idle", relaxed = true)
        every { idleActivity.triggerUrges() } returns listOf("*")
        every { idleActivity.duration() } returns 1.0.toDuration()

        val urgeActivity = mockk<Activity>("eat", relaxed = true)
        every { urgeActivity.triggerUrges() } returns listOf("eat")
        every { urgeActivity.duration() } returns 10.0.toDuration()

        val actor = ActorFactory.testActor(listOf(idleActivity, urgeActivity))
        actor.urges.increaseUrge("eat", 100.0)
        actor.conditions.add("underfed")

        repeat(10) { decisionEngine.process(actor) }

        verify(inverse = true) { idleActivity.act(any()) }
    }

    @Test
    fun `should execute on abort callback if activity is being aborted`() {
        val testActivity = mockk<Activity>("prio", relaxed = true)
        every { testActivity.triggerUrges() } returns listOf("sleep")
        every { testActivity.abortConditions() } returns listOf("stop")
        every { testActivity.duration() } returns 10.0.toDuration()
        every { testActivity.act(any()) } returns true

        val actor = ActorFactory.testActor(listOf(testActivity))
        actor.urges.increaseUrge("sleep", 100.0)

        repeat(2) { decisionEngine.process(actor) }
        actor.conditions.add("stop")
        repeat(1) { decisionEngine.process(actor) }

        verify(atMost = 2) { testActivity.act(any()) }
        verify(atMost = 1) { testActivity.onAbort(any()) }
    }

    @Test
    fun `should prioritize an activity depending on it_s priority value`() {
        val prioritizedActivity = mockk<Activity>("prio", relaxed = true)
        every { prioritizedActivity.triggerUrges() } returns listOf("sleep")
        every { prioritizedActivity.duration() } returns 1.0.toDuration()
        every { prioritizedActivity.priority() } returns 1
        every { prioritizedActivity.act(any()) } answers {
            firstArg<Actor>().urges.decreaseUrge("sleep", 50.0)
            true
        }

        val normalActivity = mockk<Activity>("normal", relaxed = true)
        every { normalActivity.triggerUrges() } returns listOf("eat")
        every { normalActivity.duration() } returns 1.0.toDuration()
        every { normalActivity.priority() } returns Int.MAX_VALUE

        val actor = ActorFactory.testActor(listOf(prioritizedActivity, normalActivity))
        actor.urges.increaseUrge("eat", 100.0)
        actor.urges.increaseUrge("sleep", 100.0)

        repeat(2) { decisionEngine.process(actor) }

        verifyOrder {
            prioritizedActivity.act(any())
            normalActivity.act(any())
        }
    }

    @Test
    fun `should prioritize an activity if it_s priority condition has been met`() {
        val prioritizedActivity = mockk<Activity>("prio", relaxed = true)
        every { prioritizedActivity.triggerUrges() } returns listOf("think")
        every { prioritizedActivity.duration() } returns 1.0.toDuration()
        every { prioritizedActivity.priorityConditions() } returns listOf("panic")
        every { prioritizedActivity.act(any()) } answers { firstArg<Actor>().conditions.remove("panic") }

        val normalActivity = mockk<Activity>("normal", relaxed = true)
        every { normalActivity.triggerUrges() } returns listOf("eat")
        every { normalActivity.duration() } returns 1.0.toDuration()

        val actor = ActorFactory.testActor(listOf(prioritizedActivity, normalActivity))
        actor.conditions.add("panic")
        actor.urges.increaseUrge("eat", 100.0)
        actor.urges.increaseUrge("think", 20.0)

        repeat(2) { decisionEngine.process(actor) }

        verifyOrder {
            prioritizedActivity.act(any())
            normalActivity.act(any())
        }
    }

    @Test
    fun `should not run any activity if global blocking condition is met`() {
        val mockedWorkActivity = mockk<Activity>("workMock", relaxed = true)
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.duration() } returns 5.0.toDuration()
        every { mockedWorkActivity.abortConditions() } returns listOf("starving")
        val actor = ActorFactory.testActor(listOf(mockedWorkActivity))
        actor.urges.increaseUrge("work", 100.0)
        ConditionLogic.GLOBAL_BLOCKING_CONDITION = listOf("starving")
        actor.conditions.add(ConditionLogic.GLOBAL_BLOCKING_CONDITION.first())

        decisionEngine.process(actor)

        verify(inverse = true) { mockedWorkActivity.act(any()) }
    }

    @Test
    fun `should remove current activity if abort condition has been met`() {
        val mockedWorkActivity = mockk<Activity>("workMock", relaxed = true)
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.duration() } returns 5.0.toDuration()
        every { mockedWorkActivity.abortConditions() } returns listOf("starving")
        val clan = ActorFactory.testActor(listOf(mockedWorkActivity))
        clan.urges.increaseUrge("work", 100.0)

        repeat(3) { decisionEngine.process(clan) }
        clan.conditions.add("starving")
        repeat(2) { decisionEngine.process(clan) }

        verify(atMost = 3) { mockedWorkActivity.act(any()) }
    }

    @Test
    fun `should add resource to stash of actor if activity has finished`() {
        val mockedWorkActivity = mockk<Activity>("workMock")
        every { mockedWorkActivity.triggerUrges() } returns listOf("*")
        every { mockedWorkActivity.blockerConditions() } returns listOf("sick")
        every { mockedWorkActivity.activity() } returns "working"
        every { mockedWorkActivity.duration() } returns 1.0.toDuration()

        val clan = ActorFactory.testActor(listOf(mockedWorkActivity))
        val expectedResource = ResourceFactory.food()
        every { mockedWorkActivity.onFinish(clan) } answers {
            val actor = firstArg<Actor>()
            actor.inventory().add(expectedResource)
        }
        every { mockedWorkActivity.act(clan) } returns true

        decisionEngine.process(clan)

        verify { mockedWorkActivity.triggerUrges() }
        verify { mockedWorkActivity.blockerConditions() }
        verify { mockedWorkActivity.act(clan) }
        assertThat(clan.stash).contains(expectedResource)
    }

    @Test
    fun `should execute an activity with higher urge for duration and then the next activity`() {
        val primaryActivity = mockk<Activity>(name = "primary")
        every { primaryActivity.triggerUrges() } returns listOf("work")
        every { primaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { primaryActivity.activity() } returns "working"
        every { primaryActivity.duration() } returns 2.0.toDuration()
        justRun { primaryActivity.onFinish(any()) }

        val secondaryActivity = mockk<Activity>(name = "secondary")
        every { secondaryActivity.triggerUrges() } returns listOf("rest")
        every { secondaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { secondaryActivity.activity() } returns "resting"
        every { secondaryActivity.duration() } returns 1.0.toDuration()
        justRun { secondaryActivity.onFinish(any()) }

        val clan = ActorFactory.testActor(listOf(primaryActivity, secondaryActivity))

        every { primaryActivity.act(clan) } answers {
            clan.urges.decreaseUrge("work", 5.0)
            true
        }
        every { secondaryActivity.act(clan) } returns true

        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("rest", 9.0)

        decisionEngine.process(clan)
        decisionEngine.process(clan)
        decisionEngine.process(clan)

        verify(exactly = 2) { primaryActivity.act(clan) }
        verify(exactly = 1) { secondaryActivity.act(clan) }
    }

    @Test
    fun `should execute an activity for it's duration value`() {
        val primaryActivity = mockk<Activity>(name = "primary")
        every { primaryActivity.triggerUrges() } returns listOf("work")
        every { primaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { primaryActivity.activity() } returns "working"
        every { primaryActivity.duration() } returns 4.0.toDuration()
        justRun { primaryActivity.onFinish(any()) }

        val secondaryActivity = mockk<Activity>()
        every { secondaryActivity.triggerUrges() } returns listOf("rest")
        every { secondaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { secondaryActivity.activity() } returns "resting"
        every { secondaryActivity.duration() } returns 2.0.toDuration()
        justRun { secondaryActivity.onFinish(any()) }

        val clan = ActorFactory.testActor(listOf(primaryActivity, secondaryActivity))

        every { primaryActivity.act(clan) } returns true
        every { secondaryActivity.act(clan) } returns true

        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("resting", 9.0)

        repeat(4) { decisionEngine.process(clan) }

        verify(exactly = 4) { primaryActivity.act(clan) }
    }

    @Test
    fun `should set current activity of a context`() {
        val firstActivity = mockk<Activity>()
        every { firstActivity.triggerUrges() } returns listOf("work")
        every { firstActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { firstActivity.activity() } returns "working"
        every { firstActivity.duration() } returns 2.0.toDuration()
        val clan = ActorFactory.testActor(listOf(firstActivity))
        every { firstActivity.act(clan) } returns true
        clan.urges.increaseUrge("work", 10.0)

        decisionEngine.process(clan)

        assertThat(clan.currentActivity).isEqualTo(firstActivity.activity())
    }

    @Test
    fun `should execute wildcard activities without a matching urge`() {
        val mockedIdleActivity = mockk<Activity>("idle")
        every { mockedIdleActivity.triggerUrges() } returns listOf("*")
        every { mockedIdleActivity.blockerConditions() } returns listOf()
        every { mockedIdleActivity.activity() } returns "idle"
        every { mockedIdleActivity.duration() } returns 1.0.toDuration()
        justRun { mockedIdleActivity.onFinish(any()) }
        val clan = ActorFactory.testActor(listOf(mockedIdleActivity))
        clan.urges.increaseUrge("work", 100.0)
        every { mockedIdleActivity.act(any()) } returns true

        decisionEngine.process(clan)

        verify { mockedIdleActivity.act(any()) }
    }

    @Test
    fun `should chose the first of two equally urgent urges`() {
        val mockedWorkActivity = mockk<Activity>()
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { mockedWorkActivity.activity() } returns "work"
        every { mockedWorkActivity.duration() } returns 5.0.toDuration()
        val clan = ActorFactory.testActor(listOf(mockedWorkActivity))
        every { mockedWorkActivity.act(clan) } returns true
        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("rest", 10.0)

        decisionEngine.process(clan)

        verify { mockedWorkActivity.triggerUrges() }
        verify { mockedWorkActivity.blockerConditions() }
        verify { mockedWorkActivity.act(clan) }
    }

    @Test
    fun `should not execute activity if trigger urge is the top urge but blocking condition exists`() {
        val mockedWorkActivity = mockk<Activity>(relaxed = true)
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { mockedWorkActivity.activity() } returns "work"
        every { mockedWorkActivity.duration() } returns 5.0.toDuration()
        val clan = ActorFactory.testActor(listOf(mockedWorkActivity))
        every { mockedWorkActivity.act(clan) } returns true
        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("rest", 5.0)
        clan.conditions.add("sick")

        decisionEngine.process(clan)

        verify { mockedWorkActivity.triggerUrges() }
        verify { mockedWorkActivity.blockerConditions() }
        verify(inverse = true) { mockedWorkActivity.act(clan) }
    }

    @Test
    fun `should execute activity if trigger urge is the top urge`() {
        val mockedWorkActivity = mockk<Activity>()
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { mockedWorkActivity.activity() } returns "work"
        every { mockedWorkActivity.duration() } returns 5.0.toDuration()
        val clan = ActorFactory.testActor(listOf(mockedWorkActivity))
        every { mockedWorkActivity.act(clan) } returns true
        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("rest", 5.0)

        decisionEngine.process(clan)

        verify { mockedWorkActivity.triggerUrges() }
        verify { mockedWorkActivity.blockerConditions() }
        verify { mockedWorkActivity.act(clan) }
    }
}
