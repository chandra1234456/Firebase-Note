import com.android.build.api.dsl.SigningConfig

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
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

    signingConfigs {
        // Reusable signing config logic
        fun applySigningConfig(config: SigningConfig, name: String) {
            val storeFileBase64Path = System.getenv("KEYSTORE_BASE64")
            val storeFilePath = System.getenv("KEYSTORE_FILE")
            val storePwd = System.getenv("KEYSTORE_PASSWORD")
            val keyAliasVal = System.getenv("KEY_ALIAS")
            val keyPwd = System.getenv("KEY_PASSWORD")

            println("🔐 [$name] KEYSTORE_BASE64 = $storeFileBase64Path")
            println("🔐 [$name] KEYSTORE_FILE = $storeFilePath")
            println("🔐 [$name] KEYSTORE_PASSWORD is set? ${!storePwd.isNullOrBlank()}")
            println("🔐 [$name] KEY_ALIAS is set? ${!keyAliasVal.isNullOrBlank()}")
            println("🔐 [$name] KEY_PASSWORD is set? ${!keyPwd.isNullOrBlank()}")

            if (!storeFilePath.isNullOrBlank() &&
                !storePwd.isNullOrBlank() &&
                !keyAliasVal.isNullOrBlank() &&
                !keyPwd.isNullOrBlank()) {
                config.storeFile = file(storeFilePath)
                config.storePassword = storePwd
                config.keyAlias = keyAliasVal
                config.keyPassword = keyPwd
            } else {
                println("⚠️ [$name] SigningConfig not set: missing environment variables.")
            }
        }

        create("release") {
            applySigningConfig(this, "release")
        }

        getByName("debug") {
            applySigningConfig(this, "debug")
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
        }

        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug") // Optional for debug
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

    // Firebase App Distribution
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
