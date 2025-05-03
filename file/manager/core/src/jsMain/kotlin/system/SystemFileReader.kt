package system

import system.internal.BrowserFileReader

actual fun SystemFileReader(): FileReader = BrowserFileReader()