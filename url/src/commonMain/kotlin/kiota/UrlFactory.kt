@file:Suppress("NOTHING_TO_INLINE")

package kiota

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
): Url = UrlImpl(scheme, domain, paths.toList())

inline fun String.toUrl() = Url(this)