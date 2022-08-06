package com.goulash.actor.logic

import com.goulash.core.domain.Actor
import com.goulash.core.logic.Logic

/**
 * Changes the state of an [Actor] depending on various factors.
 */
class StateLogic : Logic<Actor> {
    override fun process(context: Actor) {
        if (context.state["nourishment"]!! > 100.00) context.state["nourishment"] = 100.0

        val state = context.state
        when {
            state["nourishment"]!! == 100.0 -> context.state["health"] = context.state["health"]!!.plus(0.50)
            state["nourishment"]!! <= 0.0 -> context.state["health"] = context.state["health"]!!.minus(1)
            state["nourishment"]!! < 20.0 -> context.state["health"] = context.state["health"]!!.minus(0.5)
            state["nourishment"]!! < 60.0 -> context.state["health"] = context.state["health"]!!.minus(0.1)
            state["nourishment"]!! < 100.0 -> context.state["health"] = context.state["health"]!!.plus(0.1)
        }

        if (state["health"]!! > 100) {
            state["health"] = 100.0
        }
    }
}
