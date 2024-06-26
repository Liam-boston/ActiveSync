plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "edu.psu.sweng888.activesync"
    compileSdk = 34

    defaultConfig {
        applicationId = "edu.psu.sweng888.activesync"
        minSdk = 26
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

    implementation("androidx.test:core:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // Adds support for RoomDB
    // See: https://developer.android.com/training/data-storage/room/#kts
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
//    implementation("com.google.android.gms:play-services-maps:18.1.0")
//    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Firebase dependencies
    // See: https://firebase.google.com/docs/auth/android/start
    implementation(platform("com.google.firebase:firebase-bom:32.7.3"))
    implementation ("com.google.firebase:firebase-auth")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Plotting Library
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Paris, the airbnb programmatic styling library
    implementation("com.airbnb.android:paris:2.0.0")
    // TODO: May have to add "kapt" and "ksp"? Check out docs: https://github.com/airbnb/paris/blob/master/README.md#installation
}