package system.file.mime

interface Audio : Mime {
    companion object : Audio {
        override val name: String = "Audio"
        override val text: String = "audio/*"
    }

    data object MP3 : Audio {
        override val name: String get() = "MP3"
        override val text: String get() = "audio/mp3"
    }

    data object MPEG : Audio {
        override val name: String get() = "MP3"
        override val text: String get() = "audio/mpeg"
    }

    data object AAC : Audio {
        override val name: String get() = "AAC"
        override val text: String get() = "audio/aac"
    }

    data object WAV : Audio {
        override val name: String get() = "WAV"
        override val text: String get() = "audio/wav"
    }

    data object FLAC : Audio {
        override val name: String get() = "FLAC"
        override val text: String get() = "audio/flac"
    }

    data object OGG : Audio {
        override val name: String get() = "OGG"
        override val text: String get() = "audio/ogg"
    }

    data object WMA : Audio {
        override val name: String get() = "WMA"
        override val text: String get() = "audio/wma"
    }

    data object ALAC : Audio {
        override val name: String get() = "ALAC"
        override val text: String get() = "audio/alac"
    }

    data object APE : Audio {
        override val name: String get() = "APE"
        override val text: String get() = "audio/ape"
    }

    data object AMR : Audio {
        override val name: String get() = "AMR"
        override val text: String get() = "audio/amr"
    }

    data object MID : Audio {
        override val name: String get() = "MID"
        override val text: String get() = "audio/mid"
    }

    data object M4A : Audio {
        override val name: String get() = "M4A"
        override val text: String get() = "audio/m4a"
    }

    data object WavPack : Audio {
        override val name: String get() = "WavPack"
        override val text: String get() = "audio/wavpack"
    }

    data object AIF : Audio {
        override val name: String get() = "AIF"
        override val text: String get() = "audio/aif"
    }

    data object CAF : Audio {
        override val name: String get() = "CAF"
        override val text: String get() = "audio/caf"
    }

    data object DTS : Audio {
        override val name: String get() = "DTS"
        override val text: String get() = "audio/dts"
    }

    data object DSD : Audio {
        override val name: String get() = "DSD"
        override val text: String get() = "audio/dsd"
    }

    data object MKA : Audio {
        override val name: String get() = "MKA"
        override val text: String get() = "audio/mka"
    }

    data object M3U : Audio {
        override val name: String get() = "M3U"
        override val text: String get() = "audio/m3u"
    }

    data object M3U8 : Audio {
        override val name: String get() = "M3U8"
        override val text: String get() = "audio/m3u8"
    }

    data object PLS : Audio {
        override val name: String get() = "PLS"
        override val text: String get() = "audio/pls"
    }

    data object WPL : Audio {
        override val name: String get() = "WPL"
        override val text: String get() = "audio/wpl"
    }
}