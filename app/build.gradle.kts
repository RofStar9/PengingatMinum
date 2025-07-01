plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "us.oris.pengingatminum"
    compileSdk = 34
    defaultConfig {
        applicationId = "us.oris.pengingatminum"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    kotlin {
        jvmToolchain(17) // aman dan modern (bisa juga 11 atau 21)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-firestore")

    implementation("com.google.firebase:firebase-database") {
        exclude(group = "com.google.firebase", module = "firebase-common")
        exclude(group = "com.google.firebase", module = "firebase-common-ktx")
        // Lifecycle & activity
        implementation("androidx.activity:activity-ktx:1.7.2")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
        implementation(libs.firebase.database)

        // Test
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    }
}

