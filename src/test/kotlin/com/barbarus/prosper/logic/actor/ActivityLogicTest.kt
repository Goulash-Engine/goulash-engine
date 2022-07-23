package com.barbarus.prosper.logic.actor

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.actor.activity.Activity
import com.barbarus.prosper.actor.logic.ActivityLogic
import com.barbarus.prosper.core.exceptions.ActivityRedundancyException
import com.barbarus.prosper.factories.ClanFactory
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ActivityLogicTest {
    private val activityLogic = ActivityLogic()

    @Test
    fun `should execute an activity with higher urge for duration and then the next activity`() {
        val primaryActivity = mockk<Activity>(name = "primary")
        every { primaryActivity.triggerUrges() } returns listOf("work")
        every { primaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { primaryActivity.activity() } returns "working"
        every { primaryActivity.duration() } returns 2

        val secondaryActivity = mockk<Activity>(name = "secondary")
        every { secondaryActivity.triggerUrges() } returns listOf("rest")
        every { secondaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { secondaryActivity.activity() } returns "resting"
        every { secondaryActivity.duration() } returns 1

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
        every { primaryActivity.duration() } returns 4

        val secondaryActivity = mockk<Activity>()
        every { secondaryActivity.triggerUrges() } returns listOf("rest")
        every { secondaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { secondaryActivity.activity() } returns "resting"
        every { secondaryActivity.duration() } returns 2

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
        every { firstActivity.duration() } returns 2
        val clan = ClanFactory.testClan(listOf(firstActivity))
        justRun { firstActivity.act(clan) }
        clan.urges.increaseUrge("work", 10.0)

        activityLogic.process(clan)

        assertThat(clan.currentActivity).isEqualTo(firstActivity.activity())
    }

    @Test
    fun `should throw exception if more than one activity for the same urge exist`() {
        val firstActivity = mockk<Activity>()
        val secondActivity = mockk<Activity>()
        every { firstActivity.triggerUrges() } returns listOf("work")
        every { firstActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { secondActivity.triggerUrges() } returns listOf("work")
        every { secondActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        val clan = ClanFactory.testClan(listOf(firstActivity, secondActivity))
        justRun { firstActivity.act(clan) }
        justRun { secondActivity.act(clan) }
        clan.urges.increaseUrge("work", 10.0)

        assertThrows<ActivityRedundancyException> {
            activityLogic.process(clan)
        }
    }

    @Test
    fun `should execute activities that always will be executed`() {
        val mockedIdleActivity = mockk<Activity>()
        every { mockedIdleActivity.triggerUrges() } returns listOf("*")
        every { mockedIdleActivity.blockerConditions() } returns listOf("sick")
        every { mockedIdleActivity.activity() } returns "idle"
        every { mockedIdleActivity.duration() } returns 1
        val clan = ClanFactory.testClan(listOf(mockedIdleActivity))
        justRun { mockedIdleActivity.act(clan) }

        activityLogic.process(clan)

        verify { mockedIdleActivity.triggerUrges() }
        verify { mockedIdleActivity.blockerConditions() }
        verify { mockedIdleActivity.act(clan) }
    }

    @Test
    fun `should do nothing is no urges exist`() {
        val mockedWorkActivity = mockk<Activity>()
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { mockedWorkActivity.duration() } returns 5
        val clan = ClanFactory.testClan(listOf(mockedWorkActivity))
        clan.urges.stopUrge("think")
        justRun { mockedWorkActivity.act(clan) }

        activityLogic.process(clan)

        verify { mockedWorkActivity.triggerUrges() }
        verify(inverse = true) { mockedWorkActivity.blockerConditions() }
        verify(inverse = true) { mockedWorkActivity.act(clan) }
    }

    @Test
    fun `should chose the first of two equally urgent urges`() {
        val mockedWorkActivity = mockk<Activity>()
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { mockedWorkActivity.activity() } returns "work"
        every { mockedWorkActivity.duration() } returns 5
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
        val mockedWorkActivity = mockk<Activity>()
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { mockedWorkActivity.activity() } returns "work"
        every { mockedWorkActivity.duration() } returns 5
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
        every { mockedWorkActivity.duration() } returns 5
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
