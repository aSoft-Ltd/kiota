package kiota.internal

import kiota.SignedNumeral

internal data class SignedNumeralInt(override val asInt: Int) : SignedNumeral {
    override val asLong by lazy { asInt.toLong() }
    override val asFloat by lazy { asInt.toFloat() }
    override val asDouble by lazy { asInt.toDouble() }

    override fun plus(other: SignedNumeral) = SignedNumeralInt(asInt + other.asInt)

    override fun times(other: Double) = NumeralDouble(asDouble * other)
}