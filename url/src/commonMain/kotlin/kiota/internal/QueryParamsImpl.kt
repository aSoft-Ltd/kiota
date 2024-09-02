package kiota.internal

import kiota.ParamCaster
import kiota.QueryParams
import kotlin.reflect.KProperty

@PublishedApi
internal class QueryParamsImpl private constructor(
    val entries: Map<String, String>
) : QueryParams {

    companion object {
        val empty by lazy { QueryParamsImpl(emptyMap()) }
        fun from(entries: Map<String, String>): QueryParamsImpl {
            if (entries.isEmpty()) return empty
            return QueryParamsImpl(entries)
        }

        fun parse(value: String?): QueryParamsImpl {
            if (value.isNullOrBlank()) return empty
            return try {
                val pairs = value.split("&").mapNotNull {
                    val entry = it.split("=")
                    val key = entry.getOrNull(0) ?: return@mapNotNull null
                    val value = entry.getOrNull(1) ?: return@mapNotNull null
                    key to value
                }
                QueryParamsImpl(mapOf(*pairs.toTypedArray()))
            } catch (_: Throwable) {
                empty
            }
        }
    }

    override fun get(name: String): String? = entries[name]

    override fun all(): Map<String, String> = entries

    override fun getValue(thisRef: Any?, property: KProperty<*>) = entries[property.name]

    override fun <T> cast(transform: (String) -> T) = ParamCaster(entries, transform)
}