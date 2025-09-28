package kiota

interface ConnectionVerifier {
    suspend fun verify(): Connection
}