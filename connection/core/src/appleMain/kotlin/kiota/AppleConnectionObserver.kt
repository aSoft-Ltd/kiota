package kiota

import cinematic.mutableLiveOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_unsatisfied
import platform.darwin.DISPATCH_QUEUE_SERIAL_WITH_AUTORELEASE_POOL
import platform.darwin.dispatch_queue_create

internal class AppleConnectionObserver(
    private val verifier: ConnectionVerifier,
    private val scope: CoroutineScope,
) : ConnectionObserver {
    override val status by lazy { mutableLiveOf<Connection>(Connecting) }

    private val monitor by lazy { nw_path_monitor_create() }

    private val queue by lazy {
        dispatch_queue_create(
            label = "kiota.connection.observer",
            attr = DISPATCH_QUEUE_SERIAL_WITH_AUTORELEASE_POOL,
        )
    }

    override fun start() {
        nw_path_monitor_set_update_handler(monitor) { path ->
            if (nw_path_get_status(path) == nw_path_status_unsatisfied) {
                status.value = Disconnected
                return@nw_path_monitor_set_update_handler
            }

            scope.launch {
                status.value = Connecting
                status.value = verifier.verify()
            }
        }
        nw_path_monitor_set_queue(monitor, queue)
        nw_path_monitor_start(monitor)
    }

    override fun stop() {
        nw_path_monitor_cancel(monitor)
    }
}