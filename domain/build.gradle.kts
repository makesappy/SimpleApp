plugins {
    id("java-library")
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.4.20"
}
dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${rootProject.extra["kotlinVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

    testImplementation("junit:junit:4.13.1")
    testImplementation("io.kotest:kotest-runner-junit5:4.3.2")
    testImplementation("io.mockk:mockk:1.10.2")
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}