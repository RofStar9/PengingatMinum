// Top-level build file
plugins {
    id("com.android.application") version "8.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
}
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
    }
}
configurations.all {
    resolutionStrategy {
        force("com.google.firebase:firebase-common:21.1.1") // atau versi yang cocok dengan bom
    }
}
