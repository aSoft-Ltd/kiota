package kiota.file

import kiota.GB
import kiota.MemorySize

data class PickerLimit(
    val size: MemorySize = 2.GB,
    val count: Int = Int.MAX_VALUE
) {
    companion object {
        val Default by lazy { PickerLimit() }
    }
}