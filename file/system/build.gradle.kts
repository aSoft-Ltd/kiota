plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "A kotlin multiplatform implementation of files on diffrent file systems"

configureAndroid("src/androidMain") {
    namespace = "tz.co.asoft.kiota.file.system"
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
    if (Targeting.JS) js(IR) { library() } //
    if (Targeting.WASM) wasmJs { library() }
    val osxTargets = if (Targeting.OSX) (iosTargets() + macOsTargets()) else listOf()
    val linuxTargets = if (Targeting.LINUX) linuxTargets() else listOf()

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.kiotaFileCore)
                api(kotlinx.coroutines.core)
            }
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

        val darwinMain by creating {
            dependsOn(commonMain)
        }

        val linuxMain by getting {
            dependsOn(commonMain)
        }

        webMain.dependencies {
            api(kotlinx.browser)
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
