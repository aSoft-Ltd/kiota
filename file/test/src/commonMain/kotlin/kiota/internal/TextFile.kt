package kiota.internal

import kiota.file.mime.Mime

class TextFile(
    val content: String,
    override val name: String,
    override val mime: Mime
) : TestFile