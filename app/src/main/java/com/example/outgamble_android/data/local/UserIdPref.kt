package com.example.outgamble_android.data.local

import android.content.Context

class UserIdPref(context: Context) {
    private val pref = "prefs"
    private val key = "keys"

    private val shared = context.getSharedPreferences(pref, Context.MODE_PRIVATE)

    fun save(id: String) {
        shared.edit().putString(key, id).apply()
    }

    fun get(): String {
        return shared.getString(key, "").toString()
    }

    fun remove() {
        shared.edit().remove(key).apply()
    }
}