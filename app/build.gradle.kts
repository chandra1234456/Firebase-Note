import com.android.build.api.dsl.SigningConfig
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
    id("com.google.gms.google-services")
    // Add the Performance Monitoring Gradle plugin
   // id("com.google.firebase.firebase-perf")
    id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
}

android {
    namespace = "com.example.practice.notesfirebase"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.practice.notesfirebase"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.4"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        // Reusable signing config logic
        fun applySigningConfig(config: SigningConfig, name: String,boolean: Boolean) {
            if (boolean) {
            /*val storeFileBase64Path = System.getenv("C:\Users\balachandra.d\private\NotesFirebase\.gradle\notefirebase.jks")
            val storeFilePath = System.getenv("KEYSTORE_FILE")
            val storePwd = System.getenv("KEYSTORE_PASSWORD")
            val keyAliasVal = System.getenv("KEY_ALIAS")
            val keyPwd = System.getenv("KEY_PASSWORD")*/
                 val storeFilePath = System.getenv("KEYSTORE_FILE")?: "notefirebase.jks"
                //val storeFilePath = "notefirebase.jks"
                val storePwd = "android"
                val keyAliasVal = "android"
                val keyPwd = "android"

                config.storeFile = rootProject.file(storeFilePath)
                config.storePassword = "android"
                println("🔐 [$name] rootProject = ${config.storeFile}")
                config.keyAlias = "android"
                config.keyPassword = "android"

                // println("🔐 [$name] KEYSTORE_BASE64 = $storeFileBase64Path")
                println("🔐 [$name] KEYSTORE_FILE = $storeFilePath")
                println("🔐 [$name] KEYSTORE_PASSWORD is set? ${! storePwd.isNullOrBlank()}")
                println("🔐 [$name] KEY_ALIAS is set? ${! keyAliasVal.isNullOrBlank()}")
                println("🔐 [$name] KEY_PASSWORD is set? ${! keyPwd.isNullOrBlank()}")

                /*if (! storeFilePath.isNullOrBlank() &&
                    ! storePwd.isNullOrBlank() &&
                    ! keyAliasVal.isNullOrBlank() &&
                    ! keyPwd.isNullOrBlank()
                ) {
                    config.storeFile = file(storeFilePath)
                    config.storeFile = rootProject.file(storeFilePath)
                    config.storePassword = storePwd
                    println("🔐 [$name] rootProject = ${config.storeFile}")
                    config.keyAlias = keyAliasVal
                    config.keyPassword = keyPwd
                } else {
                    println("⚠️ [$name] SigningConfig not set: missing environment variables.")
                }*/
            }
        }

        create("release") {
            applySigningConfig(this, "release",true)
        }

        getByName("debug") {
            applySigningConfig(this, "debug",false)
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
ktlint {
    android = true                  // Enable Android-specific rules
    ignoreFailures = false           // Fail the build if there are linting errors
    disabledRules.set(setOf("no-wildcard-imports","final-newline"))
    reporters {
        reporter(ReporterType.PLAIN)           // Print the result in plain text format
        reporter(ReporterType.CHECKSTYLE)       // Output the result in Checkstyle format
        reporter(ReporterType.SARIF)            // Output the result in SARIF format
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
    implementation("com.google.firebase:firebase-messaging:24.1.1")

    // Firebase Analytics
    implementation ("com.google.firebase:firebase-analytics:21.2.0")

    // Kotlin Extensions for Firebase Analytics
    implementation ("com.google.firebase:firebase-analytics-ktx:21.2.0")


    implementation("com.google.firebase:firebase-appdistribution:16.0.0-beta15")  // Check for latest version
    // Google Play Services Measurement (ensure it's up-to-date)
    implementation ("com.google.android.gms:play-services-measurement-api:21.2.0")
}
