import kommander.expect
import kiota.GB
import kiota.MB
import kiota.bits
import kiota.bytes
import kiota.Gb
import kiota.Mb
import kotlin.test.Test

class MemoryUnitComparison {
    @Test
    fun should_be_able_to_compare_sizes_with_same_unit_and_multipliers() {
        expect(2.GB > 1.GB).toBe(true)
        expect(2.GB > 2.GB).toBe(false)
    }

    @Test
    fun should_be_able_to_compare_sizes_with_different_unit_and_same_multipliers() {
        expect(2.Mb < 2.MB).toBe(true)
        expect(3.Gb > 3.GB).toBe(false)
        expect(8.bits >= 1.bytes).toBe(true)
    }

    @Test
    fun should_be_able_to_compare_sizes_with_same_unit_and_different_multipliers() {
        expect(2.MB < 2.GB).toBe(true)
        expect(3.Gb > 3.Mb).toBe(true)
    }
}