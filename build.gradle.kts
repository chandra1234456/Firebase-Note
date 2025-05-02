// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google() // Ensure this line is present
        mavenCentral() // Also include Maven Central
    }
    dependencies {
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.3")  //Firebase

    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.firebase.appdistribution") version "4.0.0" apply false
    alias(libs.plugins.google.services) apply false //not Required
    // Add the dependency for the Performance Monitoring Gradle plugin
 //   id("com.google.firebase.firebase-perf") version "1.4.2" apply false

}
