package kiota.file.mime;

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
        private val cache = mutableMapOf<String, Mime>()
        fun from(extension: String): Mime = cache.getOrPut(extension.lowercase()) {
            (Image.All + Video.All).find { it.name.contentEquals(extension, ignoreCase = true) } ?: All
        }
    }
}