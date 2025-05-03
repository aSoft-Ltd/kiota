package system.file.mime

interface Video : MediaMime {
    companion object : Video {
        override val name: String = "Video"
        override val text: String = "video/*"

        val All by lazy {
            listOf(
                MP4, MKV, AVI, MOV, WMV, FLV, WEBM, OGV, M4V, TS,
                GP3, ASF, RMVB, MTS, M2TS, VOB, DVRMS, DIVX, XVID,
                MPG, MPEG
            )
        }
    }

    data object MP4 : Video {
        override val name: String get() = "MP4"
        override val text: String get() = "video/mp4"
    }

    data object MKV : Video {
        override val name: String get() = "MKV"
        override val text: String get() = "video/mkv"
    }

    data object AVI : Video {
        override val name: String get() = "AVI"
        override val text: String get() = "video/avi"
    }

    data object MOV : Video {
        override val name: String get() = "MOV"
        override val text: String get() = "video/mov"
    }

    data object WMV : Video {
        override val name: String get() = "WMV"
        override val text: String get() = "video/wmv"
    }

    data object FLV : Video {
        override val name: String get() = "FLV"
        override val text: String get() = "video/flv"
    }

    data object WEBM : Video {
        override val name: String get() = "WEBM"
        override val text: String get() = "video/webm"
    }

    data object OGV : Video {
        override val name: String get() = "OGV"
        override val text: String get() = "video/ogv"
    }

    data object M4V : Video {
        override val name: String get() = "M4V"
        override val text: String get() = "video/m4v"
    }

    data object TS : Video {
        override val name: String get() = "TS"
        override val text: String get() = "video/ts"
    }

    data object GP3 : Video {
        override val name: String get() = "3GP"
        override val text: String get() = "video/3gp"
    }

    data object ASF : Video {
        override val name: String get() = "ASF"
        override val text: String get() = "video/asf"
    }

    data object RMVB : Video {
        override val name: String get() = "RMVB"
        override val text: String get() = "video/rmvb"
    }

    data object MTS : Video {
        override val name: String get() = "MTS"
        override val text: String get() = "video/mts"
    }

    data object M2TS : Video {
        override val name: String get() = "M2TS"
        override val text: String get() = "video/m2ts"
    }

    data object VOB : Video {
        override val name: String get() = "VOB"
        override val text: String get() = "video/vob"
    }

    data object DVRMS : Video {
        override val name: String get() = "DVRMS"
        override val text: String get() = "video/dvr-ms"
    }

    data object DIVX : Video {
        override val name: String get() = "DIVX"
        override val text: String get() = "video/divx"
    }

    data object XVID : Video {
        override val name: String get() = "XVID"
        override val text: String get() = "video/xvid"
    }

    data object MPG : Video {
        override val name: String get() = "MPG"
        override val text: String get() = "video/mpg"
    }

    data object MPEG : Video {
        override val name: String get() = "MPEG"
        override val text: String get() = "video/mpeg"

    }
}