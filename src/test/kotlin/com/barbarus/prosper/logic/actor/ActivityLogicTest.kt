package com.barbarus.prosper.logic.actor

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.actor.activity.Activity
import com.barbarus.prosper.actor.logic.ActivityLogic
import com.barbarus.prosper.core.exceptions.ActivityRedundancyException
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ActivityLogicTest {

    private val activityLogic = ActivityLogic()

    @Test
    fun `should set current activity of a context`() {
        val firstActivity = mockk<Activity>()
        every { firstActivity.triggerUrge() } returns listOf("work")
        every { firstActivity.blockerCondition() } returns listOf("tired", "sick", "exhausted")
        every { firstActivity.activity() } returns "working"
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
        every { firstActivity.triggerUrge() } returns listOf("work")
        every { firstActivity.blockerCondition() } returns listOf("tired", "sick", "exhausted")
        every { secondActivity.triggerUrge() } returns listOf("work")
        every { secondActivity.blockerCondition() } returns listOf("tired", "sick", "exhausted")
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
        every { mockedIdleActivity.triggerUrge() } returns listOf("*")
        every { mockedIdleActivity.blockerCondition() } returns listOf("sick")
        every { mockedIdleActivity.activity() } returns "idle"
        val clan = ClanFactory.testClan(listOf(mockedIdleActivity))
        justRun { mockedIdleActivity.act(clan) }

        activityLogic.process(clan)

        verify { mockedIdleActivity.triggerUrge() }
        verify { mockedIdleActivity.blockerCondition() }
        verify { mockedIdleActivity.act(clan) }
    }

    @Test
    fun `should do nothing is no urges exist`() {
        val mockedWorkActivity = mockk<Activity>()
        every { mockedWorkActivity.triggerUrge() } returns listOf("work")
        every { mockedWorkActivity.blockerCondition() } returns listOf("tired", "sick", "exhausted")
        val clan = ClanFactory.testClan(listOf(mockedWorkActivity))
        justRun { mockedWorkActivity.act(clan) }

        activityLogic.process(clan)

        verify { mockedWorkActivity.triggerUrge() }
        verify(inverse = true) { mockedWorkActivity.blockerCondition() }
        verify(inverse = true) { mockedWorkActivity.act(clan) }
    }

    @Test
    fun `should chose the first of two equally urgent urges`() {
        val mockedWorkActivity = mockk<Activity>()
        every { mockedWorkActivity.triggerUrge() } returns listOf("work")
        every { mockedWorkActivity.blockerCondition() } returns listOf("tired", "sick", "exhausted")
        every { mockedWorkActivity.activity() } returns "work"
        val clan = ClanFactory.testClan(listOf(mockedWorkActivity))
        justRun { mockedWorkActivity.act(clan) }
        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("rest", 10.0)

        activityLogic.process(clan)

        verify { mockedWorkActivity.triggerUrge() }
        verify { mockedWorkActivity.blockerCondition() }
        verify { mockedWorkActivity.act(clan) }
    }

    @Test
    fun `should not execute activity if trigger urge is the top urge but blocking condition exists`() {
        val mockedWorkActivity = mockk<Activity>()
        every { mockedWorkActivity.triggerUrge() } returns listOf("work")
        every { mockedWorkActivity.blockerCondition() } returns listOf("tired", "sick", "exhausted")
        every { mockedWorkActivity.activity() } returns "work"
        val clan = ClanFactory.testClan(listOf(mockedWorkActivity))
        justRun { mockedWorkActivity.act(clan) }
        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("rest", 5.0)
        clan.conditions.add("sick")

        activityLogic.process(clan)

        verify { mockedWorkActivity.triggerUrge() }
        verify { mockedWorkActivity.blockerCondition() }
        verify(inverse = true) { mockedWorkActivity.act(clan) }
    }

    @Test
    fun `should execute activity if trigger urge is the top urge`() {
        val mockedWorkActivity = mockk<Activity>()
        every { mockedWorkActivity.triggerUrge() } returns listOf("work")
        every { mockedWorkActivity.blockerCondition() } returns listOf("tired", "sick", "exhausted")
        every { mockedWorkActivity.activity() } returns "work"
        val clan = ClanFactory.testClan(listOf(mockedWorkActivity))
        justRun { mockedWorkActivity.act(clan) }
        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("rest", 5.0)

        activityLogic.process(clan)

        verify { mockedWorkActivity.triggerUrge() }
        verify { mockedWorkActivity.blockerCondition() }
        verify { mockedWorkActivity.act(clan) }
    }
}
