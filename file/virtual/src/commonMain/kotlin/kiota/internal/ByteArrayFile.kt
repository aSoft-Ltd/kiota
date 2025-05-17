package kiota.internal

import kiota.file.mime.Mime

data class ByteArrayFile(
    val content: ByteArray,
    override val name: String,
    override val mime: Mime
) : TestFile