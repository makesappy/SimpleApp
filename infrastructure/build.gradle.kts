plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}
android {
    compileSdk = 31
    defaultConfig {
        minSdk = 26
        targetSdk = 31
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    kotlinOptions { jvmTarget = JavaVersion.VERSION_1_8.toString() }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/LICENSE-notice.md")
    }
}
dependencies {
    implementation(project(":domain"))

    implementation("androidx.room:room-runtime:2.3.0")
    implementation("androidx.room:room-ktx:2.3.0")
    implementation("androidx.core:core-ktx:1.7.0")
    kapt("androidx.room:room-compiler:2.3.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${rootProject.extra["kotlinVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.kotest:kotest-runner-junit5:4.3.2")
    testImplementation("io.mockk:mockk:1.10.2")
    androidTestImplementation("androidx.test:runner:1.4.1-alpha03")
    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.room:room-testing:2.3.0")
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("io.kotest:kotest-runner-junit5:4.3.2")
    androidTestImplementation("io.mockk:mockk:1.10.2")
}