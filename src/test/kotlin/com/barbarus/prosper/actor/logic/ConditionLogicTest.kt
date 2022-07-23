package com.barbarus.prosper.actor.logic

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import com.barbarus.prosper.factories.ClanFactory
import org.junit.jupiter.api.Test

internal class ConditionLogicTest {
    private val conditionLogic = ConditionLogic()

    @Test
    fun `should give dead condition if health is below 0`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.health = -0.1

        conditionLogic.process(clan)

        assertThat(clan.conditions).contains("dead")
    }

    @Test
    fun `should make remove malnourished condition if eat urge is below 100`() {
        val clan = ClanFactory.poorGathererClan()
        clan.urges.increaseUrge("eat", 99.0)
        clan.conditions.add("malnourished")

        conditionLogic.process(clan)

        assertThat(clan.conditions).doesNotContain("malnourished")
    }

    @Test
    fun `should make actor malnourished if eat urge is 100`() {
        val clan = ClanFactory.poorGathererClan()
        clan.urges.increaseUrge("eat", 100.0)

        conditionLogic.process(clan)

        assertThat(clan.conditions).contains("malnourished")
    }
}
