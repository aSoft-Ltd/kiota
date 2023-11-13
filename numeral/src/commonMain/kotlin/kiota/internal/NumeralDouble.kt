package kiota.internal

import kiota.SignedNumeral
import kiota.UnSignedNumeral

internal data class NumeralDouble(override val asDouble: Double) : SignedNumeral, UnSignedNumeral {
    override val asUInt by lazy { asDouble.toUInt() }
    override val asULong by lazy { asDouble.toULong() }
    override val asInt by lazy { asDouble.toInt() }
    override val asLong by lazy { asDouble.toLong() }
    override val asFloat by lazy { asDouble.toFloat() }

    override fun plus(other: SignedNumeral) = NumeralDouble(asDouble + other.asDouble)

    override fun plus(other: UnSignedNumeral) = NumeralDouble(asDouble + other.asDouble)

    override fun times(other: Double) = NumeralDouble(asDouble * other)
}