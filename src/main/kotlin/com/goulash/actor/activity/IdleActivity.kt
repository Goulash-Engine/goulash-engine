package com.goulash.actor.activity

import com.goulash.core.activity.Activity
import com.goulash.core.activity.Duration
import com.goulash.core.extension.toDuration

/**
 * This [Activity] is meant to be a placeholder for non-functional activities.
 */
class IdleActivity : Activity {
    override fun activity(): String {
        return "idle"
    }

    override fun duration(): Duration {
        return 0.0.toDuration()
    }
}
