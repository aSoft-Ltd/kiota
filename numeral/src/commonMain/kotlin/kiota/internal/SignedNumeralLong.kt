package kiota.internal

import kiota.SignedNumeral

internal data class SignedNumeralLong(override val asLong: Long) : SignedNumeral {
    override val asInt by lazy { asLong.toInt() }
    override val asFloat by lazy { asLong.toFloat() }
    override val asDouble by lazy { asLong.toDouble() }

    override fun plus(other: SignedNumeral) = SignedNumeralLong(asLong+other.asLong)

    override fun times(other: Double) = NumeralDouble(asDouble * other)
}