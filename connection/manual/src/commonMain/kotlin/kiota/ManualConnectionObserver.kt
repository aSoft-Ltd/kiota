package kiota

import cinematic.mutableLiveOf

class ManualConnectionObserver(
    initial: Connection = Disconnected,
    private val verifier: ConnectionVerifier = ManualConnectionVerifier()
) : ConnectionObserver {
    override val status by lazy { mutableLiveOf(initial) }

    fun set(connection: Connection) {
        status.value = connection
    }

    /**
     * Use to set the connection status manually.
     *
     * @param verify If true, the [status] will be set to [Connected] if [verifier] verifies that the connection is valid., otherwise it will be set to [Disconnected].
     * @param verify if false, the connection will be set to [Connected] without verification
     */
    suspend fun connect(verify: Boolean = true) {
        if (!verify) return set(Connected(null))
        set(Connecting)
        val result = when (val res = verifier.verify()) {
            is Connected -> res
            else -> Disconnected
        }
        set(result)
    }

    fun connecting() = set(Connecting)

    fun disconnect() = set(Disconnected)

    override fun start() {}

    override fun stop() {}
}