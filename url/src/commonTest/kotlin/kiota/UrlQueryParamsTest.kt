package kiota

import kommander.expect
import kotlin.test.Test

class UrlQueryParamsTest {
    @Test
    fun should_be_able_to_identify_query_params() {
        val url = Url("/test/me?p=45")
        expect(url.params["p"]).toBe("45")

        val p by url.params
        expect(p).toBe("45")

        expect(url.path).toBe("/test/me")
    }

    @Test
    fun should_be_able_to_identify_query_params_in_a_type_safe_manner() {
        val url = Url("/test/me?q=45")

        val q by url.params.cast { it.toInt() }
        expect(q).toBe(45)

        expect(url.path).toBe("/test/me")
    }

    @Test
    fun should_be_able_to_identify_query_params_into_an_int() {
        val url = Url("/test/me")
        val p by url.params.cast { it.toIntOrNull() ?: 40 }
        expect(p).toBe(40)
    }

    @Test
    fun should_throw_when_forced_to_cast_a_query_param_to_a_non_exisisting() {
        val url = Url("/test/me")
        val p by url.params.intOrNull()
        expect(p).toBe(null)
    }
}