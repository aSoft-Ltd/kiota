package kiota

import kommander.expect
import kotlin.test.Test

class UrlResolverTest {
    @Test
    fun should_be_able_to_go_back() {
        val url = Url("google.com/meet/user")
        expect(url.resolve("")).toBe(Url("google.com/meet/user"))
    }

    @Test
    fun should_resolve_back_like_a_file_system_route() {
        val url = Url("google.com")
        expect(url.resolve("")).toBe(Url("google.com"))
    }

    @Test
    fun should_resolve_to_a_child_on_a_relative_route_like_in_a_file_system() {
        val url = Url("google.com/meet/user")
        expect(url.resolve("./admin")).toBe(Url("google.com/meet/user/admin"))
    }

    @Test
    fun should_resolve_to_a_child_on_a_relative_route() {
        val url = Url("google.com/meet/user")
        expect(url.resolve("admin")).toBe(Url("google.com/meet/user/admin"))
    }

    @Test
    fun should_resolve_to_sibling_route_as_a_file_system_would() {
        val url = Url("google.com/meet/user")
        expect(url.resolve("../admin")).toBe(Url("google.com/meet/admin"))
    }

    @Test
    fun should_resolve_to_an_absolute_path() {
        val url = Url("google.com/meet/user")
        expect(url.resolve("/john/doe")).toBe(Url("google.com/john/doe"))
    }

    @Test
    fun should_be_able_to_resolve_email_addresses() {
        val url = Url("google.com/meet/user")
        expect(url.resolve("/john/doe/jane@doe.com")).toBe(Url("google.com/john/doe/jane@doe.com"))
    }

    @Test
    fun should_not_resolve_relatively_if_the_resolution_is_also_a_full_link() {
        val url = Url("app://test.com/that/works")
        expect(url.resolve("app://test.com/this/should/work")).toBe(Url("app://test.com/this/should/work"))
    }

    @Test
    fun should_not_resolve_relatively_if_the_resolution_is_also_a_full_link_on_a_different_domain() {
        val url = Url("app://test1.com/that/works")
        expect(url.resolve("app://test2.com/this/should/work")).toBe(Url("app://test2.com/this/should/work"))
    }

    @Test
    fun should_resolve_child_url_without_query_parameters_by_default() {
        val url = Url("google.com/meet/user?token=abc")
        expect(url.resolve("admin")).toBe(Url("google.com/meet/user/admin"))
    }

    @Test
    fun should_resolve_child_url_without_query_parameters_when_query_is_set_to_false() {
        val url = Url("google.com/meet/user?token=abc")
        expect(url.resolve("admin", query = false)).toBe(Url("google.com/meet/user/admin"))
    }

    @Test
    fun should_resolve_child_url_with_query_parameters_when_query_is_set_to_true() {
        val url = Url("google.com/meet/user?token=abc")
        expect(url.resolve("admin", query = true)).toBe(Url("google.com/meet/user/admin?token=abc"))
    }

    @Test
    fun should_resolve_sibling_url__query_parameters() {
        val url = Url("google.com/meet/user?token=abc")
        expect(url.resolve("../admin", query = false)).toBe(Url("google.com/meet/admin"))
    }
}