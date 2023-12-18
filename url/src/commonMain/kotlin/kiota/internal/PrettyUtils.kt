package kiota.internal

import kollections.Map
import kollections.entries
import kollections.joinToString
import kollections.key
import kollections.value

fun Map<*, *>.pretty() = entries.joinToString(prefix = "[", separator = " , ", postfix = "]") {
    "${it.key}=${it.value}"
}

fun indent(spaces: Int = 0): String {
    var blanks = ""
    repeat(spaces) {
        blanks = "$blanks "
    }
    return blanks
}