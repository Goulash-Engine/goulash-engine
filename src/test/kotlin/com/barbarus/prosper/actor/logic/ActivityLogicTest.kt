package com.barbarus.prosper.actor.logic

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.extension.toDuration
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.factories.ResourceFactory
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test

internal class ActivityLogicTest {
    private val activityLogic = ActivityLogic()

    @Test
    fun `should prioritize an activity depending on it_s priority value`() {
        val prioritizedActivity = mockk<Activity>("prio", relaxed = true)
        every { prioritizedActivity.triggerUrges() } returns listOf("sleep")
        every { prioritizedActivity.duration() } returns 1.toDuration()
        every { prioritizedActivity.priority() } returns 1
        every { prioritizedActivity.act(any()) } answers { firstArg<Actor>().urges.decreaseUrge("sleep", 50.0) }

        val normalActivity = mockk<Activity>("normal", relaxed = true)
        every { normalActivity.triggerUrges() } returns listOf("eat")
        every { normalActivity.duration() } returns 1.toDuration()
        every { normalActivity.priority() } returns Int.MAX_VALUE

        val clan = ClanFactory.testClan(listOf(prioritizedActivity, normalActivity))
        clan.urges.increaseUrge("eat", 100.0)
        clan.urges.increaseUrge("sleep", 100.0)

        repeat(2) { activityLogic.process(clan) }

        verifyOrder() {
            prioritizedActivity.act(any())
            // normalActivity.act(any())
        }
    }

    @Test
    fun `should prioritize an activity if it_s priority condition has been met`() {
        val prioritizedActivity = mockk<Activity>("prio", relaxed = true)
        every { prioritizedActivity.triggerUrges() } returns listOf("think")
        every { prioritizedActivity.duration() } returns 1.toDuration()
        every { prioritizedActivity.priorityConditions() } returns listOf("panic")
        every { prioritizedActivity.act(any()) } answers { firstArg<Actor>().conditions.remove("panic") }

        val normalActivity = mockk<Activity>("normal", relaxed = true)
        every { normalActivity.triggerUrges() } returns listOf("eat")
        every { normalActivity.duration() } returns 1.toDuration()

        val clan = ClanFactory.testClan(listOf(prioritizedActivity, normalActivity))
        clan.conditions.add("panic")
        clan.urges.increaseUrge("eat", 100.0)
        clan.urges.increaseUrge("think", 20.0)

        repeat(2) { activityLogic.process(clan) }

        verifyOrder() {
            prioritizedActivity.act(any())
            normalActivity.act(any())
        }
    }

    @Test
    fun `should not run any activity if global blocking condition is met`() {
        val mockedWorkActivity = mockk<Activity>("workMock", relaxed = true)
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.duration() } returns 5.toDuration()
        every { mockedWorkActivity.abortConditions() } returns listOf("starving")
        val clan = ClanFactory.testClan(listOf(mockedWorkActivity))
        clan.urges.increaseUrge("work", 100.0)
        clan.conditions.add(ConditionLogic.GOBAL_BLOCKING_CONDITION.first())

        activityLogic.process(clan)

        verify(inverse = true) { mockedWorkActivity.act(any()) }
    }

    @Test
    fun `should remove current activity if abort condition has been met`() {
        val mockedWorkActivity = mockk<Activity>("workMock", relaxed = true)
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.duration() } returns 5.toDuration()
        every { mockedWorkActivity.abortConditions() } returns listOf("starving")
        val clan = ClanFactory.testClan(listOf(mockedWorkActivity))
        clan.urges.increaseUrge("work", 100.0)

        repeat(3) { activityLogic.process(clan) }
        clan.conditions.add("starving")
        repeat(2) { activityLogic.process(clan) }

        verify(atMost = 3) { mockedWorkActivity.act(any()) }
    }

    @Test
    fun `should add resource to stash of actor if activity has finished`() {
        val mockedWorkActivity = mockk<Activity>("workMock")
        every { mockedWorkActivity.triggerUrges() } returns listOf("*")
        every { mockedWorkActivity.blockerConditions() } returns listOf("sick")
        every { mockedWorkActivity.activity() } returns "working"
        every { mockedWorkActivity.duration() } returns 1.toDuration()

        val clan = ClanFactory.testClan(listOf(mockedWorkActivity))
        val expectedResource = ResourceFactory.food()
        every { mockedWorkActivity.onFinish(clan) } answers {
            val actor = firstArg<Actor>()
            actor.inventory().add(expectedResource)
        }
        justRun { mockedWorkActivity.act(clan) }

        activityLogic.process(clan)

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
        every { primaryActivity.duration() } returns 2.toDuration()
        justRun { primaryActivity.onFinish(any()) }

        val secondaryActivity = mockk<Activity>(name = "secondary")
        every { secondaryActivity.triggerUrges() } returns listOf("rest")
        every { secondaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { secondaryActivity.activity() } returns "resting"
        every { secondaryActivity.duration() } returns 1.toDuration()
        justRun { secondaryActivity.onFinish(any()) }

        val clan = ClanFactory.testClan(listOf(primaryActivity, secondaryActivity))

        every { primaryActivity.act(clan) } answers { clan.urges.decreaseUrge("work", 5.0) }
        justRun { secondaryActivity.act(clan) }

        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("rest", 9.0)

        activityLogic.process(clan)
        activityLogic.process(clan)
        activityLogic.process(clan)

        verify(exactly = 2) { primaryActivity.act(clan) }
        verify(exactly = 1) { secondaryActivity.act(clan) }
    }

    @Test
    fun `should execute an activity for it's duration value`() {
        val primaryActivity = mockk<Activity>(name = "primary")
        every { primaryActivity.triggerUrges() } returns listOf("work")
        every { primaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { primaryActivity.activity() } returns "working"
        every { primaryActivity.duration() } returns 4.toDuration()
        justRun { primaryActivity.onFinish(any()) }

        val secondaryActivity = mockk<Activity>()
        every { secondaryActivity.triggerUrges() } returns listOf("rest")
        every { secondaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { secondaryActivity.activity() } returns "resting"
        every { secondaryActivity.duration() } returns 2.toDuration()
        justRun { secondaryActivity.onFinish(any()) }

        val clan = ClanFactory.testClan(listOf(primaryActivity, secondaryActivity))

        justRun { primaryActivity.act(clan) }
        justRun { secondaryActivity.act(clan) }

        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("resting", 9.0)

        repeat(4) { activityLogic.process(clan) }

        verify(exactly = 4) { primaryActivity.act(clan) }
    }

    @Test
    fun `should set current activity of a context`() {
        val firstActivity = mockk<Activity>()
        every { firstActivity.triggerUrges() } returns listOf("work")
        every { firstActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { firstActivity.activity() } returns "working"
        every { firstActivity.duration() } returns 2.toDuration()
        val clan = ClanFactory.testClan(listOf(firstActivity))
        justRun { firstActivity.act(clan) }
        clan.urges.increaseUrge("work", 10.0)

        activityLogic.process(clan)

        assertThat(clan.currentActivity).isEqualTo(firstActivity.activity())
    }

    @Test
    fun `should execute wildcard activities without a matching urge`() {
        val mockedIdleActivity = mockk<Activity>("idle")
        every { mockedIdleActivity.triggerUrges() } returns listOf("*")
        every { mockedIdleActivity.blockerConditions() } returns listOf()
        every { mockedIdleActivity.activity() } returns "idle"
        every { mockedIdleActivity.duration() } returns 1.toDuration()
        justRun { mockedIdleActivity.onFinish(any()) }
        val clan = ClanFactory.testClan(listOf(mockedIdleActivity))
        clan.urges.increaseUrge("work", 100.0)
        justRun { mockedIdleActivity.act(any()) }

        activityLogic.process(clan)

        verify { mockedIdleActivity.act(any()) }
    }

    @Test
    fun `should chose the first of two equally urgent urges`() {
        val mockedWorkActivity = mockk<Activity>()
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { mockedWorkActivity.activity() } returns "work"
        every { mockedWorkActivity.duration() } returns 5.toDuration()
        val clan = ClanFactory.testClan(listOf(mockedWorkActivity))
        justRun { mockedWorkActivity.act(clan) }
        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("rest", 10.0)

        activityLogic.process(clan)

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
        every { mockedWorkActivity.duration() } returns 5.toDuration()
        val clan = ClanFactory.testClan(listOf(mockedWorkActivity))
        justRun { mockedWorkActivity.act(clan) }
        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("rest", 5.0)
        clan.conditions.add("sick")

        activityLogic.process(clan)

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
        every { mockedWorkActivity.duration() } returns 5.toDuration()
        val clan = ClanFactory.testClan(listOf(mockedWorkActivity))
        justRun { mockedWorkActivity.act(clan) }
        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("rest", 5.0)

        activityLogic.process(clan)

        verify { mockedWorkActivity.triggerUrges() }
        verify { mockedWorkActivity.blockerConditions() }
        verify { mockedWorkActivity.act(clan) }
    }
}
