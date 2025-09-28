package kiota

import io.ktor.client.HttpClient

class HttpConnectionVerifier(
    hosts: List<String>,
    http: HttpClient
) : HttpRandomConnector(hosts, http), ConnectionVerifier {
    override suspend fun verify() = connect()
}