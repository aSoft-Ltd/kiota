package kiota

import cinematic.Watcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun ConnectionObserver.await(): Connectivity {
    if (status.value !is Connecting) return status.value as Connectivity
    return suspendCancellableCoroutine { cont ->
        var watcher: Watcher? = null
        watcher = status.watchEagerly {
            if (it is Connectivity) {
                watcher?.stop()
                cont.resume(it)
            }
        }
    }
}