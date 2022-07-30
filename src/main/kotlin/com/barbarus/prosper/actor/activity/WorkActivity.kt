package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.DemoActor
import com.barbarus.prosper.core.domain.ProfessionType
import com.barbarus.prosper.core.extension.toDuration
import com.barbarus.prosper.factories.ResourceFactory
import kotlin.random.Random

/**
 * This [Activity] controls the daily work of a actor.
 */
class WorkActivity : Activity {
    private var progress: Double = 0.0
    override fun triggerUrges(): List<String> {
        return listOf("work")
    }

    override fun blockerConditions(): List<String> {
        return listOf("sick", "exhausted", "starving")
    }

    override fun abortConditions(): List<String> {
        return blockerConditions()
    }

    override fun activity(): String {
        return "working"
    }

    override fun duration(): Duration {
        return 8.times(60).toDuration()
    }

    override fun act(actor: Actor): Boolean {
        actor.urges.increaseUrge("rest", 0.5)
        actor.urges.increaseUrge("eat", 0.3)
        actor.urges.decreaseUrge("work", 1.0)

        this.progress += Random.nextInt(1, 5)
        if (progress >= 100.0) {
            addResource(actor)
            progress = 0.0
        }

        return true
    }

    private fun addResource(actor: Actor) {
        if (actor is DemoActor) {
            val resource = when (actor.primaryProfession.type) {
                ProfessionType.GATHERER -> ResourceFactory.food()
                ProfessionType.WOODWORKER -> ResourceFactory.woodenMaterial()
                ProfessionType.TOOLMAKER -> TODO()
                ProfessionType.HERBALIST -> TODO()
            }
            actor.stash.add(resource)
        }
    }
}
