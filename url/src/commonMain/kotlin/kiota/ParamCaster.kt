package kiota

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ParamCaster<out T>(
    private val entries: Map<String, String>,
    private val transform: (String) -> T
) : ReadOnlyProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val value = entries[property.name] ?: ""
        return try {
            transform(value)
        } catch (err: Throwable) {
            throw IllegalArgumentException("Failed to transform query parameter ${property.name} with value '$value'", err)
        }
    }
}