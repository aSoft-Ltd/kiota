import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
}

android {
    namespace = "system.samples"
    compileSdk = androidx.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = 25
        targetSdk = androidx.versions.compile.sdk.get().toInt()
        applicationId = "system.samples"
        versionCode = 3
        versionName = "0.1"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
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
        }
    }

    signingConfigs {
        val release by creating {
            keyAlias = "key0"
            storeFile = file("keystore/release.keystore")
            keyPassword = "EleganceInEducation"
            storePassword = "EleganceInEducation"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
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
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.kiotaSamplesShared)
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(kotlinx.coroutines.android)
            implementation("androidx.activity:activity-compose:1.9.2")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
        }
    }
}