package kiota.file

class FilePickers<
        out MFP,
        out SFP,
        out MMP,
        out SMP
        >(
    val documents: MFP,
    val document: SFP,
    val medias: MMP,
    val media: SMP,
)