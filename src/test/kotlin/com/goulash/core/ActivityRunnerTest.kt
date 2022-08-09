package com.goulash.core

import com.goulash.core.activity.Activity
import com.goulash.core.extension.toDuration
import com.goulash.factory.BaseActorFactory
import com.goulash.script.extension.ActivityExtensions.createRunner
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test

internal class ActivityRunnerTest {

    @Test
    fun `should call on abort logic when actor has an aborting condition`() {
        val testActor = BaseActorFactory.testActor()
        val mockedActivity: Activity = mockk(relaxed = true)
        every { mockedActivity.duration() } returns 3.0.toDuration()
        every { mockedActivity.abortConditions() } returns listOf("dead")
        every { mockedActivity.act(testActor) } returns true
        val runner = mockedActivity.createRunner()

        repeat(1) { runner.run(testActor) }
        testActor.conditions.add("dead")
        repeat(2) { runner.run(testActor) }

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
        val runner = mockedActivity.createRunner()

        repeat(3) { runner.run(testActor) }

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

        val runner = mockedActivity.createRunner()
        repeat(3) { runner.run(testActor) }

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

        val runner = mockedActivity.createRunner()
        runner.run(testActor)

        verify(inverse = true) { mockedActivity.act(testActor) }
    }

    @Test
    fun `should act on activity`() {
        val testActor = BaseActorFactory.testActor()
        val mockedActivity: Activity = mockk(relaxed = true)
        every { mockedActivity.duration() } returns 5.0.toDuration()
        every { mockedActivity.act(testActor) } returns true

        val runner = mockedActivity.createRunner()
        runner.run(testActor)

        verify { mockedActivity.act(testActor) }
        verify(inverse = true) { mockedActivity.onAbort(testActor) }
    }
}
