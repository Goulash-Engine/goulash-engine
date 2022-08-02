package com.barbarus.prosper.script.domain

import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.domain.Actor

internal data class ParsedActivityContext(
    val activity: String,
    val triggerUrges: List<String>,
    val blockerConditions: List<String>,
    val abortConditions: List<String>,
    val priorityConditions: List<String>,
    val priority: Int,
    val duration: Duration,

    val actLogic: (actor: Actor) -> Boolean,
    val onFinishLogic: (actor: Actor) -> Unit,
    val onAbortLogic: (actor: Actor) -> Unit
)
