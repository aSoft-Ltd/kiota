package system.file

class FilePickers<
    out MFP : MultiFilePicker,
    out SFP : SingleFilePicker,
    out MMP : MultiMediaPicker,
    out SMP : SingleMediaPicker
>(
    val documents: MFP,
    val document: SFP,
    val medias: MMP,
    val media: SMP,
)