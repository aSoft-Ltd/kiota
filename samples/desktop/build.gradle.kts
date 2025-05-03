import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
}

kotlin {
    jvm {
        compilations.all {
            compileTaskProvider {
                compilerOptions.jvmTarget = JvmTarget.JVM_17
            }
        }
        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.kiotaSamplesShared)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "kiota.samples.MainKt"

        buildTypes.release.proguard {
            version = "7.4.0"
            isEnabled = true
            optimize = true
            obfuscate = true
            joinOutputJars = true
        }
    }
}
