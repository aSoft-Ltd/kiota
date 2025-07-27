plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
    alias(kotlinz.plugins.dokka)
}

description = "A compose way of reading local files and displaying them in compose ui"

configureAndroid("src/androidMain") {
    namespace = "tz.co.asoft.kiota.file.compose"
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
    androidTarget { library() }
    jvm { library() }
    js { browser() }
    wasmJs { browser() }

    val ios = listOf(iosArm64(), iosX64(), iosSimulatorArm64())

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(projects.kiotaFileManagerCore)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                api(projects.kiotaFileManagerSystem)?.because("We need to add tooling on AndroidFileManager")
            }
        }

        val skiaMain by creating {
            dependsOn(commonMain)
        }

        val iosMain by creating {
            dependsOn(commonMain)
            dependsOn(skiaMain)
        }

        val jvmMain by getting {
            dependsOn(skiaMain)
        }

        val wasmJsMain by getting {
            dependsOn(skiaMain)
        }

        val jsMain by getting {
            dependsOn(skiaMain)
        }

        for (device in ios) {
            val main by device.compilations
            main.defaultSourceSet.dependsOn(iosMain)
        }
    }
}


repositories {
    mavenCentral()
}