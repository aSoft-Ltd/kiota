import system.MemorySize
import system.MemoryUnit
import system.Multiplier
import system.memorySizeOrNull
import kommander.expect
import kotlin.test.Test

class MemorySizeParsingTest {

    @Test
    fun should_be_able_to_get_the_memory_size_from_a_string() {
        val size = memorySizeOrNull("1 GB")
        expect(size?.value).toBe(1.0)
        expect(size?.unit).toBe(MemoryUnit.Bytes)
        expect(size?.multiplier).toBe(Multiplier.Giga)
    }

    @Test
    fun should_be_able_to_get_the_memory_size__with_no_multiplier_from_a_string() {
        val size = memorySizeOrNull("3b")
        expect(size?.value).toBe(3.0)
        expect(size?.unit).toBe(MemoryUnit.Bits)
        expect(size?.multiplier).toBe(Multiplier.Unit)
    }

    @Test
    fun should_be_able_to_get_the_memory_size_of_a_fractional_value_from_a_string() {
        val size = memorySizeOrNull("1.5kB")
        expect(size?.value).toBe(1.5)
        expect(size?.unit).toBe(MemoryUnit.Bytes)
        expect(size?.multiplier).toBe(Multiplier.Kilo)
    }

    @Test
    fun should_be_able_to_convert_the_memory_size_to_a_string() {
        val size = MemorySize(1.5, Multiplier.Kilo, MemoryUnit.Bytes)
        expect(size.toString()).toBe("1.5KB")
    }

    @Test
    fun should_strip_down_the_zeros_from_the_string_presentation() {
        val size = MemorySize(1.0, Multiplier.Giga, MemoryUnit.Bits)
        expect(size.toString()).toBe("1Gb")
    }
}