package kiota

import cinematic.Live

interface ConnectionObserver {
    val status: Live<Connection>

    fun start()

    fun stop()
}