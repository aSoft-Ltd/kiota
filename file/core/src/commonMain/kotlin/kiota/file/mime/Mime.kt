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
            (Image.All + Video.All).associateBy { it.name.lowercase() } + mapOf(
                "pdf" to Document.PDF,
                "doc" to Document.DOC,
                "docx" to Document.DOCX,
                "xls" to Document.XLS,
                "xlsx" to Document.XLSX,
                "ppt" to Document.PPT,
                "pptx" to Document.PPTX,
                "txt" to Text.Plain,
                "csv" to Text.CSV,
                "md" to Text.Markdown,
                "markdown" to Text.Markdown,
                "json" to Application.JSON,
                "zip" to Application.ZIP,
                "7z" to Application.SevenZip,
                "rar" to Application.RAR,
                "tar" to Application.TAR,
                "gz" to Application.GZIP,
                "gzip" to Application.GZIP,
                "tgz" to Application.GZIP,
                "mp3" to Audio.MP3,
                "wav" to Audio.WAV,
                "m4a" to Audio.M4A
            )
        }

        fun from(extension: String): Mime = extensions[extension.trim().removePrefix(".").lowercase()] ?: All
    }
}