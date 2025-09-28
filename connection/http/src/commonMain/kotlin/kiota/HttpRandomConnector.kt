package kiota

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.CancellationException

abstract class HttpRandomConnector(
    private val hosts: List<String>,
    private val http: HttpClient
) {
    protected suspend fun connect(): Connection {
        if (hosts.isEmpty()) return Disconnected
        val candidates = hosts.shuffled().toMutableSet()
        do {
            val host = candidates.random()
            candidates.remove(host)
            try {
                val response = http.get(host)
                if (response.status.value in 200..299) return Connected(verified = host)
            } catch (ce: CancellationException) {
                throw ce
            } catch (_: Throwable) {
            }
        } while (candidates.isNotEmpty())
        return Disconnected
    }
}