package kiota.internal

import kiota.DynamicParamMatch
import kiota.ExactMatch
import kiota.SegmentMatch
import kiota.WildCardMatch
import kollections.toIMap


private fun SegmentMatch.toScore() = when (this) {
    is WildCardMatch -> 1
    is DynamicParamMatch -> 2
    is ExactMatch -> 3
}

internal fun Collection<SegmentMatch>.toScore(): Int {
    var score = 0
    forEachIndexed { idx, segment ->
        val level = idx + 1
        score += level * segment.toScore()
    }
    return score
}

internal fun Collection<SegmentMatch>.toEvaluatedUrl() = joinToString("/") { it.path }

internal fun Collection<SegmentMatch>.toParams() = filterIsInstance<DynamicParamMatch>().associate { it.key to it.value }.toIMap()