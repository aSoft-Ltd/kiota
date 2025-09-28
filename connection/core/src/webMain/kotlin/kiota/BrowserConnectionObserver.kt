package kiota

import cinematic.mutableLiveOf
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.w3c.dom.events.Event

class BrowserConnectionObserver(
    val verifier: ConnectionVerifier,
    val scope: CoroutineScope
) : ConnectionObserver {
    override val status by lazy { mutableLiveOf<Connection>(Connecting) }

    private val online: (Event) -> Unit = { detect() }

    private val offline: (Event) -> Unit = {
        status.value = Disconnected
    }

    private fun detect() {
        status.value = Connecting
        scope.launch {
            status.value = verifier.verify()
        }
    }

    override fun start() {
        detect()
        window.addEventListener("online", online)
        window.addEventListener("offline", offline)
    }

    override fun stop() {
        window.removeEventListener("online", online)
        window.removeEventListener("offline", offline)
    }
}