plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "A manual connection observer and verifier most suitable for testing environments"

kotlin {
    if (Targeting.JVM) jvm { library() }
    if (Targeting.JS) js(IR) { library() } //
    if (Targeting.WASM) wasmJs { library() }
    if (Targeting.WASM) wasmWasi { library() }
    if (Targeting.OSX) osxTargets() else listOf()
    if (Targeting.LINUX) linuxTargets() else listOf()
    if (Targeting.MINGW) mingwTargets() else listOf()

    sourceSets {
        commonMain.dependencies {
            api(projects.kiotaConnectionCore)
        }
    }
}
