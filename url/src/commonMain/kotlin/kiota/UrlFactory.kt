@file:Suppress("NOTHING_TO_INLINE")

package kiota

import kiota.internal.QueryParamsImpl
import kiota.internal.UrlImpl
import kotlinx.JsExport
import kotlin.js.JsName

@JsExport
@JsName("createUrl")
inline fun Url(path: String): Url = UrlImpl(path)

@JsExport
@JsName("createUrlFrom")
inline fun Url(
    scheme: String = "",
    domain: String = "",
    vararg paths: String
): Url = UrlImpl(scheme, domain, null, paths.toList(), QueryParamsImpl.empty)

@JsExport
@JsName("createUrlFrom")
inline fun Url(
    scheme: String = "",
    host: String = "",
    port: Int,
    vararg paths: String
): Url = UrlImpl(scheme, "$host:$port", port, paths.toList(), QueryParamsImpl.empty)

inline fun String.toUrl() = Url(this)