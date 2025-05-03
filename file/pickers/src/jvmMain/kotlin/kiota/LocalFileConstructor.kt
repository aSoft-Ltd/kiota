package kiota

import kiota.internal.FileImpl

inline fun File(path: String): File = FileImpl(path)