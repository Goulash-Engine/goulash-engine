package com.barbarus.prosper.core.domain

class Urges {
    private val _urges: MutableMap<String, Double> = mutableMapOf()

    fun getUrges(): Map<String, Double> {
        return _urges
    }

    fun increaseUrge(name: String, value: Double) {
        _urges[name] = _urges[name]?.plus(value) ?: value
        if (_urges[name]!! >= 100) {
            _urges[name] = 100.0
        }
    }

    fun decreaseUrge(name: String, value: Double) {
        if (_urges.containsKey(name)) {
            _urges[name] = _urges[name]!! - value
            if (_urges[name]!! < 0) {
                _urges.remove(name)
            }
        }
    }

    fun stopUrge(name: String) {
        _urges.remove(name)
    }
}
