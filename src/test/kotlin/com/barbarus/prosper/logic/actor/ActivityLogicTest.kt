package com.barbarus.prosper.logic.actor

import com.barbarus.prosper.ClanFactory
import com.barbarus.prosper.behavior.Activity
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class ActivityLogicTest {

    private val activityLogic = ActivityLogic()

    @Test
    fun `should execute activities that always will be executed`() {
        val mockedIdleActivity = mockk<Activity>()
        every { mockedIdleActivity.triggerUrge() } returns listOf("*")
        every { mockedIdleActivity.blockerCondition() } returns listOf("sick")
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
