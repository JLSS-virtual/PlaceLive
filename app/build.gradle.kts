// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.gradle.api.JavaVersion

plugins {
    // Plugin for Android application development
    id("com.android.application")
    // Plugin for Kotlin Android support
    id("org.jetbrains.kotlin.android")
}

android {
    // Namespace for the Android project
    namespace = "com.jlss.placelive"
    // Compile SDK version
    compileSdk = 34

    defaultConfig {
        // Application ID
        applicationId = "com.jlss.placelive"
        // Minimum SDK version
        minSdk = 26
        // Target SDK version
        targetSdk = 34
        // Version code of the app
        versionCode = 1
        // Version name of the app
        versionName = "1.0"

        // Instrumentation runner for testing
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Use support library for vector drawables
        vectorDrawables {
            useSupportLibrary = true
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
        }
    }

    buildTypes {
        release {
            // Disable code minification for release build
            isMinifyEnabled = false
            // Proguard configuration files
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Java compatibility settings
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        // JVM target version for Kotlin
        jvmTarget = "1.8"
    }

    buildFeatures {
        // Enable Jetpack Compose
        compose = true
    }

    composeOptions {
        // Kotlin compiler extension version for Compose
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    packaging {
        resources {
            // Exclude certain resources from the packaging
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core library for Android KTX
    implementation("androidx.core:core-ktx:1.9.0")
    // Lifecycle runtime library for KTX
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    // Lifecycle viewmodel and jetpackcompose ui
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    // Activity library for Jetpack Compose
    implementation("androidx.activity:activity-compose:1.7.0")
    // Compose BOM (Bill of Materials) to manage Compose library versions
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    // Core UI library for Jetpack Compose
    implementation("androidx.compose.ui:ui")
    // Graphics library for Jetpack Compose
    implementation("androidx.compose.ui:ui-graphics")
    // Tooling preview library for Jetpack Compose
    implementation("androidx.compose.ui:ui-tooling-preview")
    // Material 3 design library for Jetpack Compose
    implementation("androidx.compose.material3:material3")

    // Unit testing library
    testImplementation("junit:junit:4.13.2")
    // AndroidX JUnit extension for Android testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // Espresso library for Android UI testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Compose BOM for Android testing
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    // UI testing library for Jetpack Compose
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Debug implementation libraries for Jetpack Compose
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Add new dependencies here as your app grows
    // Retrofit and networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Convertor parser
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Java lib for json to objects or objects to java
    implementation("com.google.code.gson:gson:2.9.0")

}