package kiota

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

actual class EventSource(options: EventSourceOptions) {
    private val client = HttpClient.newHttpClient()
    private val request = HttpRequest.newBuilder(options.uri).GET().build()

    private val handlers = mutableListOf<(Event) -> Unit>()

    init {
        options.scope.launch(Dispatchers.IO) {
            val lines = client.send(request, BodyHandlers.ofLines()).body()
            val contents = mutableListOf<String>()
            lines.forEach {
                if (it.isNotBlank()) {
                    contents += it
                }
                if (it.isBlank()) {
                    val event = contents.toEvent()
                    contents.clear()
                    handlers.forEach { it(event) }
                }
            }
        }
    }

    private fun List<String>.toEvent(): Event {
        val contents = toMutableList()
        val id = contents.find { it.startsWith("id:") }
        if (id != null) contents.remove(id)
        val event = contents.find { it.startsWith("event:") }
        if (event != null) contents.remove(event)
        return Event(
            data = contents.joinToString("\n").clearKey("data"),
            event = event?.clearKey("event") ?: "message",
            id = id?.clearKey("id"),
        )
    }

    private fun String.clearKey(key: String) = replace("$key: ", "").replace("$key:", "")

    actual fun on(event: String, handler: (Event) -> Unit) {
        handlers.add(handler)
    }
}