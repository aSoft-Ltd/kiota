package kiota

import cinematic.mutableLiveOf
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class HttpPollingConnectionObserver(
    hosts: List<String>,
    http: HttpClient,
    private val scope: CoroutineScope,
    private val interval: Duration = 5.minutes
) : HttpRandomConnector(hosts, http), ConnectionObserver {
    override val status by lazy { mutableLiveOf<Connection>(Connecting) }

    private var job: Job? = null

    override fun start() {
        job?.cancel()
        job = scope.launch {
            while (job?.isActive == true) {
                if (status.value !is Connected) {
                    status.value = Connecting
                }
                status.value = connect()
                delay(interval)
            }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }
}