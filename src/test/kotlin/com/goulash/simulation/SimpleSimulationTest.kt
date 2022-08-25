package com.goulash.simulation

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.goulash.core.ActivityManager
import com.goulash.core.domain.Container
import com.goulash.factory.BaseActorFactory
import com.goulash.script.loader.ScriptLoader
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

class SimpleSimulationTest {

    @Test
    fun `should start eating activity once food requirement has been removed`() {
        val person = BaseActorFactory.newActor("person")
        val container = Container(actors = mutableListOf(person))
        val manualSimulationRunner = ManualSimulationRunner(listOf(container))

        repeat(5) { manualSimulationRunner.tick() }
        assertThat(person.urges.getUrgeOrNull("hunger")).isEqualTo(5.0)
        repeat(5) { manualSimulationRunner.tick() }
        assertThat(person.urges.getUrgeOrNull("hunger")).isNull()
    }

    @Test
    fun `should reset sleep urge to null since sleeping acitivy has finished`() {
        val person = BaseActorFactory.newActor("person")
        val container = Container(actors = mutableListOf(person))
        val manualSimulationRunner = ManualSimulationRunner(listOf(container))

        assertThat(person.state["health"]).isEqualTo(100.0)
        repeat(5) { manualSimulationRunner.tick() }
        assertThat(person.urges.getUrgeOrNull("sleep")).isNull()
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
                        actors::state(health).set(100);
                    }
                    
                    logic container {
                        actors::urge(hunger).plus(1);
                        actors::urge(sleep).plus(5);
                    }
                }
                """.trimIndent()
            )
            ScriptLoader.loadContainerScripts(tempDir.path)
        }

        private fun loadActivityScripts(tempDir: File) {
            val eating = tempDir.resolve("eating.gsh")
            eating.writeText(
                """ 
                    activity eating {
                        trigger_urges ["hunger"]
                        duration ["5"]
                        requirements ["food"]
                        logic on_finish {
                          actor::urge(hunger).set(0);
                        }
                        logic act {
                          actor::urge(hunger).minus(1);
                        }
                    }
                """.trimIndent()
            )
            val sleeping = tempDir.resolve("sleeping.gsh")
            sleeping.writeText(
                """ 
                    activity eating {
                        trigger_urges ["sleep"]
                        duration ["5"]
                        logic on_finish {
                          actor::urge(sleep).set(0);
                        }
                        logic act {
                          actor::urge(sleep).minus(1);
                        }
                    }
                """.trimIndent()
            )
            ScriptLoader.loadActivityScripts(tempDir.path)
        }
    }
}
