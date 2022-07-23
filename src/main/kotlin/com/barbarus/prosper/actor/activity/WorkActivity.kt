package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.Clan
import com.barbarus.prosper.core.domain.ProfessionType
import com.barbarus.prosper.core.extension.toDuration
import com.barbarus.prosper.factories.ResourceFactory

/**
 * This [Activity] controls the daily work of a clan.
 */
class WorkActivity : Activity {
    override fun triggerUrges(): List<String> {
        return listOf("work")
    }

    override fun blockerConditions(): List<String> {
        return listOf("tired", "sick", "exhausted")
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

    override fun onFinish(actor: Actor) {
        if (actor is Clan) {
            val resource = when (actor.primaryProfession.type) {
                ProfessionType.GATHERER -> ResourceFactory.food()
                ProfessionType.WOODWORKER -> ResourceFactory.woodenMaterial()
                ProfessionType.TOOLMAKER -> TODO()
                ProfessionType.HERBALIST -> TODO()
            }
            actor.stash.add(resource)
        }
    }

    override fun act(actor: Actor) {
        actor.urges.increaseUrge("rest", 1.5)
        actor.urges.increaseUrge("eat", 0.3)
        actor.urges.decreaseUrge("work", 1.0)
    }
}
