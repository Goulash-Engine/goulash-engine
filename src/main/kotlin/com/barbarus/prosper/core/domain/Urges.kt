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
        if (_urges[name]!! < 0) {
            _urges.remove(name)
        }
    }

    fun stopUrge(name: String) {
        _urges.remove(name)
    }
}
