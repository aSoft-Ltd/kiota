plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "A connection observer and verifier implementation using http calls"

kotlin {
    if (Targeting.JVM) jvm { library() }
    if (Targeting.JS) js(IR) { library() } // // untill https://youtrack.jetbrains.com/issue/KT-80014 gets fixed
    if (Targeting.WASM) wasmJs { library() }
//    if (Targeting.WASM) wasmWasi { library() } // Because ktor client doesn't support wasi yet
    if (Targeting.OSX) osxTargets() else listOf()
//    if (Targeting.NDK) ndkTargets() else listOf()
    if (Targeting.LINUX) linuxTargets() else listOf()
    if (Targeting.MINGW) mingwTargets() else listOf()

    sourceSets {
        commonMain.dependencies {
            api(projects.kiotaConnectionCore)
            api(ktor.client.core)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kommander.core)
            implementation(kotlinx.coroutines.test)
            implementation(libs.kommander.coroutines)
        }

        jvmTest.dependencies {
            implementation(ktor.client.cio)
        }

        appleTest.dependencies {
            implementation(ktor.client.darwin)
        }
    }
}
