@file:JsExport

package kiota

import kase.Optional
import kase.optionalOf
import kiota.internal.indent
import kiota.internal.pretty
import kiota.internal.toEvaluatedUrl
import kiota.internal.toParams
import kiota.internal.toScore
import kollections.List
import kollections.Map
import kollections.get
import kotlinx.JsExport
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

data class UrlMatch(
    val route: Url,
    val pattern: Url,
    val segments: List<SegmentMatch>,
) : ReadOnlyProperty<Any?, String> {

    val params: Map<String, String> by lazy { segments.toParams() }

    fun param(key: String): Optional<String> = optionalOf(params[key])

    fun debugString(spaces: Int) = buildString {
        val gap = indent(spaces)
        appendLine("UrlMatch(")
        appendLine("$gap${gap}route = $route")
        appendLine("$gap${gap}pattern = $pattern")
        appendLine("$gap${gap}params = ${params.pretty()}")
        appendLine("$gap${gap}score = $score")
        append("${gap})")
    }

    val evaluatedRoute get() = segments.toEvaluatedUrl().toUrl()

    val score: Int by lazy { segments.toScore() }

    fun printDebugString() = println(debugString(0))
    override fun toString() = debugString(0)

    override fun getValue(thisRef: Any?, property: KProperty<*>) = param(property.name).getOrThrow("Parameter with ${property.name} is not matched")
}