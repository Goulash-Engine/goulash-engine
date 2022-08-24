package com.goulash.simulation

import com.goulash.core.ActivityManager
import com.goulash.core.domain.Container
import com.goulash.script.extension.ActivityExtensions.createRunner
import com.goulash.script.loader.ScriptLoader
import org.slf4j.LoggerFactory

/**
 * Responsible for the tick execution of all given containers.
 */
class ContainerRunner(
    private val activityManager: ActivityManager = ActivityManager()
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
                    if (actor.activityRunner.isRunning()) {
                        actor.tick()
                    } else {
                        val activity = activityManager.resolve(actor)
                        if (activity != null) {
                            actor.activityRunner = activity.createRunner()
                            actor.tick()
                        }
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
