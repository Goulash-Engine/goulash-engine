package com.goulash.simulation

import com.goulash.core.ActivityRunner
import com.goulash.core.ActivitySelector
import com.goulash.core.domain.Container
import com.goulash.script.loader.ScriptLoader
import org.slf4j.LoggerFactory

/**
 * Responsible for the tick execution of all given containers.
 */
class ContainerRunner(
    private val activitySelector: ActivitySelector = ActivitySelector(),
    private val activityRunner: ActivityRunner = ActivityRunner()
) {
    private val containers: MutableList<Container> = mutableListOf()

    fun register(container: Container) {
        LOG.trace("Registering container ${container.id}")
        containers.add(container)
        ScriptLoader.containerScripts.forEach { containerScript ->
            LOG.trace("Initializing container script for newly registered container ${container.id}")
            containerScript.init(container)
        }
    }

    fun tick() {
        LOG.trace("Tick container")
        applyContainerScript()
        containers.forEach { container ->
            container.mutateActors { actors ->
                actors.forEach { actor ->
                    if (activityRunner.hasEnded(actor)) {
                        val activity = activitySelector.select(actor)
                        if (activity != null) {
                            activity.init(actor)
                            activityRunner.start(actor, activity)
                        }
                    } else {
                        activityRunner.`continue`(actor)
                    }
                }
            }
        }
    }

    private fun applyContainerScript() {
        ScriptLoader.containerScripts.forEach { containerScript ->
            containers.forEach { container ->
                LOG.trace("Initializing container script for $container")
                containerScript.process(container)
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("ContainerRunner")
    }
}
