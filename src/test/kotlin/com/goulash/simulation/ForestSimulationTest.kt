package com.goulash.simulation

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import com.goulash.core.domain.Actor
import com.goulash.core.domain.Container
import com.goulash.factory.BaseActorFactory
import com.goulash.script.loader.ScriptLoader
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ForestSimulationTest {

    @Test
    fun `should run a simulation of dying trees`() {
        val trees = mutableListOf<Actor>(
            BaseActorFactory.newActor("tree1")
        )
        val container = Container(actors = trees)
        val manualRunner = ManualRunner(listOf(container))

        repeat(110) { manualRunner.tick() }

        assertThat(trees[0].state["hydration"]).isEqualTo(0.0)
        assertThat(trees[0].state["damage"]).isEqualTo(11.0)
        assertThat(trees[0].conditions).contains("dry")
        assertThat(trees[0].urges.getAllUrges()).contains("consume" to 70.0)
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setup(@TempDir containerDir: File, @TempDir scriptDir: File) {
            loadContainerScripts(containerDir)
            loadActivityScripts(scriptDir)
        }

        private fun loadContainerScripts(tempDir: File) {
            val scriptFile = tempDir.resolve("container.gsh")
            scriptFile.writeText(
                """ 
                container routine {
                    logic init {
                        actors::state(hydration).set(100);
                    }
                    
                    logic container {
                        actors::state(hydration).minus(1);
                        
                        actors[state.hydration < 60]::urge(consume).plus(1);
                        actors[state.hydration < 20]::condition(dry).add();
                        actors[state.hydration <= 0]::state(damage).plus(1);
    
                        actors[state.hydration <= 0]::state(hydration).set(0);
                        actors[state.damage < 0]::state(damage).set(0);
    
                        actors[state.damage > 50]::condition(alive).remove();
                        actors[state.damage > 50]::condition(dying).add();
    
                        actors[state.damage >= 100]::condition(dying).remove();
                        actors[state.damage >= 100]::condition(dead).add();
                    }
                }
                """.trimIndent()
            )
            ScriptLoader.loadContainerScripts(tempDir.path)
        }

        private fun loadActivityScripts(tempDir: File) {
            val scriptFile = tempDir.resolve("activity.gsh")
            scriptFile.writeText(
                """ 
                    activity consuming {
                        trigger_urges ["wip"]
                        duration ["5"]
                        logic on_finish {
                          actor::urge(consume).set(0);
                        }
                        logic act {
                          actor::urge(consume).minus(5);
                        }
                    }
                """.trimIndent()
            )
            ScriptLoader.loadActivityScripts(tempDir.path)
        }
    }
}
