@file:JsExport
@file:Suppress("NON_EXPORTABLE_TYPE")

package kiota

import kotlin.js.JsExport

interface SignedNumeral {
    val asInt: Int
    val asLong: Long
    val asFloat: Float
    val asDouble: Double

    @JsExport.Ignore
    operator fun plus(other: SignedNumeral) : SignedNumeral

    operator fun times(other: Double) : SignedNumeral
}