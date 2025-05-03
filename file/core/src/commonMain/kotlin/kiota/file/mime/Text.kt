package kiota.file.mime

interface Text : Mime {
    companion object : Text {
        override val name: String = "Text"
        override val text: String = "text/*"
    }

    data object Plain : Text {
        override val name: String get() = "Plain"
        override val text: String get() = "text/plain"
    }

    data object HTML : Text {
        override val name: String get() = "HTML"
        override val text: String get() = "text/html"
    }

    data object CSS : Text {
        override val name: String get() = "CSS"
        override val text: String get() = "text/css"
    }

    data object XML : Text {
        override val name: String get() = "XML"
        override val text: String get() = "text/xml"
    }

    data object CSV : Text {
        override val name: String get() = "CSV"
        override val text: String get() = "text/csv"
    }

    data object Markdown : Text {
        override val name: String get() = "Markdown"
        override val text: String get() = "text/markdown"
    }
}