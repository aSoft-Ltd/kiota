pluginManagement {
    includeBuild("../build-logic")
}

plugins {
    id("multimodule")
}

fun includeSubs(base: String, path: String = base, vararg subs: String) {
    subs.forEach {
        include(":$base-$it")
        project(":$base-$it").projectDir = File("$path/$it")
    }
}

listOf("kommander", "kollections", "kase", "lexi").forEach { includeBuild("../$it") }

rootProject.name = "kiota"

// submodules
includeSubs("kiota", ".", "url", "sse", "files")
includeSubs("kiota-file", "file", "core", "system", "test", "compose")
includeSubs("kiota-file-picker", "file/picker", "core", "system", "test")
includeSubs("kiota-file-manager", "file/manager", "core", "system", "test")
includeSubs("kiota-samples", "samples", "shared", "desktop", "browser", "android")