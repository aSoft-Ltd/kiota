import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "A kotlin multiplatform abstraction for reading files as blobs"

configureAndroid("src/androidMain") {
    namespace = "tz.co.asoft.system.file.core"
    compileSdkVersion(apiLevel = androidx.versions.compile.sdk.get().toInt())
    defaultConfig {
        minSdk = 8
    }
}

kotlin {
    if (Targeting.ANDROID) androidTarget { library() }
    if (Targeting.JVM) jvm { library() }
    if (Targeting.JS) js(IR) { library() }
    if (Targeting.WASM) wasmJs { library() }
//    if (Targeting.WASM) wasmWasi { library() }
//    val osxTargets = if (Targeting.OSX) osxTargets() else listOf()
    val osxTargets = if (Targeting.OSX) (iosTargets() + macOsTargets()) else listOf()
//    val ndkTargets = if (Targeting.NDK) ndkTargets() else listOf()
    val linuxTargets = if (Targeting.LINUX) linuxTargets() else listOf()
//    val mingwTargets = if (Targeting.MINGW) mingwTargets() else listOf()

    sourceSets {
        val commonMain by getting {
            dependencies {
//                api(libs.koncurrent.later.core)
//                api(kotlinx.coroutines.core)
//                api(libs.kase.core)
                api(libs.kotlinx.exports)
                api(kotlinx.serialization.core)
            }
        }

        val commonTest by getting {
            dependencies {
//                implementation(libs.koncurrent.later.test)
                implementation(kotlinx.serialization.json)
//                implementation(libs.kommander.coroutines)
            }
        }

        val wasmMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(kotlinx.browser)
            }
        }

        if (Targeting.WASM) {
            val wasmJsMain by getting {
                dependsOn(wasmMain)
            }
        }

        val darwinMain by creating {
            dependsOn(commonMain)
        }

        val linuxMain by creating {
            dependsOn(commonMain)
        }

        osxTargets.forEach {
            val main by it.compilations.getting {}
            main.defaultSourceSet {
                dependsOn(darwinMain)
            }
        }

        linuxTargets.forEach {
            val main by it.compilations.getting {}
            main.defaultSourceSet {
                dependsOn(linuxMain)
            }
        }
    }
}

rootProject.tasks.withType<KotlinNpmInstallTask>().configureEach {
    args.add("--ignore-engines")
}

tasks.named("wasmJsTestTestDevelopmentExecutableCompileSync").configure {
    mustRunAfter(tasks.named("jsBrowserTest"))
    mustRunAfter(tasks.named("jsNodeTest"))
}