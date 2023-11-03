package kiota

import java.net.URI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

actual fun EventSource(url: String): EventSource = EventSource(EventSourceOptions(URI(url), CoroutineScope(SupervisorJob())))