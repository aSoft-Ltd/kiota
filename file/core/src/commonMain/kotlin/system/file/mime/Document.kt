package system.file.mime

interface Document : Application {
    companion object : Document {
        override val name: String = "Document"
        override val text: String = "application/*"
    }

    data object PDF : Document {
        override val name: String get() = "PDF"
        override val text: String get() = "application/pdf"
    }

    data object DOC : Document {
        override val name: String get() = "DOC"
        override val text: String get() = "application/msword"
    }

    data object DOCX : Document {
        override val name: String get() = "DOCX"
        override val text: String get() = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    }

    data object XLS : Document {
        override val name: String get() = "XLS"
        override val text: String get() = "application/vnd.ms-excel"
    }

    data object XLSX : Document {
        override val name: String get() = "XLSX"
        override val text: String get() = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    }

    data object PPT : Document {
        override val name: String get() = "PPT"
        override val text: String get() = "application/vnd.ms-powerpoint"
    }

    data object PPTX : Document {
        override val name: String get() = "PPTX"
        override val text: String get() = "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    }

    data object RTF : Document {
        override val name: String get() = "RTF"
        override val text: String get() = "application/rtf"
    }
}