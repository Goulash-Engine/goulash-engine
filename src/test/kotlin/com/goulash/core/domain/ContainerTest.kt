package com.goulash.core.domain

internal class ContainerTest {

//    @BeforeEach
//    fun setup() {
//        ScriptLoader.resetLoader()
//    }
//
//    @Test
//    fun `should not resolve an activity if there is one still running`() {
//        val actorMock: Actor = mockk(relaxed = true)
//        val activityManagerMock: ActivityManager = mockk(relaxed = true)
//        val activityRunnerMock: ActivityRunner = mockk(relaxed = true)
//        every { activityManagerMock.resolve(actorMock) } returns mockk()
//        every { activityRunnerMock.isRunning() } returns true
//        every { actorMock.activityRunner } returns activityRunnerMock
//        val container = Container(actors = mutableListOf(actorMock), activityManager = activityManagerMock)
//
//        container.tick()
//
//        verifyOrder {
//            activityRunnerMock.isRunning()
//            actorMock.tick()
//        }
//        verify(inverse = true) { activityManagerMock.resolve(actorMock) }
//    }
//
//    @Test
//    fun `should run the activity manager for every actor in a container on every tick`() {
//        val actorMock: Actor = mockk(relaxed = true)
//        val activityManagerMock: ActivityManager = mockk(relaxed = true)
//        val activityRunnerMock: ActivityRunner = mockk(relaxed = true)
//        val container = Container(actors = mutableListOf(actorMock), activityManager = activityManagerMock)
//
//        container.tick()
//
//        verifyOrder {
//            activityManagerMock.resolve(actorMock)
//            actorMock.tick()
//        }
//    }
//
//    @Test
//    fun `should init container logic`(@TempDir tempDir: java.io.File) {
//        val actors: MutableList<Actor> = mutableListOf(BaseActorFactory.testActor())
//        val config = tempDir.resolve("logic.gsh")
//        config.writeText(
//            """
//            container myfoo {
//                logic container {
//                    actors::urge(eat).plus(1);
//                }
//                logic init {
//                    actors::state(health).set(20);
//                }
//            }
//            """.trimIndent()
//        )
//        ScriptLoader.loadContainerScripts(tempDir.path)
//        Container(actors = actors)
//
//        assertThat(actors[0].state["health"]).isEqualTo(20.0)
//    }
//
//    @Test
//    fun `should invoke scripted logic`(@TempDir tempDir: java.io.File) {
//        val actors: MutableList<Actor> = mutableListOf(BaseActorFactory.testActor())
//        val config = tempDir.resolve("logic.gsh")
//        config.writeText(
//            """
//            container myfoo {
//                logic container {
//                    actors::urge(eat).plus(1);
//                }
//            }
//            """.trimIndent()
//        )
//        ScriptLoader.loadContainerScripts(tempDir.path)
//        val container = Container(actors = actors)
//
//        container.tick()
//
//        assertThat(actors.first().urges.getUrgeOrNull("eat")).isEqualTo(1.0)
//    }
}
