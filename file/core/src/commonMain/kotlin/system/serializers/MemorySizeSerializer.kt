package system.serializers

import system.MemorySize
import system.memorySize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object MemorySizeSerializer : KSerializer<MemorySize> {
    override val descriptor = PrimitiveSerialDescriptor("epsilon.MemorySize", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): MemorySize = memorySize(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: MemorySize) {
        encoder.encodeString(value.toString())
    }
}