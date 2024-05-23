plugins {
    id("com.android.application")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    alias(libs.plugins.com.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.rodmat95.trucksgps"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rodmat95.trucksgps"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    // Implementaciones de AndroidX
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))

    // Dependencias de Firebase
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-messaging")

    implementation("com.firebase:geofire-android:3.1.0")

    // Dependencia de google maps
    implementation("com.google.android.gms:play-services-maps:16.1.0")
    implementation("com.google.android.gms:play-services-location:16.0.0")

    // Recyclerview de AndroidX
    implementation("androidx.recyclerview:recyclerview") //1.3.2
}