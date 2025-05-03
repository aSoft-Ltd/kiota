package kiota

import kiota.internal.BrowserFileReader

actual fun SystemFileReader(): FileReader = BrowserFileReader()