package com.application.mapa.feature.encription

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

class StorableManager {

    /**
     * Retrieves the [Storable] instance from prefs.
     *
     * @param context the caller's context
     * @return the storable instance
     */
    fun getStorable(context: Context): Storable? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val serializedStorable = prefs.getString(STORABLE_KEY, null)
        if (serializedStorable.isNullOrBlank()) {
            return null
        }

        return try {
            // TODO add decription
            Gson().fromJson(serializedStorable, object : TypeToken<Storable>() {}.type)
        } catch (ex: JsonSyntaxException) {
            null
        }
    }

    /**
     * Save the storable instance to preferences.
     *
     * @param storable a storable instance
     */
    fun saveToPrefs(context: Context, storable: Storable) {
        // TODO add encription
        val serializedStorable = Gson().toJson(storable)
        val prefs = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        prefs.edit()
            .putString(STORABLE_KEY, serializedStorable)
            .apply()
    }

    fun storableEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.contains("key")
    }

    companion object {
        private const val STORABLE_KEY = "key"
        private const val PREFS_NAME = "database"
    }
}