import system.memorySize
import kommander.expect
import kotlin.test.Test

class MemoryUnitConvertor {
    @Test
    fun should_convert_in_1024bytes_into_1KB() {
        val size = memorySize("1024B").toBestSize()
        expect(size.toString()).toBe("1KB")
    }

    @Test
    fun should_be_convert_2048KB_into_2MB() {
        val size = memorySize("2048KB").toBestSize()
        expect(size.toString()).toBe("2MB")
    }

    @Test
    fun should_be_convert_4096MB_into_4GB() {
        val size = memorySize("4096MB").toBestSize()
        expect(size.toString()).toBe("4GB")
    }

    @Test
    fun should_be_able_to_convert_0_5GB_into_512MB() {
        val size = memorySize("0.5GB").toBestSize()
        expect(size.toString()).toBe("512MB")
    }

    @Test
    fun should_be_convert_3000Gb_into_Tb() {
        val size = memorySize("3000Gb").toBestSize()
        expect(size.toString()).toBe("3Tb")
    }

    @Test
    fun should_be_able_to_convert_1byte_into_8bits() {
        val size = memorySize("1B").toBits()
        expect(size.toString()).toBe("8b")
    }

    @Test
    fun should_be_able_to_convert_1024bytes_into_8192bits() {
        val size = memorySize("1024B").toBits()
        expect(size.toString()).toBe("8192b")
    }

    @Test
    fun should_be_able_to_get_the_best_bits_presentation() {
        val size = memorySize("8192b").toBestSize()
        expect(size.toString()).toBe("8.192Kb")
    }

    @Test
    fun should_be_able_to_get_the_size_in_bytes() {
        val size = memorySize("1KB")
        expect(size.inBytes()).toBe(1024.0)
    }
}