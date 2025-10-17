package dev.rafiqulislam.core.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    private companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("DATA_STORE")
    }

    suspend fun saveString(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    suspend fun getString(key: String, defaultValue: String = ""): String {
        val prefKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[prefKey] ?: defaultValue
    }

    suspend fun saveInt(key: String, value: Int) {
        val prefKey = intPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    suspend fun getInt(key: String, defaultValue: Int = 0): Int {
        val prefKey = intPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[prefKey] ?: defaultValue
    }

    suspend fun saveBoolean(key: String, value: Boolean) {
        val prefKey = booleanPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        val prefKey = booleanPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[prefKey] ?: defaultValue
    }

    suspend fun clearAllData() {
        // clear all data from the data store
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun clear(key: String) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences.remove(prefKey)
        }
    }
}