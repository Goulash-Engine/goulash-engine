package com.goulash.core.domain

class Urges {
    private val _urges: MutableMap<String, Double> = mutableMapOf()

    fun getAllUrges(): Map<String, Double> {
        return _urges.toMap()
    }
    fun getUrgeOrNull(name: String): Double? {
        return _urges[name]
    }

    fun increaseUrge(name: String, value: Double) {
        _urges[name] = _urges[name]?.plus(value) ?: value
        if (_urges[name]!! >= 100) {
            _urges[name] = 100.0
        }
    }

    fun decreaseUrge(name: String, value: Double, remove: Boolean = true) {
        if (_urges.containsKey(name)) {
            _urges[name] = _urges[name]!! - value
            if (_urges[name]!! < 0 && remove) {
                _urges.remove(name)
            }
        }
    }

    fun stopUrge(name: String) {
        _urges.remove(name)
    }

    fun copy() = Urges().apply {
        _urges.putAll(_urges)
    }
}
