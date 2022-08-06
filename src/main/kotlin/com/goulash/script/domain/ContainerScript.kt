package com.goulash.script.domain

import com.goulash.core.domain.Container
import com.goulash.core.logic.Logic

class ContainerScript(
    val name: String,
    private val containerLogic: (context: Container) -> Unit
) : Logic<Container> {

    override fun process(context: Container) {
        this.containerLogic(context)
    }
}
