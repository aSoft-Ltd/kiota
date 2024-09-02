package kiota

class Param(val value: Any?) {
    fun int(): Int = "$value".toInt()
    fun intOrNull(): Int? = "$value".toIntOrNull()
}