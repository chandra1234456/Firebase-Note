// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google() // Ensure this line is present
        mavenCentral() // Also include Maven Central
    }
    dependencies {
     //   classpath("com.google.gms:google-services:4.4.2")
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.3")  //Firebase

    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.firebase.appdistribution") version "4.0.0" apply false //not Required

}
