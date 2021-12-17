// Top-level build file where you can add configuration options common to all sub-projects/modules.

@Suppress("JcenterRepositoryObsolete") buildscript {
    val kotlinVersion by extra("1.6.0")
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${rootProject.extra["kotlinVersion"]}")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
    configurations.all {
        // Don't cache changing modules at all.
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
        resolutionStrategy.cacheDynamicVersionsFor(2, "minutes")

    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

tasks.register("allTests") {
    dependsOn(
        ":app:testDebugUnitTest",
        ":infrastructure:testDebugUnitTest",
        ":data:test",
        ":domain:test"
    )
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
tasks.withType<Test> {
    useJUnitPlatform()
}
