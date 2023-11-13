package kiota.internal

import kiota.UnSignedNumeral

internal data class UnSignedNumeralLong(override val asULong: ULong) : UnSignedNumeral {
    override val asUInt by lazy { asULong.toUInt() }
    override val asFloat by lazy { asULong.toFloat() }
    override val asDouble by lazy { asULong.toDouble() }

    override fun plus(other: UnSignedNumeral) = UnSignedNumeralLong(asULong + other.asULong)

    override fun times(other: Double) = NumeralDouble(asDouble * other)
}