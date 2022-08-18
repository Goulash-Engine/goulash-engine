package com.goulash.actor.logic

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.goulash.core.ActivityManager
import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor
import com.goulash.core.extension.toDuration
import com.goulash.factory.BaseActorFactory
import com.goulash.script.domain.ActivityScript
import com.goulash.script.loader.ScriptLoader
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test

internal class ActivityManagerTest {
    private val activityManager = ActivityManager()

//    @Test
//    fun `should call activity init logic once when activity has started`() {
//        val scriptedSleepActivity = spyk(
//            ActivityScript(
//                "sleeping",
//                mapOf(
//                    "trigger_urges" to listOf("sleep"),
//                    "duration" to listOf("5")
//                ),
//                { _ -> },
//                { _ -> true },
//                { _ -> },
//                { _ -> }
//            )
//        )
//
//        val testActor = BaseActorFactory.testActor(listOf(scriptedSleepActivity))
//        testActor.urges.increaseUrge("sleep", 10.0)
//
//        repeat(2) { activityManager.resolve(testActor, onResolve = any()) }
//
//        verify(exactly = 1) {
//            scriptedSleepActivity.init(any())
//        }
//    }
//
//    @Test
//    fun `should not apply same activity if its already the current one`() {
//        val exitingActivity = mockk<Activity>("foo", relaxed = true)
//        every { exitingActivity.triggerUrges() } returns listOf("foo")
//        every { exitingActivity.duration() } returns 5.0.toDuration()
//        every { exitingActivity.act(any()) } returns true
//
//        val testActor = BaseActorFactory.testActor(listOf(exitingActivity))
//        testActor.urges.increaseUrge("foo", 100.0)
//
//        repeat(10) { activityManager.resolve(testActor, onResolve = any()) }
//
//        verify(atMost = 10) { exitingActivity.act(any()) }
//    }
//
//    @Test
//    fun `should not execute a wildcard activity if there is an urge activity present`() {
//        val idleActivity = mockk<Activity>("idle", relaxed = true)
//        every { idleActivity.triggerUrges() } returns listOf("*")
//        every { idleActivity.duration() } returns 1.0.toDuration()
//
//        val urgeActivity = mockk<Activity>("eat", relaxed = true)
//        every { urgeActivity.triggerUrges() } returns listOf("eat")
//        every { urgeActivity.duration() } returns 10.0.toDuration()
//
//        val actor = BaseActorFactory.testActor(listOf(idleActivity, urgeActivity))
//        actor.urges.increaseUrge("eat", 100.0)
//        actor.conditions.add("underfed")
//
//        repeat(10) { activityManager.resolve(actor) }
//
//        verify(inverse = true) { idleActivity.act(any()) }
//    }
//
//    @Test
//    fun `should execute on abort callback if activity is being aborted`() {
//        val testActivity = mockk<Activity>("prio", relaxed = true)
//        every { testActivity.triggerUrges() } returns listOf("sleep")
//        every { testActivity.abortConditions() } returns listOf("stop")
//        every { testActivity.duration() } returns 10.0.toDuration()
//        every { testActivity.act(any()) } returns true
//
//        val actor = BaseActorFactory.testActor(listOf(testActivity))
//        actor.urges.increaseUrge("sleep", 100.0)
//
//        repeat(2) { activityManager.resolve(actor) }
//        actor.conditions.add("stop")
//        repeat(1) { activityManager.resolve(actor) }
//
//        verify(atMost = 2) { testActivity.act(any()) }
//        verify(atMost = 1) { testActivity.onAbort(any()) }
//    }
//
//    @Test
//    fun `should prioritize an activity depending on it_s priority value`() {
//        val prioritizedActivity = mockk<Activity>("prio", relaxed = true)
//        every { prioritizedActivity.triggerUrges() } returns listOf("sleep")
//        every { prioritizedActivity.duration() } returns 1.0.toDuration()
//        every { prioritizedActivity.priority() } returns 1
//        every { prioritizedActivity.act(any()) } answers {
//            firstArg<Actor>().urges.decreaseUrge("sleep", 50.0)
//            true
//        }
//
//        val normalActivity = mockk<Activity>("normal", relaxed = true)
//        every { normalActivity.triggerUrges() } returns listOf("eat")
//        every { normalActivity.duration() } returns 1.0.toDuration()
//        every { normalActivity.priority() } returns Int.MAX_VALUE
//
//        val actor = BaseActorFactory.testActor(listOf(prioritizedActivity, normalActivity))
//        actor.urges.increaseUrge("eat", 100.0)
//        actor.urges.increaseUrge("sleep", 100.0)
//
//        repeat(2) { activityManager.resolve(actor) }
//
//        verifyOrder {
//            prioritizedActivity.act(any())
//            normalActivity.act(any())
//        }
//    }
//
//    @Test
//    fun `should prioritize an activity if it_s priority condition has been met`() {
//        val prioritizedActivity = mockk<Activity>("prio", relaxed = true)
//        every { prioritizedActivity.triggerUrges() } returns listOf("think")
//        every { prioritizedActivity.duration() } returns 1.0.toDuration()
//        every { prioritizedActivity.priorityConditions() } returns listOf("panic")
//        every { prioritizedActivity.act(any()) } answers { firstArg<Actor>().conditions.remove("panic") }
//
//        val normalActivity = mockk<Activity>("normal", relaxed = true)
//        every { normalActivity.triggerUrges() } returns listOf("eat")
//        every { normalActivity.duration() } returns 1.0.toDuration()
//
//        val actor = BaseActorFactory.testActor(listOf(prioritizedActivity, normalActivity))
//        actor.conditions.add("panic")
//        actor.urges.increaseUrge("eat", 100.0)
//        actor.urges.increaseUrge("think", 20.0)
//
//        repeat(2) { activityManager.resolve(actor) }
//
//        verifyOrder {
//            prioritizedActivity.act(any())
//            normalActivity.act(any())
//        }
//    }
//
//    @Test
//    fun `should not run any activity if global blocking condition is met`() {
//        val mockedWorkActivity = mockk<Activity>("workMock", relaxed = true)
//        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
//        every { mockedWorkActivity.duration() } returns 5.0.toDuration()
//        every { mockedWorkActivity.abortConditions() } returns listOf("starving")
//        val actor = BaseActorFactory.testActor(listOf(mockedWorkActivity))
//        actor.urges.increaseUrge("work", 100.0)
//        ScriptLoader.globalBlockingConditions = listOf("starving")
//        actor.conditions.add(ScriptLoader.globalBlockingConditions!!.first())
//
//        activityManager.resolve(actor)
//
//        verify(inverse = true) { mockedWorkActivity.act(any()) }
//    }
//
//    @Test
//    fun `should remove current activity if abort condition has been met`() {
//        val mockedWorkActivity = mockk<Activity>("workMock", relaxed = true)
//        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
//        every { mockedWorkActivity.duration() } returns 5.0.toDuration()
//        every { mockedWorkActivity.abortConditions() } returns listOf("starving")
//        val clan = BaseActorFactory.testActor(listOf(mockedWorkActivity))
//        clan.urges.increaseUrge("work", 100.0)
//
//        repeat(3) { activityManager.resolve(clan) }
//        clan.conditions.add("starving")
//        repeat(2) { activityManager.resolve(clan) }
//
//        verify(atMost = 3) { mockedWorkActivity.act(any()) }
//    }
//
//    @Test
//    fun `should add resource to stash of actor if activity has finished`() {
//        val mockedWorkActivity = mockk<Activity>("workMock", relaxed = true)
//        every { mockedWorkActivity.triggerUrges() } returns listOf("*")
//        every { mockedWorkActivity.blockerConditions() } returns listOf("sick")
//        every { mockedWorkActivity.activity() } returns "working"
//        every { mockedWorkActivity.duration() } returns 1.0.toDuration()
//
//        val clan = BaseActorFactory.testActor(listOf(mockedWorkActivity))
//        every { mockedWorkActivity.act(clan) } returns true
//
//        activityManager.resolve(clan)
//
//        verify { mockedWorkActivity.triggerUrges() }
//        verify { mockedWorkActivity.blockerConditions() }
//        verify { mockedWorkActivity.act(clan) }
//        verify { mockedWorkActivity.onFinish(clan) }
//    }
//
//    @Test
//    fun `should execute an activity with higher urge for duration and then the next activity`() {
//        val primaryActivity = mockk<Activity>(name = "primary", relaxed = true)
//        every { primaryActivity.triggerUrges() } returns listOf("work")
//        every { primaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
//        every { primaryActivity.activity() } returns "working"
//        every { primaryActivity.duration() } returns 2.0.toDuration()
//        justRun { primaryActivity.onFinish(any()) }
//
//        val secondaryActivity = mockk<Activity>(name = "secondary", relaxed = true)
//        every { secondaryActivity.triggerUrges() } returns listOf("rest")
//        every { secondaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
//        every { secondaryActivity.activity() } returns "resting"
//        every { secondaryActivity.duration() } returns 1.0.toDuration()
//        justRun { secondaryActivity.onFinish(any()) }
//
//        val clan = BaseActorFactory.testActor(listOf(primaryActivity, secondaryActivity))
//
//        every { primaryActivity.act(clan) } answers {
//            clan.urges.decreaseUrge("work", 5.0)
//            true
//        }
//        every { secondaryActivity.act(clan) } returns true
//
//        clan.urges.increaseUrge("work", 10.0)
//        clan.urges.increaseUrge("rest", 9.0)
//
//        activityManager.resolve(clan)
//        activityManager.resolve(clan)
//        activityManager.resolve(clan)
//
//        verify(exactly = 2) { primaryActivity.act(clan) }
//        verify(exactly = 1) { secondaryActivity.act(clan) }
//    }
//
//    @Test
//    fun `should execute an activity for it's duration value`() {
//        val primaryActivity = mockk<Activity>(name = "primary", relaxed = true)
//        every { primaryActivity.triggerUrges() } returns listOf("work")
//        every { primaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
//        every { primaryActivity.activity() } returns "working"
//        every { primaryActivity.duration() } returns 4.0.toDuration()
//        justRun { primaryActivity.onFinish(any()) }
//
//        val secondaryActivity = mockk<Activity>(relaxed = true)
//        every { secondaryActivity.triggerUrges() } returns listOf("rest")
//        every { secondaryActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
//        every { secondaryActivity.activity() } returns "resting"
//        every { secondaryActivity.duration() } returns 2.0.toDuration()
//        justRun { secondaryActivity.onFinish(any()) }
//
//        val clan = BaseActorFactory.testActor(listOf(primaryActivity, secondaryActivity))
//
//        every { primaryActivity.act(clan) } returns true
//        every { secondaryActivity.act(clan) } returns true
//
//        clan.urges.increaseUrge("work", 10.0)
//        clan.urges.increaseUrge("resting", 9.0)
//
//        repeat(4) { activityManager.resolve(clan) }
//
//        verify(exactly = 4) { primaryActivity.act(clan) }
//    }
//
//    @Test
//    fun `should set current activity of a context`() {
//        val firstActivity = mockk<Activity>(relaxed = true)
//        every { firstActivity.triggerUrges() } returns listOf("work")
//        every { firstActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
//        every { firstActivity.activity() } returns "working"
//        every { firstActivity.duration() } returns 2.0.toDuration()
//        val clan = BaseActorFactory.testActor(listOf(firstActivity))
//        every { firstActivity.act(clan) } returns true
//        clan.urges.increaseUrge("work", 10.0)
//
//        activityManager.resolve(clan)
//
//        assertThat(clan.currentActivity).isEqualTo(firstActivity.activity())
//    }
//
//    @Test
//    fun `should execute wildcard activities without a matching urge`() {
//        val mockedIdleActivity = mockk<Activity>("idle", relaxed = true)
//        every { mockedIdleActivity.triggerUrges() } returns listOf("*")
//        every { mockedIdleActivity.blockerConditions() } returns listOf()
//        every { mockedIdleActivity.activity() } returns "idle"
//        every { mockedIdleActivity.duration() } returns 1.0.toDuration()
//        justRun { mockedIdleActivity.onFinish(any()) }
//        val clan = BaseActorFactory.testActor(listOf(mockedIdleActivity))
//        clan.urges.increaseUrge("work", 100.0)
//        every { mockedIdleActivity.act(any()) } returns true
//
//        activityManager.resolve(clan)
//
//        verify { mockedIdleActivity.act(any()) }
//    }
//
//    @Test
//    fun `should chose the first of two equally urgent urges`() {
//        val mockedWorkActivity = mockk<Activity>(relaxed = true)
//        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
//        every { mockedWorkActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
//        every { mockedWorkActivity.activity() } returns "work"
//        every { mockedWorkActivity.duration() } returns 5.0.toDuration()
//        val clan = BaseActorFactory.testActor(listOf(mockedWorkActivity))
//        every { mockedWorkActivity.act(clan) } returns true
//        clan.urges.increaseUrge("work", 10.0)
//        clan.urges.increaseUrge("rest", 10.0)
//
//        activityManager.resolve(clan)
//
//        verify { mockedWorkActivity.triggerUrges() }
//        verify { mockedWorkActivity.blockerConditions() }
//        verify { mockedWorkActivity.act(clan) }
//    }
//
//    @Test
//    fun `should not execute activity if trigger urge is the top urge but blocking condition exists`() {
//        val mockedWorkActivity = mockk<Activity>(relaxed = true)
//        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
//        every { mockedWorkActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
//        every { mockedWorkActivity.activity() } returns "work"
//        every { mockedWorkActivity.duration() } returns 5.0.toDuration()
//        val clan = BaseActorFactory.testActor(listOf(mockedWorkActivity))
//        every { mockedWorkActivity.act(clan) } returns true
//        clan.urges.increaseUrge("work", 10.0)
//        clan.urges.increaseUrge("rest", 5.0)
//        clan.conditions.add("sick")
//
//        activityManager.resolve(clan)
//
//        verify { mockedWorkActivity.triggerUrges() }
//        verify { mockedWorkActivity.blockerConditions() }
//        verify(inverse = true) { mockedWorkActivity.act(clan) }
//    }
//
//    @Test
//    fun `should execute activity if trigger urge is the top urge`() {
//        val mockedWorkActivity = mockk<Activity>(relaxed = true)
//        every { mockedWorkActivity.triggerUrges() } returns listOf("work")
//        every { mockedWorkActivity.blockerConditions() } returns listOf("tired", "sick", "exhausted")
//        every { mockedWorkActivity.activity() } returns "work"
//        every { mockedWorkActivity.duration() } returns 5.0.toDuration()
//        val clan = BaseActorFactory.testActor(listOf(mockedWorkActivity))
//        every { mockedWorkActivity.act(clan) } returns true
//        clan.urges.increaseUrge("work", 10.0)
//        clan.urges.increaseUrge("rest", 5.0)
//
//        activityManager.resolve(clan)
//
//        verify { mockedWorkActivity.triggerUrges() }
//        verify { mockedWorkActivity.blockerConditions() }
//        verify { mockedWorkActivity.act(clan) }
//    }
}