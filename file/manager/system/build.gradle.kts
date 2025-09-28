plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "A kotlin multiplatform abstraction for managing files provided by systems on different platforms"

configureAndroid("src/androidMain") {
    namespace = "tz.co.asoft.kiota.file.manager.system"
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
    if (Targeting.JS) js(IR) { library() } // untill https://youtrack.jetbrains.com/issue/KT-80014 gets fixed // untill https://youtrack.jetbrains.com/issue/KT-80014 gets fixed
    if (Targeting.WASM) wasmJs { library() }
    val iosTargets = if (Targeting.OSX) iosTargets() else listOf()

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.kiotaFileManagerCore)
                api(projects.kiotaFilePickerSystem)
            }
        }

        webMain.dependencies {
            api(kotlinx.browser)
        }

        val wasmMain by creating {
            dependsOn(commonMain)
            dependencies {
                api(kotlinx.browser)
            }
        }

        if (Targeting.WASM) {
            val wasmJsMain by getting {
                dependsOn(wasmMain)
            }
        }

        val iosMain by getting {
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
    }
}
