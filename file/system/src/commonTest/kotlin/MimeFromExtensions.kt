import kommander.expect
import kiota.file.mime.Image
import kiota.file.mime.Mime
import kotlin.test.Test

class MimeFromExtensions {
    @Test
    fun should_properly_detect_that_a_jpg_is_an_image() {
        expect(Mime.from(extension = "jpg")).toBe(Image.JPG)
    }
}