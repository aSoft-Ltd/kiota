package kiota.internal

import kiota.DynamicParamMatch
import kiota.ExactMatch
import kiota.SegmentMatch
import kiota.Url
import kiota.UrlMatch
import kiota.WildCardMatch
import kollections.List
import kollections.add
import kollections.get
import kollections.getOrNull
import kollections.indices
import kollections.isEmpty
import kollections.iterator
import kollections.joinToString
import kollections.last
import kollections.lastOrNull
import kollections.minus
import kollections.mutableListOf
import kollections.mutableSetOf
import kollections.plus
import kollections.size
import kollections.toList

@PublishedApi
internal class UrlImpl(
    override val scheme: String,
    override val domain: String,
    override val segments: List<String>,
    override val params: QueryParamsImpl
) : Url {
    companion object {

        private val String.isDomainLike: Boolean
            get() = (contains(".") && !startsWith(".")) || (contains(":") && !startsWith(":"))

        private fun scheme(value: String): String {
            if (value.contains("?")) return scheme(value.substringBefore("?"))
            if (value.contains("://")) return value.substringBefore("://").lowercase()
            return ""
        }

        private fun String.schemeless(scheme: String) : String {
            if(scheme.isEmpty()) return this
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
            val domain = if (first?.isDomainLike == true) first else ""
            val paths = segments - domain
            return UrlImpl(
                scheme = protocol,
                domain = domain,
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
        return UrlImpl(scheme, domain, s, p)
    }

    override fun at(path: String): Url {
        val next = UrlImpl(path)
        val p = QueryParamsImpl.from(entries = params.entries + next.params.entries)
        return UrlImpl(scheme, domain, next.segments, p)
    }

    override fun child(url: String): Url {
        val next = UrlImpl(url)
        val p = QueryParamsImpl.from(entries = params.entries + next.params.entries)
        return UrlImpl(scheme, domain, segments + next.segments, p)
    }

    override val path = "/${segments.joinToString(separator = "/")}"

    override fun trail(): Url = UrlImpl(scheme = "", domain = "", segments = segments, params = params)

    override fun resolve(path: String): Url = when {
        path.startsWith("/") -> at(path)
        path.startsWith(".") -> relativePathResolver(path)
        segments.isEmpty() -> at(path)
        Url(path).domain.isNotBlank() -> Url(path)
        else -> child(path)
    }

    private fun relativePathResolver(path: String): Url {
        var out = segments
        val next = UrlImpl(path)
        for (segment in next.segments) when {
            segment == "." -> {}
            segment == ".." -> out -= (out.lastOrNull() ?: "")
            else -> out += segment
        }
        val p = QueryParamsImpl.from(entries = params.entries + next.params.entries)
        return UrlImpl(scheme, domain, out, p)
    }

    override val root = buildString {
        if (scheme.isNotBlank()) {
            append(scheme)
            append("://")
        }
        if (domain.isNotBlank()) {
            append(domain)
        }
    }

    override fun withParams(vararg params: Pair<String, String>): Url {
        val p = buildMap {
            putAll(this@UrlImpl.params.entries)
            putAll(params)
        }
        return UrlImpl(scheme, domain, segments, QueryParamsImpl.from(p))
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
        return UrlImpl(url.scheme, url.domain, (url.segments - matchingSegments), params)
    }

    override fun rebase(url: String) = rebase(UrlImpl(url))

    override fun matches(pattern: String): UrlMatch? = matches(UrlImpl(pattern))

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