plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services) // Make sure to add this line if using version catalog
    id("com.google.firebase.crashlytics") //Firebase Crashalytics
    id("com.google.firebase.appdistribution") //Firebase Appdistribution not Required
}

android {
    namespace = "com.example.practice.notesfirebase"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.practice.notesfirebase"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"

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

    viewBinding {
        enable = true // Correct way to enable View Binding in Kotlin DSL
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.location)
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation Components
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    implementation(libs.gson)


    // Add Firebase dependencies without versions
    implementation(libs.firebase.analytics) //Analytics
    implementation(libs.firebase.storage.ktx)
    implementation(libs.google.firebase.auth.ktx)
    implementation("androidx.security:security-crypto:1.1.0-alpha06") //Encrypted SharedPreferences
    implementation("com.google.firebase:firebase-crashlytics:19.4.2") //Crash Analytics
    // For FeedBack Api
    // ADD the API-only library to all variants
    implementation("com.google.firebase:firebase-appdistribution-api-ktx:16.0.0-beta15")
    // ADD the full SDK implementation to the "beta" variant only (example)
    implementation("com.google.firebase:firebase-appdistribution:16.0.0-beta15")
    //implementation("com.google.firebase:firebase-appdistribution:3.1.0") //App Distribution for Testers Feedback

}

// Apply Google Services plugin
apply(plugin = "com.google.gms.google-services")
