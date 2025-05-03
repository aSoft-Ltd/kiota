package kiota.file.mime

interface Image : MediaMime {
    companion object : Image {
        override val name: String = "Image"
        override val text: String = "image/*"

        val All by lazy {
            listOf(
                JPG, JPEG, PNG, GIF, BMP, SVG, TIFF, WEBP, HEIC, ICO,
                AVIF, PSD, CR2, NEF, DNG, RAW, ARW, ORF, RW2, SR2,
                X3F, PEF, DCR, KDC
            )
        }
    }

    data object JPG : Image {
        override val name: String get() = "JPG"
        override val text: String get() = "image/jpg"
    }

    data object JPEG : Image {
        override val name: String get() = "JPEG"
        override val text: String get() = "image/jpeg"
    }

    data object PNG : Image {
        override val name: String get() = "PNG"
        override val text: String get() = "image/png"
    }

    data object GIF : Image {
        override val name: String get() = "GIF"
        override val text: String get() = "image/gif"
    }

    data object BMP : Image {
        override val name: String get() = "BMP"
        override val text: String get() = "image/bmp"
    }

    data object SVG : Image {
        override val name: String get() = "SVG"
        override val text: String get() = "image/svg+xml"
    }

    data object TIFF : Image {
        override val name: String get() = "TIFF"
        override val text: String get() = "image/tiff"
    }

    data object WEBP : Image {
        override val name: String get() = "WEBP"
        override val text: String get() = "image/webp"
    }

    data object HEIC : Image {
        override val name: String get() = "HEIC"
        override val text: String get() = "image/heic"
    }

    data object ICO : Image {
        override val name: String get() = "ICO"
        override val text: String get() = "image/x-icon"
    }

    data object AVIF : Image {
        override val name: String get() = "AVIF"
        override val text: String get() = "image/avif"
    }

    data object PSD : Image {
        override val name: String get() = "PSD"
        override val text: String get() = "image/vnd.adobe.photoshop"
    }

    data object CR2 : Image {
        override val name: String get() = "CR2"
        override val text: String get() = "image/cr2"
    }

    data object NEF : Image {
        override val name: String get() = "NEF"
        override val text: String get() = "image/nef"
    }

    data object DNG : Image {
        override val name: String get() = "DNG"
        override val text: String get() = "image/dng"
    }

    data object RAW : Image {
        override val name: String get() = "RAW"
        override val text: String get() = "image/raw"
    }

    data object ARW : Image {
        override val name: String get() = "ARW"
        override val text: String get() = "image/arw"
    }

    data object ORF : Image {
        override val name: String get() = "ORF"
        override val text: String get() = "image/orf"
    }

    data object RW2 : Image {
        override val name: String get() = "RW2"
        override val text: String get() = "image/rw2"
    }

    data object SR2 : Image {
        override val name: String get() = "SR2"
        override val text: String get() = "image/sr2"
    }

    data object X3F : Image {
        override val name: String get() = "X3F"
        override val text: String get() = "image/x3f"
    }

    data object PEF : Image {
        override val name: String get() = "PEF"
        override val text: String get() = "image/pef"
    }

    data object DCR : Image {
        override val name: String get() = "DCR"
        override val text: String get() = "image/dcr"
    }

    data object KDC : Image {
        override val name: String get() = "KDC"
        override val text: String get() = "image/kdc"
    }
}