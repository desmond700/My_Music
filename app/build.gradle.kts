plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
//    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.mymusic"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mymusic"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "34.0.0"
}

dependencies {
    val nav_version = "2.7.6"
    val room_version = "2.6.1"

    // If using insets-ui
    implementation("com.google.accompanist:accompanist-insets:0.31.5-beta")
    implementation("com.google.accompanist:accompanist-insets-ui:0.31.5-beta")

    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")

    // Permissions library
    implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")

    // CoordinateLayout library
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha13")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.legacy:legacy-support-v4:1.0.0") // Needed MediaSessionCompat.Token

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // Color Palette
    implementation("androidx.palette:palette-ktx:1.0.0")

    // Compose Libraries
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0-beta01")
    implementation("androidx.compose.material:material:1.6.0-beta03")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.foundation:foundation:1.6.0-beta03")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Datastore
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")


    // Dagger hilt
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Room
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    // Datastore
    implementation("androidx.datastore:datastore-preferences-core:1.0.0")


    // Media3
    val media3_version = "1.2.0"

    // For media playback using ExoPlayer
    implementation("androidx.media3:media3-exoplayer:$media3_version")
    // For building media playback UIs
    implementation("androidx.media3:media3-ui:$media3_version")
    // For exposing and controlling media sessions
    implementation("androidx.media3:media3-session:$media3_version")
    // For extracting data from media containers
    implementation("androidx.media3:media3-extractor:$media3_version")
    // For scheduling background operations using Jetpack Work's WorkManager with ExoPlayer
    implementation("androidx.media3:media3-exoplayer-workmanager:$media3_version")
    // Utilities for testing media components (including ExoPlayer components)
    implementation("androidx.media3:media3-test-utils:$media3_version")
    // Utilities for testing media components (including ExoPlayer components) via Robolectric
    implementation("androidx.media3:media3-test-utils-robolectric:$media3_version")
    // Common functionality for media database components
    implementation("androidx.media3:media3-database:$media3_version")
    // Common functionality for media decoders
    implementation("androidx.media3:media3-decoder:$media3_version")
    // Common functionality for loading data
    implementation("androidx.media3:media3-datasource:$media3_version")
    // Common functionality used across multiple media libraries
    implementation("androidx.media3:media3-common:$media3_version")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // dagger hilt
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.49")
    annotationProcessor("com.google.dagger:hilt-compiler:2.49")

    // Testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
