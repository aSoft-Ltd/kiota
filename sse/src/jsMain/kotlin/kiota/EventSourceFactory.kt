package kiota

actual fun EventSource(url: String) : EventSource = EventSource(org.w3c.dom.EventSource(url))