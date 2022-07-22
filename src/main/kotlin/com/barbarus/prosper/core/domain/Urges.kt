package com.barbarus.prosper.core.domain

class Urges {
    private val _urges: MutableMap<String, Double> = mutableMapOf()

    fun getUrges(): Map<String, Double> {
        return _urges
    }

    fun increaseUrge(name: String, value: Double) {
        _urges[name] = _urges[name]?.plus(value) ?: value
    }

    fun decreaseUrge(name: String, value: Double) {
        _urges[name] = _urges[name]?.minus(value) ?: value
    }

    fun resetUrge(Name: String) {
        _urges[Name] = 0.0
    }
}
