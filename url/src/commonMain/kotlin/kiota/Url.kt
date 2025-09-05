@file:JsExport
@file:Suppress("NON_EXPORTABLE_TYPE")

package kiota

import kollections.List
import kotlinx.JsExport
import kotlinx.JsExportIgnore
import kotlin.js.JsName

/**
 * A representation of a Url
 */
interface Url {
    /**
     * The scheme of the url (i.e. https, http)
     * e.g. For a url with https://test.com/john/doe
     * scheme should give you "https"
     */
    val scheme: String

    /**
     * The domain of the url (i.e. test.com)
     * e.g. For a url with https://test.com/john/doe
     * domain should give you "test.com"
     */
    val domain: String

    /**
     * The list of the path segments of the url
     * e.g. For a url with https://test.com/john/doe
     * segments should give you ["john","doe"]
     */
    val segments: List<String>

    /**
     * The root (scheme + domain) of the url
     * e.g. For a url with https://test.com/john/doe
     * root should give you "https://test.com"
     */
    val root: String

    /**
     * The path url without the domain and scheme
     * e.g. For a url with https://test.com/john/doe
     * path should give you "john/doe"
     */
    val path: String

    /**
     * The query params from this url
     */
    val params: QueryParams

    /**
     * resolves the url with the same [root] but at the provided [path]
     */
    fun at(path: String, query: Boolean = false): Url

    /**
     * Appends adds a path segment to the current list of [segments]
     */
    fun child(url: String, query: Boolean = false): Url

    /**
     * Replaces the last path segment with the provided [url]
     */
    fun sibling(url: String): Url

    /**
     * Resolves a path relative to this url
     */
    fun resolve(path: String, query: Boolean = false): Url

    @JsName("rebaseUrl")
            /**
             * Removes the base common base of between this [Url] and the provided [url]
             *
             * e.g.
             * 1. Rebasing /posts/\* with /posts/1/test should result in /1/test
             * 2. Rebasing /posts/{uid}/\* with /posts/1/test should result in /test
             * 3. Rebasing / with /test/john/doe should result in /test/john/doe
             */
    fun rebase(url: Url): Url

    /**
     * Removes the base common base of between this [Url] and the provided [url]
     *
     * e.g.
     * 1. Rebasing /posts/\* with /posts/1/test should result in /1/test
     * 2. Rebasing /posts/{uid}/\* with /posts/1/test should result in /test
     * 3. Rebasing / with /test/john/doe should result in /test/john/doe
     */
    fun rebase(url: String): Url

    /**
     * @return the [path] as a [Url] eliminating all its [root] ([scheme]:[domain])
     */
    fun trail(): Url

    /**
     * @return the same url with query params
     */
    fun withParams(name: String, value: String): Url

    /**
     * @return the same url with query params
     */
    @JsExportIgnore
    fun withParams(name: String, value: Number): Url

    /**
     * @return the same url with query params as provided
     */
    @JsExportIgnore
    fun withParams(vararg params: Pair<String, String>): Url

    fun matches(pattern: String): UrlMatch?

    @JsName("matchesUrl")
    fun matches(pattern: Url): UrlMatch?
}