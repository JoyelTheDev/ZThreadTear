plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation("com.github.johnrengelman.shadow:com.github.johnrengelman.shadow.gradle.plugin:8.1.1")
    implementation("commons-io:commons-io:2.15.1")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    // Optional: Add maven(url = "https://plugins.gradle.org/m2/") if needed
}
