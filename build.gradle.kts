import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileOutputStream
import java.util.*

buildscript {
    val antlrKotlinVersion = "f845b6f724"
    extra.set("antlrKotlinVersion", antlrKotlinVersion)
    dependencies {
        classpath("com.github.alchitry.antlr-kotlin:antlr-kotlin-gradle-plugin:$antlrKotlinVersion")
    }
}

val antlrKotlinVersion = ext.get("antlrKotlinVersion") as String

plugins {
    kotlin("jvm") version "1.9.0"
    id("org.jetbrains.compose") version "1.4.3"
    //antlr
}

group = "com.alchitry"
version = "2.0.0-ALPHA-SNAPSHOT"

repositories {
    google()
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.alchitry.antlr-kotlin:antlr-kotlin-runtime:$antlrKotlinVersion")
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.1")
    implementation(compose.desktop.linux_x64)
    implementation(compose.desktop.linux_arm64)
    implementation(compose.desktop.windows_x64)
    implementation(compose.desktop.macos_x64)
    implementation(compose.desktop.macos_arm64)
    implementation(kotlin("reflect"))
    implementation("org.jdom:jdom2:2.0.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")

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
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AlchitryLabsV2"
            packageVersion = "2.0.0"
        }
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
