package kiota.file.mime

interface Application : Mime {
    companion object : Application {
        override val name: String = "Application"
        override val text: String = "application/*"
    }

    data object JSON : Application {
        override val name: String get() = "JSON"
        override val text: String get() = "application/json"
    }

    data object Pdf : Mime {
        override val name: String get() = "PDF"
        override val text: String get() = "application/pdf"
    }

    data object XML : Application {
        override val name: String get() = "XML"
        override val text: String get() = "application/xml"
    }

    data object ZIP : Application {
        override val name: String get() = "ZIP"
        override val text: String get() = "application/zip"
    }

    data object GZIP : Application {
        override val name: String get() = "GZIP"
        override val text: String get() = "application/gzip"
    }

    data object SevenZip : Application {
        override val name: String get() = "7Z"
        override val text: String get() = "application/x-7z-compressed"
    }

    data object RAR : Application {
        override val name: String get() = "RAR"
        override val text: String get() = "application/vnd.rar"
    }

    data object TAR : Application {
        override val name: String get() = "TAR"
        override val text: String get() = "application/x-tar"
    }

    data object OctetStream : Application {
        override val name: String get() = "OctetStream"
        override val text: String = "application/octet-stream"
    }
}