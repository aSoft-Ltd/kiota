plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "A kotlin multiplatform abstraction for choosing files on different file systems provided by their respective platforms"

configureAndroid("src/androidMain") {
    namespace = "tz.co.asoft.kiota.file.picker.system"
    compileSdkVersion(apiLevel = androidx.versions.compile.sdk.get().toInt())
    defaultConfig {
        minSdk = 8
    }

    lintOptions {
        isWarningsAsErrors = false
        isAbortOnError = false
        isIgnoreTestSources = true
    }
}

kotlin {
    if (Targeting.ANDROID) androidTarget { library() }
    if (Targeting.JVM) jvm { library() }
    if (Targeting.JS) js(IR) { library() }
    if (Targeting.WASM) wasmJs { library() }
    val osxTargets = if (Targeting.OSX) (iosTargets()) else listOf()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.kiotaFilePickerCore)
                api(projects.kiotaFileSystem)
                api(kotlinx.coroutines.core)
            }
        }

        val androidMain by getting {
            dependencies {
                api(androidx.activity.ktx)?.because("We need it to check permissions while picking documents")
            }
        }

        val wasmMain by creating {
            dependsOn(commonMain)
        }

        if (Targeting.WASM) {
            val wasmJsMain by getting {
                dependsOn(wasmMain)
            }
        }

        val osxMain by creating {
            dependsOn(commonMain)
        }

        osxTargets.forEach {
            val main by it.compilations.getting {}
            main.defaultSourceSet {
                dependsOn(osxMain)
            }
        }
    }
}
