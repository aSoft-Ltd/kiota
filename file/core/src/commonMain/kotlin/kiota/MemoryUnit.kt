@file:JsExport
package kiota

import kotlinx.JsExport
import kotlinx.serialization.Serializable

@Serializable
enum class MemoryUnit {
    Bits, Bytes;
    override fun toString() : String = when(this) {
        Bits -> "b"
        Bytes -> "B"
    }
}