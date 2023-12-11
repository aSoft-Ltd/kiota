@file:JsExport
@file:Suppress("NON_EXPORTABLE_TYPE")

package kiota

import kotlinx.JsExport

interface SignedNumeral {
    val asInt: Int
    val asLong: Long
    val asFloat: Float
    val asDouble: Double

    @JsExportIgnore
    operator fun plus(other: SignedNumeral) : SignedNumeral

    operator fun times(other: Double) : SignedNumeral
}