package kiota

import kommander.expect
import kotlin.test.Test

class UrlSegmentTest {
    @Test
    fun should_be_able_to_disambiguate_the_query_params_from_the_last_segments() {
        val url = Url("app://captain.com/pacha/wako?zuzu=45")
        val last = url.segments.last()
        expect(last).toBe("wako")
    }

    @Test
    fun should_be_able_to_disambiguate_the_query_params_from_the_last_segment_after_rebaseing() {
        var url = Url("app://captain.com/pacha/wako?zuzu=45")
        url = url.rebase("wenu")
        val last = url.segments.last()
        expect(last).toBe("wenu")
    }

    @Test
    fun should_not_confuse_relative_urls_with_domains() {
        val url = Url("../that")
        expect(url.domain).toBe("")
        expect(url.segments[0]).toBe("..")
    }
}