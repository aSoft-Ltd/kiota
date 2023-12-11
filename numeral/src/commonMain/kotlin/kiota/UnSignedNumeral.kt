@file:JsExport
@file:Suppress("NON_EXPORTABLE_TYPE")

package kiota

import kotlinx.JsExport

interface UnSignedNumeral {
    val asUInt: UInt
    val asULong: ULong
    val asFloat: Float
    val asDouble: Double

    @JsExportIgnore
    operator fun plus(other: UnSignedNumeral) : UnSignedNumeral

    operator fun times(other: Double) : UnSignedNumeral
}