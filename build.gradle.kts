import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileOutputStream
import java.util.*

buildscript {
    val antlrKotlinVersion = "662e198228" // commit hash for custom antlr runtime
    extra.set("antlrKotlinVersion", antlrKotlinVersion)

    dependencies {
        classpath("com.github.alchitry.antlr-kotlin:antlr-kotlin-gradle-plugin:$antlrKotlinVersion")
    }
}

val antlrKotlinVersion = extra.get("antlrKotlinVersion") as String


plugins {
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.2"
    id("dev.hydraulic.conveyor") version "1.5"
    id("at.stnwtr.gradle-secrets-plugin") version "1.0.1"
    id("org.openjfx.javafxplugin") version "0.1.0"
    kotlin("plugin.serialization") version "1.9.23"
}

val fullVersion = "2.0.8-PREVIEW"
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
    implementation("org.apache.commons:commons-text:1.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    implementation(kotlin("reflect"))
    implementation("org.jdom:jdom2:2.0.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
    implementation("org.usb4java:usb4java:1.3.0")
    macAarch64("io.github.dsheirer:libusb4java-darwin-aarch64:1.3.1") // ARM Mac binary
    implementation("com.fazecast:jSerialComm:2.11.0")
    implementation("com.github.alchitry.yad2xx:yad2xxJava:d2xx_only_with_lib-SNAPSHOT")
    implementation("me.tongfei:progressbar:0.10.1")
    implementation("org.jline:jline:3.26.1")
    implementation("org.fusesource.jansi:jansi:2.4.1")
    implementation("li.flor:native-j-file-chooser:1.6.4")

    linuxAmd64(compose.desktop.linux_x64)
    linuxAarch64(compose.desktop.linux_arm64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)

    implementation("org.jetbrains.compose.material3:material3-desktop:1.6.2")

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
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-opt-in=kotlin.contracts.ExperimentalContracts"
        freeCompilerArgs += "-opt-in=kotlin.ExperimentalStdlibApi"
        freeCompilerArgs += "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
        freeCompilerArgs += "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        freeCompilerArgs += "-Xcontext-receivers"
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

tasks.registerConveyorTask("app")
tasks.registerConveyorTask("site") // makes the site files locally
tasks.registerConveyorTask("windows-msix") // builds a windows msix
tasks.registerConveyorTask("windows-app") // builds a windows app
tasks.registerConveyorTask("copied-site") // pushes the site files to GitHub
tasks.registerConveyorTask("mac-app", "-Kapp.machines=mac.aarch64")

val generatedVersionDir = "$buildDir/resources/main"

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