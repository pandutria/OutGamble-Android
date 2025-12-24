package com.example.outgamble_android.data.local

import android.content.Context

class FullnamePref(context: Context) {
    private val pref = "pref"
    private val key = "key"

    private val shared = context.getSharedPreferences(pref, Context.MODE_PRIVATE)

    fun save(name: String) {
        shared.edit().putString(key, name).apply()
    }

    fun get(): String {
        return shared.getString(key, "").toString()
    }

    fun remove() {
        shared.edit().remove(key).apply()
    }
}