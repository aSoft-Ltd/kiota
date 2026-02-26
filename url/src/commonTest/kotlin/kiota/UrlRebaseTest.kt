package kiota

import kommander.expect
import kotlin.test.Test

class UrlRebaseTest {
    @Test
    fun should_rebase_a_string() {
        val url = Url("/posts/*")
        expect(url.rebase("/posts/1/test").path).toBe("/1/test")
    }

    @Test
    fun should_rebase_a_dynamic_string() {
        val url = Url("/posts/{uid}/*")
        expect(url.rebase("/posts/1/test").path).toBe("/test")
    }

    @Test
    fun rebasing_a_root_url_should_have_no_effect() {
        val url = Url("/")
        expect(url.rebase("/test/john/doe").path).toBe("/test/john/doe")
    }

    @Test
    fun should_rebase_a_two_dynamic_routes_with_the_same_string() {
        val url = Url("/campuses/{campus}/*")
        expect(url.rebase("/campuses/1/curriculums/1").path).toBe("/curriculums/1")
    }
}