@file:JsExport
@file:Suppress("NON_EXPORTABLE_TYPE")

package kiota

import kotlin.js.JsExport

interface UnSignedNumeral {
    val asUInt: UInt
    val asULong: ULong
    val asFloat: Float
    val asDouble: Double

    @JsExport.Ignore
    operator fun plus(other: UnSignedNumeral) : UnSignedNumeral

    operator fun times(other: Double) : UnSignedNumeral
}