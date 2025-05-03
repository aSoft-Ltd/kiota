@file:JsExport

package kiota

import kotlinx.JsExport
import kotlinx.serialization.Serializable
import kiota.MemoryUnit.Bits
import kiota.MemoryUnit.Bytes
import kiota.serializers.MemorySizeSerializer
import kotlin.math.round

@Serializable(with = MemorySizeSerializer::class)
data class MemorySize(
    val value: Double,
    val multiplier: Multiplier,
    val unit: MemoryUnit
) : Comparable<MemorySize> {

    companion object {
        val Zero by lazy { MemorySize(0.0, Multiplier.Unit, Bytes) }
    }

    override fun toString() = "${(round(value * 100) / 100).toString().removeSuffix(".0")}${multiplier}${unit}"

    private val convertor by lazy {
        when (unit) {
            Bits -> ::convertBits
            Bytes -> ::convertBytes
        }
    }

    fun toBestSize(): MemorySize {
        val multipliers = Multiplier.values().reversed()
        var best: Double
        for (multiplier in multipliers) {
            best = convertor(value, this.multiplier, multiplier)
            if (best >= 1) {
                return MemorySize(best, multiplier, unit)
            }
        }
        return this
    }

    fun toBytes(): MemorySize {
        if (unit == Bytes) return this
        return MemorySize(value / 8, multiplier, Bytes)
    }

    fun toBits(): MemorySize {
        if (unit == Bits) return this
        return MemorySize(value * 8, multiplier, Bits)
    }

    fun to(unit: Multiplier): MemorySize {
        val v = convertor(value, this.multiplier, unit)
        return MemorySize(v, unit, this.unit)
    }

    fun inBytes() = toBytes().to(Multiplier.Unit).value

    override fun compareTo(other: MemorySize): Int {
        if (unit == other.unit && multiplier == other.multiplier) {
            return value.compareTo(other.value)
        }

        if (unit == other.unit) return compareTo(other.to(multiplier))

        return toBits().compareTo(other.toBits())
    }
}