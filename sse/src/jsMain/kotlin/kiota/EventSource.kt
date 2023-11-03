package kiota

import org.w3c.dom.MessageEvent
import org.w3c.dom.events.EventListener
import org.w3c.dom.EventSource as JsEventSource

actual class EventSource(val source: JsEventSource) {
    actual fun on(event: String, handler: (Event) -> Unit) {
        source.addEventListener(event, EventListener {
            val e = it.unsafeCast<MessageEvent>()
            handler(Event(e.data.toString(),event,e.lastEventId))
        })
    }
}