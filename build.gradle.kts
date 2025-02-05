import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

buildscript {
    val antlrKotlinVersion = "36deef96aa" // commit hash for custom antlr runtime
    extra.set("antlrKotlinVersion", antlrKotlinVersion)

    dependencies {
        classpath("com.github.alchitry.antlr-kotlin:antlr-kotlin-gradle-plugin:$antlrKotlinVersion")
    }
}

val antlrKotlinVersion = extra.get("antlrKotlinVersion") as String

plugins {
    kotlin("jvm") version "2.1.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
    kotlin("plugin.serialization") version "2.0.10"
    id("org.jetbrains.compose") version "1.7.3"
    id("dev.hydraulic.conveyor") version "1.10"
    id("at.stnwtr.gradle-secrets-plugin") version "1.0.1"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

val fullVersion = "2.0.27-BETA"
val numOnlyVersion = fullVersion.split('-').first()

group = "com.alchitry"
version = numOnlyVersion

repositories {
    google()
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}

javafx {
    version = "21.0.1"
    modules("javafx.controls", "javafx.swing")
}

dependencies {
    implementation("com.github.alchitry.antlr-kotlin:antlr-kotlin-runtime:$antlrKotlinVersion")
    implementation("org.apache.commons:commons-text:1.13.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    implementation(kotlin("reflect"))
    implementation("org.jdom:jdom2:2.0.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
    implementation("org.usb4java:usb4java:1.3.0")
    macAarch64("io.github.dsheirer:libusb4java-darwin-aarch64:1.3.1") // ARM Mac binary
    implementation("com.fazecast:jSerialComm:2.11.0")
    implementation("com.github.alchitry.yad2xx:yad2xxJava:8d48cda")
    implementation("me.tongfei:progressbar:0.10.1")
    implementation("org.jline:jline:3.28.0")
    implementation("org.fusesource.jansi:jansi:2.4.1")
    implementation("li.flor:native-j-file-chooser:1.6.4")

    linuxAmd64(compose.desktop.linux_x64)
    linuxAarch64(compose.desktop.linux_arm64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)

    implementation("org.jetbrains.compose.material3:material3-desktop:1.7.3")

    testImplementation(kotlin("test"))
}

configurations.all {
    attributes {
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.add("-opt-in=kotlin.contracts.ExperimentalContracts")
        freeCompilerArgs.add("-opt-in=kotlin.ExperimentalStdlibApi")
        freeCompilerArgs.add("-opt-in=kotlinx.serialization.ExperimentalSerializationApi")
        freeCompilerArgs.add("-opt-in=androidx.compose.foundation.ExperimentalFoundationApi")
        freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

kotlin {
    jvmToolchain(17)
}

compose.desktop {
    application {
        mainClass = "com.alchitry.labs2.GUIKt"
    }
}

val conveyorCommand = "/home/justin/.npm-global/bin/conveyor"
fun TaskContainer.registerConveyorTask(name: String, arg: String? = null) {
    register(name) {
        group = "conveyor"
        dependsOn.add("jar")
        doLast {
            exec {
                if (arg == null) {
                    commandLine(conveyorCommand, "--passphrase=${secrets.get("conveyorRootKey")}", "make", name)
                } else {
                    commandLine(conveyorCommand, "--passphrase=${secrets.get("conveyorRootKey")}", arg, "make", name)
                }
            }
        }
    }
}

tasks.register("version") {
    group = "conveyor"
    doLast { exec { commandLine(conveyorCommand, "--version") } }
}
tasks.registerConveyorTask("app")
tasks.registerConveyorTask("site") // makes the site files locally
tasks.registerConveyorTask("windows-msix") // builds a windows msix
tasks.registerConveyorTask("windows-app") // builds a windows app
tasks.registerConveyorTask("ms-store-release") // publish to microsoft store
tasks.registerConveyorTask("copied-site") // pushes the site files to GitHub
tasks.registerConveyorTask("mac-app", "-Kapp.machines=mac.aarch64")

val generatedVersionDir = "${layout.buildDirectory.get()}/resources/main"

tasks.register("generateVersionProperties") {
    doLast {
        val propertiesFile = file("$generatedVersionDir/version.properties")
        propertiesFile.parentFile.mkdirs()
        val properties = Properties()
        properties.setProperty("version", fullVersion)
        val out = FileOutputStream(propertiesFile)
        properties.store(out, null)
    }
}

tasks.named("compileKotlin") {
    dependsOn("processResources")
}

tasks.named("processResources") {
    dependsOn("generateVersionProperties")
}

tasks.register<com.alchitry.antlrkotlin.gradleplugin.AntlrKotlinTask>("generateKotlinGrammarSource") {
// the classpath used to run antlr code generation
    antlrClasspath = configurations.detachedConfiguration(
        // antlr itself
        // antlr is transitive added by antlr-kotlin-target,
        // add another dependency if you want to choose another antlr4 version (not recommended)
        // project.dependencies.create("org.antlr:antlr4:$antlrVersion"),

        // antlr target, required to create kotlin code
        project.dependencies.create("com.github.alchitry.antlr-kotlin:antlr-kotlin-target:$antlrKotlinVersion")
    )
    maxHeapSize = "64m"
    packageName = "com.alchitry.labs2.parsers.grammar"
    arguments = listOf("-no-visitor", "-listener")
    source = project.objects
        .sourceDirectorySet("antlr", "antlr")
        .srcDir("src/main/kotlin/com/alchitry/labs2/parsers/grammar").apply {
            include("*.g4")
        }
    // outputDirectory is required, put it into the build directory
    // if you do not want to add the generated sources to version control
    //outputDirectory = File("build/generated-src/antlr/main")
    // use this setting if you want to add the generated sources to version control
    outputDirectory = File("src/main/kotlin")
}

tasks.register("publish") {
    group = "conveyor"
    dependsOn("copied-site", "ms-store-release")
}

tasks.register("download-oss-cad-suite") {
    group = "build setup"
    doLast {
        val includesDirectory = project.buildFile.parentFile.resolve("includes")

        val latestReleaseUrl = getRedirectedUrl("https://github.com/alchitry/oss-cad-suite-build/releases/latest")
            ?: error("Failed to get latest release URL!")
        val urlDate = latestReleaseUrl.split('/').last()
        val dateStamp = urlDate.replace("-", "")

        listOf("windows-x64", "linux-x64", "darwin-x64", "darwin-arm64").forEach { arch ->
            val extension = if (arch == "windows-x64") "exe" else "tgz"
            val archive = temporaryDir.resolve("$arch.$extension")

            downloadFile(
                "https://github.com/alchitry/oss-cad-suite-build/releases/download/$urlDate/oss-cad-suite-$arch-$dateStamp.$extension",
                archive
            )

            val toolsDir = includesDirectory.resolve(arch).resolve("tools")
            val target = toolsDir.resolve("oss-cad-suite")
            if (target.exists())
                target.deleteRecursively()
            target.mkdirs()

            if (arch == "windows-x64") {
                project.exec { commandLine("7z", "x", archive.absolutePath, "-o${toolsDir.absolutePath}") }
                // work around for windows not finding the .dlls
                target.resolve("lib").apply {
                    listFiles()?.forEach {
                        if (it.extension == "dll") {
                            it.copyTo(target.resolve("bin").resolve(it.name), overwrite = true)
                        }
                    }
                }
            } else {
                project.exec { commandLine("tar", "-xzf", archive.absolutePath, "-C", toolsDir.absolutePath) }

                if (arch.startsWith("darwin")) {
                    // these files aren't needed and cause failures during signing
                    target.resolve("lib/python3.11/config-3.11-darwin/python.o").delete()
                }
            }
        }
    }
}

fun downloadFile(url: String, destination: File) {
    try {
        val urlObj = URL(url)
        val connection = urlObj.openConnection()
        val inputStream = connection.getInputStream()
        val buffer = ByteArray(4096)
        var bytesRead: Int

        val outputStream = FileOutputStream(destination)

        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        outputStream.close()
        inputStream.close()
    } catch (e: Exception) {
        println("Error downloading file: ${e.message}")
    }
}

fun getRedirectedUrl(url: String): String? {
    var currentUrl = url
    var connection: HttpURLConnection? = null
    try {
        do {
            connection = URL(currentUrl).openConnection() as HttpURLConnection
            connection.instanceFollowRedirects = false
            connection.connect()
            val location = connection.getHeaderField("Location")
            if (location != null) {
                currentUrl = location
            } else {
                return currentUrl
            }
        } while (connection!!.responseCode in 300..399)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        connection?.disconnect()
    }
    return null
}