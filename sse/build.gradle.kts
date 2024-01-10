plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "A kotlin multiplatform library for representing headless collection based ui such as lists, tables and grids"

kotlin {
    if (Targeting.JVM) jvm { library() }
    if (Targeting.JS) js(IR) { library(testTimeout = 10000) }
    if (Targeting.WASM) wasmJs { library() }
    if (Targeting.WASM) wasmWasi { library() }
    val osxTargets = if (Targeting.OSX) osxTargets() else listOf()
//    val ndkTargets = if (Targeting.NDK) ndkTargets() else listOf()
    val linuxTargets = if (Targeting.LINUX) linuxTargets() else listOf()
//    val mingwTargets = if (Targeting.MINGW) mingwTargets() else listOf()

    val nativeTargets = osxTargets + /*ndkTargets + mingwTargets */ linuxTargets

    sourceSets {

        val commonMain by getting {
            dependencies {

            }
        }

        jvmMain.dependencies {
            api(kotlinx.coroutines.core)
        }

        val unknownMain by creating {
            dependsOn(commonMain)
        }

        if(Targeting.WASM) {
            val wasmJsMain by getting {
                dependsOn(unknownMain)
            }

            val wasmWasiMain by getting {
                dependsOn(unknownMain)
            }
        }

        nativeTargets.forEach {
            val main by it.compilations
            main.defaultSourceSet { dependsOn(unknownMain) }
        }
    }
}