import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.incremental.createDirectory
import org.jetbrains.kotlin.incremental.deleteDirectoryContents
import java.io.FileOutputStream
import java.util.*

buildscript {
    val antlrKotlinVersion = "f845b6f724" // commit hash for custom antlr runtime
    extra.set("antlrKotlinVersion", antlrKotlinVersion)

    dependencies {
        classpath("com.github.alchitry.antlr-kotlin:antlr-kotlin-gradle-plugin:$antlrKotlinVersion")
    }
}

val antlrKotlinVersion = extra.get("antlrKotlinVersion") as String


plugins {
    kotlin("jvm") version "1.9.0"
    // TODO: replace with main branch when PR accepted: https://github.com/JetBrains/compose-multiplatform/pull/3640
    id("org.jetbrains.compose") version "0.1.0-SNAPSHOT"
}


val fullVersion = "2.0.0-ALPHA-SNAPSHOT"
val numOnlyVersion = fullVersion.split('-').first()

group = "com.alchitry"
version = fullVersion

repositories {
    google()
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}

val crossPlatform = true

dependencies {
    implementation("com.github.alchitry.antlr-kotlin:antlr-kotlin-runtime:$antlrKotlinVersion")
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.1")

    implementation(kotlin("reflect"))
    implementation("org.jdom:jdom2:2.0.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
    implementation("org.usb4java:usb4java:1.3.0")
    implementation("com.fazecast:jSerialComm:2.10.3")
    implementation("com.github.alchitry.yad2xx:yad2xxJava:d2xx_only_with_lib-SNAPSHOT")
    implementation("me.tongfei:progressbar:0.10.0")

    if (crossPlatform) {
        implementation(compose.desktop.linux_x64)
        implementation(compose.desktop.linux_arm64)
        implementation(compose.desktop.windows_x64)
        implementation(compose.desktop.macos_x64)
        implementation(compose.desktop.macos_arm64)
    } else {
        implementation(compose.desktop.currentOs)
    }

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-opt-in=kotlin.ExperimentalStdlibApi"
        freeCompilerArgs += "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        freeCompilerArgs += "-Xcontext-receivers"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

compose.desktop {
    application {
        mainClass = "com.alchitry.labs.MainKt"
        nativeDistributions {
            // Docs: https://github.com/JetBrains/compose-multiplatform/blob/master/tutorials/Native_distributions_and_local_execution/README.md
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Alchitry Labs"
            packageVersion = (version as String).split("-").first()
            vendor = "Alchitry"
            args.add("labs")

            linux {
                iconFile.set(File("icon.png"))
                shortcut = true
                debMaintainer = "Alchitry"
            }

            windows {
                console = false // print output when running .exe from cmd line
                upgradeUuid = "3e4e3be7-b77e-437e-9ffe-5607b15a6710"
                shortcut = true
                iconFile.set(File("icon.ico"))
                menuGroup = "Alchitry"
            }

            macOS {
                // TODO: Add mac support
            }

            // This currently relies upon our custom compose fork
            // PR: https://github.com/JetBrains/compose-multiplatform/pull/3640
            additionalLauncher("Alchitry Loader") {
                add("arguments", "loader")
            }

            if (Os.isFamily(Os.FAMILY_WINDOWS)) {
                additionalLauncher("alchitry") {
                    add("arguments", "")
                    add("win-console", "true")
                    add("win-shortcut", "false")
                }
            }
        }
    }
}

afterEvaluate {
    tasks.named("packageDeb") {
        dependsOn(cleanDebsTask)
        finalizedBy(modifyDebTask)
    }
}

// used to remove all the debs before building so that there will only be one for modifyDebTask to find
val cleanDebsTask = tasks.register("cleanDebs") {
    doLast {
        project.buildDir.resolve("compose/binaries/main/deb").apply {
            if (exists())
                deleteDirectoryContents()
        }
    }
}

fun modifyFile(file: File, modifier: StringBuilder.() -> Unit) {
    val fileText = file.readText()
    val newPostInst = StringBuilder(fileText).run {
        modifier()
        toString()
    }
    file.writeText(newPostInst)
}

fun StringBuilder.indexAtEndOf(str: String) = indexOf(str) + str.length

val modifyDebTask = tasks.register("modifyDeb") {
    dependsOn("packageDeb")
    doLast {
        val debDir = project.buildDir.resolve("compose/binaries/main/deb")
        val deb = debDir.listFiles().firstOrNull { it.extension == "deb" } ?: error("Failed to find deb file!")
        val tmpDir = debDir.resolve("tmp")
        tmpDir.createDirectory()

        exec {
            workingDir = debDir
            executable = "dpkg-deb"
            args = listOf("-R", deb.name, "tmp")
        }

        val sourceDirectory = File("install/linux")
        sourceDirectory.copyRecursively(tmpDir, overwrite = true)

        modifyFile(tmpDir.resolve("DEBIAN/postinst")) {
            insert(
                indexAtEndOf("configure)"),
                "\nln -s /opt/alchitry-labs/bin/Alchitry\\ Labs /usr/bin/alchitry"
            )
        }

        modifyFile(tmpDir.resolve("DEBIAN/prerm")) {
            insert(indexAtEndOf("deconfigure)"), "\nrm /usr/bin/alchitry")
        }

        exec {
            workingDir = debDir
            executable = "dpkg-deb"
            args = listOf("--root-owner-group", "-b", "tmp", deb.name)
        }

        tmpDir.deleteRecursively()
    }
}

val generatedVersionDir = "$buildDir/resources/main"

tasks.register("generateVersionProperties") {
    doLast {
        val propertiesFile = file("$generatedVersionDir/version.properties")
        propertiesFile.parentFile.mkdirs()
        val properties = Properties()
        properties.setProperty("version", version.toString())
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

tasks.register<Jar>("fatJar") {
    dependsOn.addAll(
        listOf(
            "compileJava",
            "compileKotlin",
            "processResources"
        )
    ) // We need this for Gradle optimization to work
    archiveClassifier.set("standalone") // Naming the jar
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest { attributes(mapOf("Main-Class" to "com.alchitry.labs.MainKt")) }
    val sourcesMain = sourceSets.main.get()
    val contents = configurations.runtimeClasspath.get()
        .map { if (it.isDirectory) it else zipTree(it) } +
            sourcesMain.output
    from(contents)
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
    packageName = "com.alchitry.labs.parsers.grammar"
    arguments = listOf("-no-visitor", "-listener")
    source = project.objects
        .sourceDirectorySet("antlr", "antlr")
        .srcDir("src/main/kotlin/com/alchitry/labs/parsers/grammar").apply {
            include("*.g4")
        }
    // outputDirectory is required, put it into the build directory
    // if you do not want to add the generated sources to version control
    //outputDirectory = File("build/generated-src/antlr/main")
    // use this setting if you want to add the generated sources to version control
    outputDirectory = File("src/main/kotlin")
}