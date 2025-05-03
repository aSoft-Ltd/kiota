package system

import kotlin.math.pow

internal fun convertBits(value: Double, from: Multiplier, to: Multiplier): Double {
    if (from == to) return value
    return value * 10.0.pow(from.exponent - to.exponent)
}

internal fun convertBytes(value: Double, from: Multiplier, to: Multiplier): Double {
    if (from == to) return value
    return value * 1024.0.pow((from.exponent - to.exponent) / 3)
}