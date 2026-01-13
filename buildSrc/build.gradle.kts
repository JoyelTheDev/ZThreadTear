plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation("gradle.plugin.com.github.johnrengelman:shadow:8.1.1")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}
