plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services) // Firebase Analytics
    id("com.google.firebase.crashlytics") // Firebase Crashlytics
    id("com.google.firebase.appdistribution") // Firebase App Distribution (optional)
}

android {
    namespace = "com.example.practice.notesfirebase"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.practice.notesfirebase"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // ✅ Signing config for release
    signingConfigs {
        create("release") {
            //Added in Github Secretes
            val storeFilePath = System.getenv("KEYSTORE_FILE")
            val storePwd = System.getenv("KEYSTORE_PASSWORD")
            val keyAliasVal = System.getenv("KEY_ALIAS")
            val keyPwd = System.getenv("KEY_PASSWORD")
            if (!storeFilePath.isNullOrBlank() &&
                !storePwd.isNullOrBlank() &&
                !keyAliasVal.isNullOrBlank() &&
                !keyPwd.isNullOrBlank()) {

                storeFile = file(storeFilePath)
                storePassword = storePwd
                keyAlias = keyAliasVal
                keyPassword = keyPwd
            } else {
                println("⚠️ SigningConfig not set: missing environment variables.")
            }
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                         )
            //JKS FILE PATH - C:\Users\balachandra.d\private\NotesFirebase\.gradle\notefirebase.jks

        }

        getByName("debug") {
            isDebuggable = true
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                         )
        }
    }

    viewBinding {
        enable = true
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

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.google.firebase.auth.ktx)

    // Firebase App Distribution (optional for tester feedback)
    implementation("com.google.firebase:firebase-appdistribution-api-ktx:16.0.0-beta15")
    implementation("com.google.firebase:firebase-appdistribution:16.0.0-beta15")

    // Encrypted SharedPreferences
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // Gson
    implementation(libs.gson)
    implementation(libs.play.services.location)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
