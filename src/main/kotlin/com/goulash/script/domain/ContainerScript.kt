package com.goulash.script.domain

import com.goulash.core.domain.Container
import com.goulash.core.logic.Logic

class ContainerScript(
    val name: String,
    private val containerLogic: (context: Container) -> Unit,
    private val initLogic: (context: Container) -> Unit
) : Logic<Container> {

    fun init(context: Container) {
        initLogic(context)
    }

    override fun process(context: Container) {
        this.containerLogic(context)
    }
}
