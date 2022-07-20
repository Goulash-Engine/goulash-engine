package com.barbarus.prosper.logic

import assertk.assertThat
import assertk.assertions.contains
import com.barbarus.prosper.core.domain.Clan
import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.ProfessionType
import com.barbarus.prosper.core.domain.Resource
import com.barbarus.prosper.core.domain.ResourceType
import org.junit.jupiter.api.Test

internal class StashLogicTest {
    private val stashLogic = StashLogic()

    @Test
    fun `should add a new resource to the stash`() {
        val clan = Clan(primaryProfession = Profession(ProfessionType.WOODWORKER, 1.0))
        val resource = Resource(type = ResourceType.WOODEN_MATERIAL, weight = 1.5)

        stashLogic.process(resource, clan)

        assertThat(clan.stash).contains(resource)
    }
}
