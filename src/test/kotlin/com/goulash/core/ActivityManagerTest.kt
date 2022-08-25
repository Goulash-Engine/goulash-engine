package com.goulash.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor
import com.goulash.core.extension.toDuration
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class ActivityManagerTest {
    private val activityManager: ActivityManager = ActivityManager()

    @Test
    fun `should set abort state for activity`() {
        val actor: Actor = mockk(relaxed = true)
        val activity: Activity = mockk(relaxed = true)
        every { activity.duration() } returns 10.0.toDuration()

        activityManager.setActivity(actor, activity)
        activityManager.abortActivity(actor)
        val aborted = activityManager.isAborted(actor)

        assertThat(aborted).isTrue()
    }

    @Test
    fun `should count down duration of activity`() {
        val actor: Actor = mockk(relaxed = true)
        val activity: Activity = mockk(relaxed = true)
        every { activity.duration() } returns 10.0.toDuration()

        activityManager.setActivity(actor, activity)
        activityManager.countDown(actor)
        val duration = activityManager.getDuration(actor)

        assertThat(duration).isEqualTo(9.0)
    }

    @Test
    fun `should set duration of set activity`() {
        val actor: Actor = mockk(relaxed = true)
        val activity: Activity = mockk(relaxed = true)
        every { activity.duration() } returns 10.0.toDuration()

        activityManager.setActivity(actor, activity)
        val duration = activityManager.getDuration(actor)

        assertThat(duration).isEqualTo(10.0)
    }

    @Test
    fun `should set and retrieve an actor activity`() {
        val actor: Actor = mockk(relaxed = true)
        val activity: Activity = mockk(relaxed = true)

        activityManager.setActivity(actor, activity)
        val actual = activityManager.getActivity(actor)

        assertThat(actual).isNotNull()
        assertThat(actual).isEqualTo(activity)
    }

}
