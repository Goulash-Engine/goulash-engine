package com.goulash.actor.activity

import com.goulash.core.activity.Activity

/**
 * This [Activity] is meant to be a placeholder for non-functional activities.
 */
class IdleActivity : Activity {
    override fun activity(): String {
        return "idle"
    }
}
