# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Firebase (keep models and required classes)
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Gson / JSON parsing if used
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# EncryptedSharedPreferences & Security
-keep class androidx.security.crypto.** { *; }
-dontwarn androidx.security.crypto.**

# Needed for reflection (optional)
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}
