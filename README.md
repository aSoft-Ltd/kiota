# kiota

Kotlin multiplatform I/O tools designed to provide a consistent API for working with URLs, files, server-sent events, and network connectivity across different platforms (JVM, Android, iOS, macOS, Browser/JS, and Wasm).

## Table of Contents
- [Modules](#modules)
- [Setup](#setup)
- [Simple Usage](#simple-usage)
    - [URL Manipulation](#url-manipulation)
    - [File Management](#file-management)
    - [Connectivity Monitoring](#connectivity-monitoring)
- [Platform Support](#platform-support)

---

## Modules

The library is composed of several modules, allowing you to pick only what you need:

- **`kiota-url`**: A multiplatform URL representation and manipulation library.
- **`kiota-file-core`**: Core abstractions for files and file-like objects.
- **`kiota-file-manager`**: Multiplatform file management (Create, Open, Export, Read).
- **`kiota-file-picker`**: Multiplatform file and directory picking functionality.
- **`kiota-sse`**: Multiplatform Server-Sent Events (SSE) support.
- **`kiota-connection`**: Tools for monitoring network connectivity.

---

## Setup

Add the following to your `build.gradle.kts` file:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    // Choose the modules you need
    implementation("tz.co.asoft:kiota-url:<version>")
    implementation("tz.co.asoft:kiota-file-manager:<version>")
    implementation("tz.co.asoft:kiota-file-picker:<version>")
    implementation("tz.co.asoft:kiota-sse:<version>")
    implementation("tz.co.asoft:kiota-connection-core:<version>")
}
```

---

## Simple Usage

### URL Manipulation (`kiota-url`)

```kotlin
import kiota.Url

val url = Url("https://example.com/api/v1")
val child = url.child("users").withParams("page", "1")

println(child.toString()) // https://example.com/api/v1/users?page=1
println(child.host)      // example.com
println(child.scheme)    // https
```

### File Management (`kiota-file-manager`)

Kiota provides a `FileManager` interface with platform-specific implementations (e.g., `JvmFileManager`, `SystemFileManager` for Android, etc.).

```kotlin
// In a Compose Multiplatform environment
val files: FileManager = JvmFileManager() // or BrowserFileManager(), etc.

// Picking a file
val result = files.picker().open()
if (result is File) {
    val info = files.info(result)
    println("Picked: ${info.name()}, Size: ${info.size()}")
}

// Creating a file
files.create(
    content = "Hello World".encodeToByteArray(),
    name = "hello.txt"
)
```

### Connectivity Monitoring (`kiota-connection`)

```kotlin
import kiota.ConnectionObserver

// Implementation varies by platform
val observer: ConnectionObserver = // ... platform specific observer

observer.status.observe { status ->
    when (status) {
        is Connected -> println("Back online!")
        is Disconnected -> println("Connection lost.")
        else -> println("Connecting...")
    }
}

observer.start()
```

---

## Platform Support

| Module           | JVM | Android | iOS | macOS | Browser (JS/Wasm) |
|:-----------------|:---:|:-------:|:---:|:-----:|:-----------------:|
| kiota-url        |  ✅  |    ✅    |  ✅  |   ✅   |         ✅         |
| kiota-file       |  ✅  |    ✅    |  ✅  |   ✅   |         ✅         |
| kiota-sse        |  ✅  |    ✅    |  ✅  |   ✅   |         ✅         |
| kiota-connection |  ✅  |    ✅    |  ✅  |   ✅   |         ✅         |
