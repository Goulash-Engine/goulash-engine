package com.goulash.api.http

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.goulash.api.http.response.SimulationStatus
import com.goulash.core.SimulationHolder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import java.util.concurrent.Executors

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ManualSimulationHttpApiTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @AfterEach
    fun tearDown() {
        SimulationHolder.simulation = null
    }

    @Test
    fun `should tick simulation`() {
        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            testRestTemplate.postForEntity("/simulation/manual/start", Any::class.java, Any::class.java)
        }
        Thread.sleep(500)
        testRestTemplate.postForEntity("/simulation/manual/tick", Any::class.java, Any::class.java)

        val simulationStatus = testRestTemplate.getForObject("/simulation/status", SimulationStatus::class.java)
        assertThat(simulationStatus.ticks).isEqualTo(1)
    }

    @Test
    fun `should start simulation`() {
        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            testRestTemplate.postForEntity("/simulation/manual/start", Any::class.java, Any::class.java)
        }
        Thread.sleep(500)

        val simulationStatus = testRestTemplate.getForObject("/simulation/status", SimulationStatus::class.java)
        assertThat(simulationStatus.status).isEqualTo("manual")
    }

    @Test
    fun `should check status for not running simulation`() {
        val simulationStatus = testRestTemplate.getForObject("/simulation/status", SimulationStatus::class.java)

        assertThat(simulationStatus.status).isEqualTo("not running")
    }
}
