package kiota.internal

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