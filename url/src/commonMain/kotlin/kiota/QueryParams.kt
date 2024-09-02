package kiota

import kotlin.properties.ReadOnlyProperty

interface QueryParams : ReadOnlyProperty<Any?, String?> {
    operator fun get(name: String): String?
    fun all(): Map<String, String>

    fun <T> cast(transform: (String) -> T): ParamCaster<T>

    fun int() = cast { it.toInt() }

    fun intOrNull() = cast { it.toIntOrNull() }

    fun intOr(value: Int) = cast { it.toIntOrNull() ?: value }

    fun string() = cast { it }

    fun double() = cast { it.toDouble() }

    fun doubleOrNull() = cast { it.toDoubleOrNull() }

    fun doubleOr(value: Double) = cast { it.toDoubleOrNull() ?: value }
}