import system.MemorySize
import system.MemoryUnit
import system.Multiplier
import system.memorySizeOrNull
import kommander.expect
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test

class MemorySizeSerializationTest {

    @Test
    fun should_be_able_to_serialize_and_deserialize_well() {
        val size = memorySizeOrNull("1 GB")
        val json = Json.encodeToString(size)
        val deserialized = Json.decodeFromString<MemorySize>(json)
        expect(deserialized.value).toBe(1.0)
        expect(deserialized.multiplier).toBe(Multiplier.Giga)
        expect(deserialized.unit).toBe(MemoryUnit.Bytes)
        expect(size).toBe(deserialized)
    }

    @Test
    fun should_be_able_to_serialize_and_deserialize_well_with_no_multiplier() {
        val size = memorySizeOrNull("3b")
        val json = Json.encodeToString(size)
        val deserialized = Json.decodeFromString<MemorySize>(json)
        expect(deserialized.value).toBe(3.0)
        expect(deserialized.multiplier).toBe(Multiplier.Unit)
        expect(deserialized.unit).toBe(MemoryUnit.Bits)
        expect(size).toBe(deserialized)
    }
}