pluginManagement {
    repositories {
        mavenLocal()
        google()
        gradlePluginPortal()
        //maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
        mavenCentral()
    }
}

//includeBuild("../Alchitry-Interface") {
//    dependencySubstitution {
//        substitute(module("com.github.alchitry:Alchitry-Interface")).using(project(":"))
//    }
//}

rootProject.name = "Alchitry Labs V2"

