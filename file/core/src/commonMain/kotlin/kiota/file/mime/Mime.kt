package kiota.file.mime

interface Mime {
    val name: String
    val text: String

    fun matches(other: Mime): Boolean {
        if (this is All) return true
        val (lhsType, lhsExt) = text.split("/")
        val (rhsType, rhsExt) = other.text.split("/")
        if (lhsType != rhsType) return false
        return when (lhsExt) {
            "*" -> true
            rhsExt -> true
            else -> false
        }
    }

    fun matches(extension: String): Boolean {
        if (this is All) return true
        return matches(from(extension))
    }

    companion object {
        private val extensions: Map<String, Mime> by lazy {
            (Image.All + Video.All + Document.All + Application.All + Audio.All).associateBy { it.name.lowercase() } + mapOf(
                "txt" to Text.Plain,
                "md" to Text.Markdown,
                "7z" to Application.SevenZip,
                "gz" to Application.GZIP,
            )
        }

        fun from(extension: String): Mime = extensions[extension.trim().removePrefix(".").lowercase()] ?: All
    }
}