package com.goulash.core

import com.goulash.core.activity.Activity
import com.goulash.core.extension.toDuration
import com.goulash.factory.BaseActorFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test

internal class ActivityRunnerTest {
    private val activityRunner = ActivityRunner()

    @Test
    fun `should only abort for a separate actor`() {
        val testActor = BaseActorFactory.testActor()
        val testActor2 = BaseActorFactory.testActor()
        val mockedActivity: Activity = mockk(relaxed = true)
        every { mockedActivity.duration() } returns 3.0.toDuration()
        every { mockedActivity.act(testActor) } returns true
        every { mockedActivity.act(testActor2) } returnsMany listOf(true, false)

        activityRunner.start(testActor, mockedActivity)
        activityRunner.start(testActor2, mockedActivity)
        repeat(2) { activityRunner.`continue`(testActor) }
        repeat(2) { activityRunner.`continue`(testActor2) }

        verify(atMost = 3) { mockedActivity.act(testActor) }
        verify(atMost = 1) { mockedActivity.onFinish(testActor) }
        verify(atMost = 2) { mockedActivity.act(testActor2) }
        verify(atMost = 1) { mockedActivity.onAbort(testActor2) }
    }

    @Test
    fun `should separately count down duration of activity for every actor`() {
        val testActor = BaseActorFactory.testActor()
        val testActor2 = BaseActorFactory.testActor()
        val mockedActivity: Activity = mockk(relaxed = true)
        every { mockedActivity.duration() } returns 3.0.toDuration()
        every { mockedActivity.act(testActor) } returns true
        every { mockedActivity.act(testActor2) } returns true

        activityRunner.start(testActor, mockedActivity)
        activityRunner.start(testActor2, mockedActivity)
        repeat(2) { activityRunner.`continue`(testActor) }
        repeat(2) { activityRunner.`continue`(testActor2) }

        verify(atMost = 3) { mockedActivity.act(testActor) }
        verify(atMost = 1) { mockedActivity.onFinish(testActor) }
        verify(atMost = 3) { mockedActivity.act(testActor2) }
        verify(atMost = 1) { mockedActivity.onFinish(testActor2) }
    }

    @Test
    fun `should not run if requirement exists`() {
        val testActor = BaseActorFactory.testActor()
        val mockedActivity: Activity = mockk(relaxed = true)
        every { mockedActivity.duration() } returns 3.0.toDuration()
        every { mockedActivity.requirements() } returns mutableListOf("food")
        every { mockedActivity.act(testActor) } returns true

        activityRunner.start(testActor, mockedActivity)
        activityRunner.`continue`(testActor)

        verify(inverse = true) { mockedActivity.act(testActor) }
    }

    @Test
    fun `should call on abort logic when actor has an aborting condition`() {
        val testActor = BaseActorFactory.testActor()
        val mockedActivity: Activity = mockk(relaxed = true)
        every { mockedActivity.duration() } returns 3.0.toDuration()
        every { mockedActivity.abortConditions() } returns listOf("dead")
        every { mockedActivity.act(testActor) } returns true

        activityRunner.start(testActor, mockedActivity)
        testActor.conditions.add("dead")
        repeat(2) { activityRunner.`continue`(testActor) }

        verifyOrder {
            mockedActivity.act(testActor)
            mockedActivity.onAbort(testActor)
        }
        verify(inverse = true) { mockedActivity.onFinish(testActor) }
        verify(atMost = 1) { mockedActivity.act(testActor) }
    }

    @Test
    fun `should call on abort logic when 3 runs happen but at the second act will abort`() {
        val testActor = BaseActorFactory.testActor()
        val mockedActivity: Activity = mockk(relaxed = true)
        every { mockedActivity.duration() } returns 3.0.toDuration()
        every { mockedActivity.act(testActor) } returnsMany listOf(true, false, true)

        activityRunner.start(testActor, mockedActivity)
        repeat(2) { activityRunner.`continue`(testActor) }

        verifyOrder {
            mockedActivity.act(testActor)
            mockedActivity.act(testActor)
            mockedActivity.onAbort(testActor)
        }
        verify(inverse = true) { mockedActivity.onFinish(testActor) }
        verify(atMost = 2) { mockedActivity.act(testActor) }
    }

    @Test
    fun `should call on finish logic when 3 runs happen for a duration of 3`() {
        val testActor = BaseActorFactory.testActor()
        val mockedActivity: Activity = mockk(relaxed = true)
        every { mockedActivity.duration() } returns 3.0.toDuration()
        every { mockedActivity.act(testActor) } returns true

        activityRunner.start(testActor, mockedActivity)
        repeat(2) { activityRunner.`continue`(testActor) }

        verifyOrder {
            mockedActivity.act(testActor)
            mockedActivity.act(testActor)
            mockedActivity.onFinish(testActor)
        }
    }

    @Test
    fun `should never act on activity with duration of 0`() {
        val testActor = BaseActorFactory.testActor()
        val mockedActivity: Activity = mockk(relaxed = true)
        every { mockedActivity.duration() } returns 0.0.toDuration()
        every { mockedActivity.act(testActor) } returns true

        activityRunner.start(testActor, mockedActivity)

        verify(inverse = true) { mockedActivity.act(testActor) }
    }

    @Test
    fun `should act on activity`() {
        val testActor = BaseActorFactory.testActor()
        val mockedActivity: Activity = mockk(relaxed = true)
        every { mockedActivity.duration() } returns 5.0.toDuration()
        every { mockedActivity.act(testActor) } returns true

        activityRunner.start(testActor, mockedActivity)

        verify { mockedActivity.act(testActor) }
        verify(inverse = true) { mockedActivity.onAbort(testActor) }
    }
}
