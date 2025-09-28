import io.ktor.client.HttpClient
import kiota.Connected
import kiota.HttpConnectionVerifier
import kommander.expect
import kommander.toBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class HttpConnectionVerifierTest {

    @Test
    fun should_verify_that_google_is_always_connected() = runTest {
        val hosts = listOf("https://google.com", "https://asoft.co.tz")
        val verifier = HttpConnectionVerifier(
            hosts = hosts,
            http = HttpClient { }
        )
        val result = verifier.verify()
        val connected = expect(result).toBe<Connected>()

        expect(hosts.contains(connected.verified)).toBe(true)
    }
}