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

listOf("kommander", "kollections", "kase", "kotlinx-interoperable").forEach { includeBuild("../$it") }

rootProject.name = "kiota"

// submodules
includeSubs("kiota", ".", "url", "sse", "files")
includeSubs("kiota-file", "file", "core", "system", "virtual", "compose")
includeSubs("kiota-file-picker", "file/picker", "core", "system", "virtual")
includeSubs("kiota-file-manager", "file/manager", "core", "system", "virtual")
includeSubs("kiota-samples", "samples", "shared", "desktop", "browser", "android")