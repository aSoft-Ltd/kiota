@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
    alias(kotlinz.plugins.dokka)
}

description = "The compose sdk to assist in building of the apps"

android {
    namespace = "tz.co.asoft.kiota.compose"
    compileSdk = androidx.versions.compile.sdk.get().toInt()
    defaultConfig {
        minSdk = 25 // because of the coil dependency has this as it's min sdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/*.kotlin_module"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider {
                compilerOptions.jvmTarget = JvmTarget.JVM_17
            }
        }
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.unitTest)
    }

    jvm {
//        tasks.withType<Test> {
//            useJUnitPlatform()
//        }
    }

    wasmJs {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    useConfigDirectory(project.projectDir.resolve("karma.config.d").resolve("wasm"))
                }
            }
        }
    }

    val ios = listOf(iosArm64(), iosX64(), iosSimulatorArm64())

    for (device in ios) {
        device.binaries.framework {
            baseName = "SystemSampleUI"
            binaryOption("bundledId", "tz.co.asoft.kiota.sample")
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                api(compose.materialIconsExtended)
                implementation(compose.components.resources)
                api(projects.kiotaFileManagerSystem)
                api(projects.kiotaFileCompose)
            }
        }

        val commonTest by getting {
            dependencies {

            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {

            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(ktor.client.darwin)
            }
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

compose {
    resources {
        packageOfResClass = "kiota.generated.resources"
    }
}
