import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
}

kotlin {
    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "sample.js"
                sourceMaps = true
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.kiotaSamplesShared)
        }

        wasmJsMain.dependencies {
            implementation(compose.components.resources)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
        }
    }
}

compose {
    web { }
}

rootProject.the<NodeJsRootExtension>().apply {
    version = npm.versions.node.version.get()
    downloadBaseUrl = npm.versions.node.url.get()
}

rootProject.tasks.withType<KotlinNpmInstallTask>().configureEach {
    args.add("--ignore-engines")
}