@file:Suppress("NOTHING_TO_INLINE")

package kiota

import kollections.Collection
import kollections.toList

inline fun UrlMatch(
    route: Url,
    pattern: Url,
    segments: Collection<SegmentMatch>
): UrlMatch = UrlMatch(route, pattern, segments.toList())