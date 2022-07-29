package com.barbarus.prosper.script.extension

import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.extensionReceiverParameter
import kotlin.reflect.full.instanceParameter

object ReflectionExtensions {

    /**
     * borrow by: https://stackoverflow.com/questions/44792787/reflectively-calling-function-and-using-default-parameters
     * @author https://stackoverflow.com/users/5818889/glee8e
     */
    fun <R> KFunction<R>.callFunction(params: Map<String, Any?>, self: Any? = null, extSelf: Any? = null): R {
        val map = params.entries.mapTo(ArrayList()) { entry ->
            this.parameters
                .filterNot { it.name == null }
                .find { it.name == entry.key }!! to entry.value
        }
        if (self != null) map += instanceParameter!! to self
        if (extSelf != null) map += extensionReceiverParameter!! to extSelf
        return callBy(map.toMap())
    }

    fun <R> KFunction<R>.callSetter(value: Any, self: Any? = null, extSelf: Any? = null): R {
        val map = ArrayList<Pair<KParameter, Any?>>()
        val setterParameter = this.parameters[1]
        val convertedValue = when (setterParameter.type.toString()) {
            "kotlin.Double" -> (value as String).toDouble()
            else -> value as Any
        }
        val setterArgument = this.parameters[1] to convertedValue
        map += setterArgument
        if (self != null) map += instanceParameter!! to self
        if (extSelf != null) map += extensionReceiverParameter!! to extSelf
        return callBy(map.toMap())
    }

    private fun <T> convertArg(arg: Any): T {
        return arg as T
    }
}
