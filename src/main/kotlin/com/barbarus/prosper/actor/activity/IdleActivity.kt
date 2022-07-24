package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.extension.toDuration

/**
 * This [Activity] is meant to be a placeholder for non-functional activities.
 */
class IdleActivity : Activity {
    override fun activity(): String {
        return "idle"
    }

    override fun duration(): Duration {
        return 0.toDuration()
    }
}
