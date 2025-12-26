package kiota.internal

import kiota.DynamicParamMatch
import kiota.ExactMatch
import kiota.SegmentMatch
import kiota.Url
import kiota.UrlMatch
import kiota.WildCardMatch

@PublishedApi
internal class UrlImpl(
    override val scheme: String,
    override val domain: String,
    override val port: Int?,
    override val segments: List<String>,
    override val params: QueryParamsImpl
) : Url {

    override val host by lazy { if (port == null) domain else "$domain:$port" }

    companion object {

        private val String.isDomainLike: Boolean
            get() = (contains(".") && !startsWith(".")) || (contains(":") && !startsWith(":"))

        private fun scheme(value: String): String {
            if (value.contains("?")) return scheme(value.substringBefore("?"))
            if (value.contains("://")) return value.substringBefore("://").lowercase()
            return ""
        }

        private fun String.schemeless(scheme: String): String {
            if (scheme.isEmpty()) return this
            return lowercase().substringAfter("$scheme://")
        }

        operator fun invoke(value: String): UrlImpl {
            val protocol = scheme(value)
            val schemeless = value.schemeless(protocol)
            val split = schemeless.split("?")
            val params = split.getOrNull(1)
            val segments = (split.getOrNull(0) ?: "").replace("//", "/").split("/").filter {
                it.isNotBlank()
            }
            val first = segments.firstOrNull()
            val host = when {
                protocol.isNotEmpty() -> first ?: ""
                first?.isDomainLike == true -> first
                else -> ""
            }
            val paths = segments - host

            val domain = host.substringBefore(":")
            val port = host.substringAfter(":", "").toIntOrNull()
            return UrlImpl(
                scheme = protocol,
                domain = domain,
                port = port,
                segments = paths.toList(),
                params = QueryParamsImpl.parse(params)
            )
        }
    }

    override fun toString() = "$root$path" + if (params.entries.isNotEmpty()) {
        params.entries.entries.joinToString(prefix = "?", separator = "&") { "${it.key}=${it.value}" }
    } else ""

    override fun equals(other: Any?): Boolean = when (other) {
        is String -> toString() == other
        is Url -> toString() == other.toString()
        else -> false
    }

    override fun hashCode() = toString().hashCode()

    override fun sibling(url: String): Url {
        if (segments.isEmpty()) return this
        val next = UrlImpl(url)
        val s = (segments - segments.last()) + next.segments
        val p = QueryParamsImpl.from(entries = params.entries + next.params.entries)
        return UrlImpl(scheme, domain, port, s, p)
    }

    override fun at(path: String, query: Boolean): Url {
        val next = UrlImpl(path)
        val p = QueryParamsImpl.from(entries = (if (query) params.entries else emptyMap()) + next.params.entries)
        return UrlImpl(scheme, domain, port, next.segments, p)
    }

    override fun child(url: String, query: Boolean): Url {
        val next = UrlImpl(url)
        val p = QueryParamsImpl.from(entries = (if (query) params.entries else emptyMap()) + next.params.entries)
        return UrlImpl(scheme, domain, port, segments + next.segments, p)
    }

    override val path = "/${segments.joinToString(separator = "/")}"

    override fun trail(): Url = UrlImpl(scheme = "", domain = "", port = null, segments = segments, params = params)

    override fun resolve(path: String, query: Boolean): Url = when {
        path.startsWith("/") -> at(path, query)
        path.startsWith(".") -> relativePathResolver(path, query)
        segments.isEmpty() -> at(path, query)
        Url(path).domain.isNotBlank() -> Url(path)
        else -> child(path, query)
    }

    private fun relativePathResolver(path: String, query: Boolean): Url {
        var out = segments
        val next = UrlImpl(path)
        for (segment in next.segments) when {
            segment == "." -> {}
            segment == ".." -> out -= (out.lastOrNull() ?: "")
            else -> out += segment
        }
        val p = QueryParamsImpl.from(entries = (if (query) params.entries else emptyMap()) + next.params.entries)
        return UrlImpl(scheme, domain, port, out, p)
    }

    override val root = buildString {
        if (scheme.isNotBlank()) {
            append(scheme)
            append("://")
        }
        if (domain.isNotBlank()) append(domain)
        if (port != null) append(":$port")
    }

    override fun withParams(vararg params: Pair<String, String>): Url {
        val p = buildMap {
            putAll(this@UrlImpl.params.entries)
            putAll(params)
        }
        return UrlImpl(scheme, domain, port, segments, QueryParamsImpl.from(p))
    }

    override fun withParams(name: String, value: Number): Url = withParams(name to "$value")

    override fun withParams(name: String, value: String): Url = withParams(name to value)

    override fun rebase(url: Url): Url {
        val matchingSegments = mutableSetOf<String>()
        for (s in url.segments.indices) {
            val patternSegment = segments.getOrNull(s) ?: break
            if (patternSegment == "*" && s == segments.indices.last) break
            val routeSegment = url.segments.getOrNull(s) ?: break
            if (routeSegment.matches(patternSegment) == null) break
            matchingSegments.add(routeSegment)
        }
        return UrlImpl(url.scheme, url.domain, url.port, (url.segments - matchingSegments), params)
    }

    override fun rebase(url: String) = rebase(UrlImpl(url))

    override fun matches(pattern: String): UrlMatch? = matches(UrlImpl(pattern))

    override fun withScheme(scheme: String): Url = UrlImpl(scheme, domain, port, segments, params)

    override fun matches(pattern: Url): UrlMatch? {
        if (pattern.path == "/" && path != "/") return null
        val p = when {
            pattern.segments.size >= segments.size -> segments
            else -> pattern.segments
        }
        val pathMatches = mutableListOf<SegmentMatch>()
        for (i in p.indices) {
            val match = segments[i].matches(pattern.segments[i]) ?: return null
            pathMatches.add(match)
        }
        return UrlMatch(trail(), pattern.trail(), pathMatches.toList())
    }

    private fun String.matches(configPath: String): SegmentMatch? {
        if (configPath == "*") return WildCardMatch(this)
        if (configPath.startsWith(":")) {
            val param = configPath.substringAfter(":")
            return DynamicParamMatch(this, param, this)
        }
        if (configPath.startsWith("{")) {
            val param = configPath.removePrefix("{").removeSuffix("}")
            return DynamicParamMatch(this, param, this)
        }
        if (configPath.startsWith("[")) {
            val param = configPath.removePrefix("[").removeSuffix("]")
            return DynamicParamMatch(this, param, this)
        }
        if (configPath == this) return ExactMatch(this)
        return null
    }
}