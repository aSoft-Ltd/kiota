@file:JsExport
@file:Suppress("NON_EXPORTABLE_TYPE")

package kiota

import kase.Optional
import kollections.List
import kollections.Map
import kotlinx.JsExport

interface UrlMatch {
    val segments: List<SegmentMatch>
    val route: Url
    val pattern: Url
    val score: Int
    val params: Map<String, String>

    fun param(key: String): Optional<String>

    fun debugString(spaces: Int = 0): String

    val evaluatedRoute: Url

    fun printDebugString() = println(debugString(0))
}