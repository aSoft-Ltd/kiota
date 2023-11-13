package kiota.internal

import kiota.UnSignedNumeral

internal data class UnSignedNumeralInt(override val asUInt: UInt) : UnSignedNumeral {
    override val asULong by lazy { asUInt.toULong() }
    override val asFloat by lazy { asUInt.toFloat() }
    override val asDouble by lazy { asUInt.toDouble() }

    override fun plus(other: UnSignedNumeral) = UnSignedNumeralInt(asUInt + other.asUInt)

    override fun times(other: Double) = NumeralDouble(asDouble * other)
}