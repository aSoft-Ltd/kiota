package kiota

import java.net.URI
import kotlinx.coroutines.CoroutineScope

class EventSourceOptions(
    val uri: URI,
    val scope: CoroutineScope
)