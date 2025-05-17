package kiota.file

fun interface Openable<out T> {
    suspend fun open(): T
}