package com.barbarus.prosper.script.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.core.domain.State
import com.barbarus.prosper.script.domain.ScriptStatement
import com.barbarus.prosper.script.domain.ScriptedLogic
import com.barbarus.prosper.script.extension.ReflectionExtensions.callSetter
import com.barbarus.prosper.script.extension.TranspilerExtensions.tryScriptFilter
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers

/**
 * Transpiles [ScriptContext] to [ScriptedLogic]
 */
class ScriptTranspiler {
    fun transpile(scriptContext: ScriptContext): ScriptedLogic<Civilisation> {
        return ScriptedLogic<Civilisation>(scriptContext.head.name) { context ->
            val statements = scriptContext.statements
            statements.forEach { statement ->
                if (statement.context == "actors") {
                    if (statement.mutationType == "state") {
                        val actors = context.actors.tryScriptFilter(statement.filter)
                        when (statement.mutationOperation) {
                            "set" -> actors.forEach { set(it, statement.mutationTarget, statement.mutationOperationArgument) }
                            "plus" -> actors.forEach { plus(it, statement.mutationTarget, statement.mutationOperationArgument) }
                            "minus" -> actors.forEach { minus(it, statement.mutationTarget, statement.mutationOperationArgument) }
                        }
                    }
                    if (statement.mutationType == "urge") {
                        val actors = context.actors.tryScriptFilter(statement.filter)
                        when (statement.mutationOperation) {
                            "plus" -> actors.forEach { increaseUrge(it, statement) }
                            "minus" -> actors.forEach { decreaseUrge(it, statement) }
                            "set" -> actors.forEach { setUrge(it, statement) }
                        }
                    }
                }
            }
        }
    }

    private fun minus(actor: Actor, property: String, value: Any) {
        val state: State = getActorState(actor)
        val setter = getStateSetter(property)
        val currentValue = getCurrentValue(state, property)
        when (setter.parameters[SETTER_PARAM].type.toString()) {
            "kotlin.Double" -> {
                val valueDouble = (value as String).toDouble()
                setter.callSetter((currentValue as Double).minus(valueDouble), state)
            }
        }
    }

    private fun plus(actor: Actor, property: String, value: Any) {
        val state: State = getActorState(actor)
        val setter = getStateSetter(property)
        val currentValue = getCurrentValue(state, property)
        when (setter.parameters[SETTER_PARAM].type.toString()) {
            "kotlin.Double" -> {
                val valueDouble = (value as String).toDouble()
                setter.callSetter((currentValue as Double).plus(valueDouble), state)
            }
        }
    }

    private fun getCurrentValue(state: State, property: String): Any? {
        return (State::class.declaredMembers.find { it.name == property } as KMutableProperty).getter.call(state)
    }

    private fun getActorState(actor: Actor): State {
        return Actor::class.declaredMemberProperties.find { it.name == "state" }!!.get(actor) as State
    }

    private fun getStateSetter(property: String): KMutableProperty.Setter<out Any?> {
        val stateProperty = State::class.declaredMembers.find { it.name == property } as KMutableProperty
        return stateProperty.setter
    }

    private fun set(actor: Actor, property: String, value: Any) {
        val state: State = Actor::class.declaredMemberProperties.find { it.name == "state" }!!.get(actor) as State
        val stateProperty = State::class.declaredMembers.find { it.name == property } as KMutableProperty
        val setter = stateProperty.setter
        when (setter.parameters[SETTER_PARAM].type.toString()) {
            "kotlin.Double" -> {
                val valueDouble = (value as String).toDouble()
                setter.callSetter(valueDouble, state)
            }
        }
    }

    private fun setUrge(actor: Actor, statement: ScriptStatement) {
        actor.urges.stopUrge("eat")
        actor.urges.increaseUrge(
            statement.mutationTarget,
            statement.mutationOperationArgument.toDouble()
        )
    }

    private fun increaseUrge(actor: Actor, statement: ScriptStatement) {
        actor.urges.increaseUrge(
            statement.mutationTarget,
            statement.mutationOperationArgument.toDouble()
        )
    }

    private fun decreaseUrge(actor: Actor, statement: ScriptStatement) {
        actor.urges.decreaseUrge(
            statement.mutationTarget,
            statement.mutationOperationArgument.toDouble()
        )
    }

    companion object {
        private const val SETTER_PARAM = 1
    }
}
