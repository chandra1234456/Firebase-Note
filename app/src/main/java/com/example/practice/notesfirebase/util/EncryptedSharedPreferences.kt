package com.example.practice.notesfirebase.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.io.File
import java.security.GeneralSecurityException

object EncryptedSharedPreferencesManager {

    // Constants for SharedPreferences keys
    private const val PREFS_NAME = "preferences"
    private const val KEY_VALUE_ONE = "valueOne"
    private const val KEY_VALUE_TWO = "valueTwo"

    // Insert and save data to EncryptedSharedPreferences
    fun insertAndSaveData(
        context: Context,
        valueOne: String,
        valueTwo: String
                         ) {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val sharedPreferences = EncryptedSharedPreferences.create(
                    PREFS_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                                                                     )

            sharedPreferences.edit().apply {
                putString(KEY_VALUE_ONE, valueOne)
                putString(KEY_VALUE_TWO, valueTwo)
            }.apply()  // Use apply() for asynchronous save operation
        } catch (e: GeneralSecurityException) {
            // Handle potential security exceptions
            e.printStackTrace()
        }
    }

    // Load data from EncryptedSharedPreferences
    fun getLoadTheData(context: Context): Pair<String?, String?>? {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val sharedPreferences = EncryptedSharedPreferences.create(
                    PREFS_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                                                                     )

            // Retrieve data from SharedPreferences
            val valueOne = sharedPreferences.getString(KEY_VALUE_ONE, null)
            val valueTwo = sharedPreferences.getString(KEY_VALUE_TWO, null)

            // Return the values as a pair or null if no data
            return Pair(valueOne, valueTwo)
        } catch (e: GeneralSecurityException) {
            // Handle potential security exceptions
            e.printStackTrace()
            return null
        }
    }
    fun clearAllData(context: Context) {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val sharedPreferences = EncryptedSharedPreferences.create(
                    PREFS_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                                                                     )

            sharedPreferences.edit().clear().apply()

            // Delete the encrypted SharedPreferences file
            val prefsFile = File(context.filesDir.parentFile, "shared_prefs/$PREFS_NAME.xml")
            if (prefsFile.exists()) {
                prefsFile.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
