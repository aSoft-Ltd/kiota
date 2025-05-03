import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "A kotlin multiplatform abstraction for choosing files on all platforms"

configureAndroid("src/androidMain") {
    namespace = "tz.co.asoft.system.file.chooser"
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
//    val osxTargets = if (Targeting.OSX) (iosTargets() + tvOsTargets()) else listOf()
    val osxTargets = if (Targeting.OSX) (iosTargets()) else listOf()
//    val ndkTargets = if (Targeting.NDK) ndkTargets() else listOf()
    val linuxTargets = if (Targeting.LINUX) linuxTargets() else listOf()
//    val mingwTargets = if (Targeting.MINGW) mingwTargets() else listOf()
    val nativeTargets = osxTargets + linuxTargets

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.kiotaFilePickerCore)
            }
        }

        val androidMain by getting {
            dependencies {
                api(androidx.activity.ktx)?.because("We need it to check permissions while picking files")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.koncurrent.later.coroutines)
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

        val osxMain by creating {
            dependsOn(commonMain)
            dependencies {

            }
        }

        osxTargets.forEach {
            val main by it.compilations.getting {}
            main.defaultSourceSet {
                dependsOn(osxMain)
            }
        }

//        val linuxMain by creating {
//            dependsOn(commonMain)
//        }
//
//        linuxTargets.forEach {
//            val main by it.compilations.getting {}
//            main.defaultSourceSet {
//                dependsOn(linuxMain)
//            }
//        }
    }
}

rootProject.tasks.withType<KotlinNpmInstallTask>().configureEach {
    args.add("--ignore-engines")
}

tasks.named("wasmJsTestTestDevelopmentExecutableCompileSync").configure {
    mustRunAfter(tasks.named("jsBrowserTest"))
    mustRunAfter(tasks.named("jsNodeTest"))
}