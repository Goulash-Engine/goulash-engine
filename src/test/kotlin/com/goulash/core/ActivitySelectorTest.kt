package com.goulash.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor
import com.goulash.core.extension.toDuration
import com.goulash.factory.BaseActorFactory
import com.goulash.script.loader.ScriptLoader
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class ActivitySelectorTest {
    private val activitySelector = ActivitySelector()

    @Test
    fun `should not execute a wildcard activity if there is an urge activity present`() {
        val idleActivity = mockk<Activity>("idle", relaxed = true)
        every { idleActivity.triggerUrges() } returns listOf("*")
        every { idleActivity.duration() } returns 1.0.toDuration()

        val urgeActivity = mockk<Activity>("eat", relaxed = true)
        every { urgeActivity.triggerUrges() } returns listOf("eat")
        every { urgeActivity.duration() } returns 10.0.toDuration()

        val actor = BaseActorFactory.testActor(listOf(idleActivity, urgeActivity))
        actor.urges.increaseUrge("eat", 100.0)
        actor.conditions.add("underfed")

        repeat(10) { activitySelector.select(actor) }

        verify(inverse = true) { idleActivity.act(any()) }
    }

    @Test
    fun `should prioritize an activity depending on it_s priority value`() {
        val prioritizedActivity = mockk<Activity>("prio", relaxed = true)
        every { prioritizedActivity.triggerUrges() } returns listOf("sleep")
        every { prioritizedActivity.priority() } returns 1

        val normalActivity = mockk<Activity>("normal", relaxed = true)
        every { normalActivity.triggerUrges() } returns listOf("eat")
        every { normalActivity.priority() } returns Int.MAX_VALUE

        val actor = BaseActorFactory.testActor(listOf(prioritizedActivity, normalActivity))
        actor.urges.increaseUrge("eat", 100.0)
        actor.urges.increaseUrge("sleep", 100.0)

        assertThat(activitySelector.select(actor)).isEqualTo(prioritizedActivity)
        actor.urges.decreaseUrge("eat", 50.0)
        assertThat(activitySelector.select(actor)).isEqualTo(prioritizedActivity)
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

        val actor = BaseActorFactory.testActor(listOf(prioritizedActivity, normalActivity))
        actor.conditions.add("panic")
        actor.urges.increaseUrge("eat", 100.0)
        actor.urges.increaseUrge("think", 20.0)

        assertThat(activitySelector.select(actor)).isEqualTo(prioritizedActivity)
        assertThat(activitySelector.select(actor)).isEqualTo(prioritizedActivity)
    }

    @Test
    fun `should not run any activity if global blocking condition is met`() {
        val mockedWorkActivity = mockk<Activity>("workMock", relaxed = true)
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.duration() } returns 5.0.toDuration()
        every { mockedWorkActivity.abortConditions() } returns listOf("starving")
        val actor = BaseActorFactory.testActor(listOf(mockedWorkActivity))
        actor.urges.increaseUrge("work", 100.0)
        ScriptLoader.globalBlockingConditions = listOf("starving")
        actor.conditions.add(ScriptLoader.globalBlockingConditions!!.first())

        activitySelector.select(actor)

        verify(inverse = true) { mockedWorkActivity.act(any()) }
    }

    @Test
    fun `should execute an activity with higher urge for duration and then the next activity`() {
        val primaryActivity = mockk<Activity>(name = "primary", relaxed = true)
        every { primaryActivity.triggerUrges() } returns listOf("work")
        every { primaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")

        val secondaryActivity = mockk<Activity>(name = "secondary", relaxed = true)
        every { secondaryActivity.triggerUrges() } returns listOf("rest")
        every { secondaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")

        val actor = BaseActorFactory.testActor(listOf(primaryActivity, secondaryActivity))

        actor.urges.increaseUrge("work", 10.0)
        actor.urges.increaseUrge("rest", 9.0)

        assertThat(activitySelector.select(actor)).isEqualTo(primaryActivity)
        actor.urges.decreaseUrge("work", 5.0)
        activitySelector.select(actor)
        assertThat(activitySelector.select(actor)).isEqualTo(secondaryActivity)
        activitySelector.select(actor)
        assertThat(activitySelector.select(actor)).isEqualTo(secondaryActivity)
    }

    @Test
    fun `should set current activity of a context`() {
        val firstActivity = mockk<Activity>(relaxed = true)
        every { firstActivity.triggerUrges() } returns listOf("work")
        every { firstActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        val actor = BaseActorFactory.testActor(listOf(firstActivity))
        actor.urges.increaseUrge("work", 10.0)

        val activity = activitySelector.select(actor)

        assertThat(activity).isEqualTo(firstActivity)
    }

    @Test
    fun `should return wildcard activity without a matching urge`() {
        val mockedIdleActivity = mockk<Activity>("idle", relaxed = true)
        every { mockedIdleActivity.triggerUrges() } returns listOf("*")
        every { mockedIdleActivity.blockerConditions() } returns listOf()
        val actor = BaseActorFactory.testActor(listOf(mockedIdleActivity))
        actor.urges.increaseUrge("work", 100.0)
        every { mockedIdleActivity.act(any()) } returns true

        activitySelector.select(actor)

        assertThat(activitySelector.select(actor)).isEqualTo(mockedIdleActivity)
    }

    @Test
    fun `should chose the first of two equally urgent urges`() {
        val mockedWorkActivity = mockk<Activity>(relaxed = true)
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        val actor = BaseActorFactory.testActor(listOf(mockedWorkActivity))
        actor.urges.increaseUrge("work", 10.0)
        actor.urges.increaseUrge("rest", 10.0)

        val activity = activitySelector.select(actor)

        assertThat(activity).isNotNull()
        assertThat(activity).isEqualTo(mockedWorkActivity)
    }

    @Test
    fun `should not return activity if trigger urge is the top urge but blocking condition exists`() {
        val mockedWorkActivity = mockk<Activity>(relaxed = true)
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        val clan = BaseActorFactory.testActor(listOf(mockedWorkActivity))
        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("rest", 5.0)
        clan.conditions.add("sick")

        val activity = activitySelector.select(clan)

        assertThat(activity).isNull()
    }

    @Test
    fun `should return activity if trigger urge is the top urge`() {
        val mockedWorkActivity = mockk<Activity>(relaxed = true)
        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
        every { mockedWorkActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
        every { mockedWorkActivity.activity() } returns "work"
        every { mockedWorkActivity.duration() } returns 5.0.toDuration()
        val clan = BaseActorFactory.testActor(listOf(mockedWorkActivity))
        every { mockedWorkActivity.act(clan) } returns true
        clan.urges.increaseUrge("work", 10.0)
        clan.urges.increaseUrge("rest", 5.0)

        val activity = activitySelector.select(clan)

        assertThat(activity).isEqualTo(mockedWorkActivity)
    }
}
