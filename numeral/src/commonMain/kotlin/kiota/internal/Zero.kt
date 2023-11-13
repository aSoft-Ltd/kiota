package kiota.internal

import kiota.SignedNumeral
import kiota.UnSignedNumeral

internal object Zero : SignedNumeral, UnSignedNumeral {
    override val asUInt: UInt by lazy { 0u }
    override val asULong: ULong by lazy { 0uL }
    override val asInt: Int by lazy { 0 }
    override val asLong: Long by lazy { 0L }
    override val asFloat: Float by lazy { 0f }
    override val asDouble: Double by lazy { 0.0 }

    override fun plus(other: SignedNumeral) = other

    override fun plus(other: UnSignedNumeral) = other

    override fun times(other: Double) = Zero
}