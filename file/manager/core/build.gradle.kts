import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "A kotlin multiplatform abstraction for reading files as blobs"

configureAndroid("src/androidMain") {
    namespace = "tz.co.asoft.system.file.manager"
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
    val iosTargets = if (Targeting.OSX) iosTargets() else listOf()
//    val ndkTargets = if (Targeting.NDK) ndkTargets() else listOf()
    val linuxTargets = if (Targeting.LINUX) linuxTargets() else listOf()
//    val mingwTargets = if (Targeting.MINGW) mingwTargets() else listOf()
    val nativeTargets = iosTargets + linuxTargets

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.kiotaFilePickerCore)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.koncurrent.later.test)
                implementation(kotlinx.serialization.json)
                implementation(libs.kommander.coroutines)
            }
        }

        val wasmMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(kotlinx.browser)
            }
        }

        if(Targeting.WASM) {
            val wasmJsMain by getting {
                dependsOn(wasmMain)
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
        }

        val linuxMain by creating {
            dependsOn(commonMain)
        }

        iosTargets.forEach {
            val main by it.compilations.getting {}
            main.defaultSourceSet {
                dependsOn(iosMain)
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