package kiota

import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class ManualConnectionVerifier(
    private var result: Connection = Disconnected,
    private var delay: Duration = 2.seconds,
) : ConnectionVerifier {
    override suspend fun verify(): Connection {
        delay(delay)
        return result
    }

    /**
     * Instructs the verifier to respond with the given connection after the given delay when [verify] is called.
     */
    fun respond(with: Connected, after: Duration = delay) {
        delay = after
        result = with
    }

    /**
     * Instructs the verifier to respond with the given connection after the given delay when [verify] is called.
     */
    fun respond(with: Disconnected, after: Duration = delay) {
        delay = after
        result = with
    }
}