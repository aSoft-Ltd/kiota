package system

import system.internal.LocalFileImpl

inline fun LocalFile(path: String): LocalFile = LocalFileImpl(path)