package com.goulash.script.extension

import com.goulash.core.ActivityRunner
import com.goulash.core.activity.Activity

object ActivityExtensions {
    fun Activity.createRunner() = ActivityRunner(
        activity = this,
        duration = this.duration().asDouble()
    )
}
