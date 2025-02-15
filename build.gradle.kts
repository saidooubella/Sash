plugins {
    kotlin("jvm") version "2.1.0"
}

group = "io.github.saidooubella"
version = "0.0.1-dev"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(11)
    explicitApi()
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.contracts.ExperimentalContracts")
        freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.8")
    implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.6.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
