package system.file

import system.GB
import system.MemorySize

data class PickerLimit(
    val size: MemorySize = 2.GB,
    val count: Int = Int.MAX_VALUE
) {
    companion object {
        val Default by lazy { PickerLimit() }
    }
}