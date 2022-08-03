package com.barbarus.prosper.script.domain

import com.barbarus.prosper.core.domain.Container
import com.barbarus.prosper.core.logic.Logic

internal class ContainerScript(
    val name: String,
    private val containerLogic: (context: Container) -> Unit
) : Logic<Container> {

    override fun process(context: Container) {
        this.containerLogic(context)
    }
}
