package com.multicastdns.appSupport

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build
import android.text.TextUtils
import java.util.*


@TargetApi(Build.VERSION_CODES.GINGERBREAD)
class SharedPref {


    private var preferences: SharedPreferences? = null
    val PREFS_NAME = "mySharedPrefs"

    /**
     * initialize default SharedPreferences
     */
    fun Sharedpreferences(context: Context) {
        preferences =
            context.getSharedPreferences("MyPreference", Context.MODE_PRIVATE)
    }

    /**
     * @name SharedPreferences name
     */
    fun Sharedpreferences(
        context: Context,
        name: String?
    ) {
        preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun getPreferences(): SharedPreferences {
        if (preferences == null) throw RuntimeException("Opps you have forget to intialize Prefs class.. Prefs prefs = new Prefs(context)")
        return preferences as SharedPreferences
    }

    fun getEditor(): SharedPreferences.Editor {
        return getPreferences().edit()
    }

    operator fun get(key: String?, defaultValue: Int): Int {
        return getPreferences().getInt(key, defaultValue)
    }

    operator fun get(key: String?, defaultValue: Long): Long {
        return getPreferences().getLong(key, defaultValue)
    }

    operator fun get(key: String?, defaultValue: Float): Float {
        return getPreferences().getFloat(key, defaultValue)
    }

    operator fun get(key: String?, defaultValue: Double): Double {
        val number = get(key)
        return try {
            number!!.toDouble()
        } catch (e: NumberFormatException) {
            defaultValue
        }
    }

    operator fun get(key: String?): String? {
        return getPreferences().getString(key, "")
    }

    fun getList(key: String?): ArrayList<String>? {
        return ArrayList(
            Arrays.asList(
                *TextUtils.split(
                    getPreferences().getString(key, ""), "‚‗‚"
                )
            )
        )
    }

    operator fun get(key: String?, defaultValue: Boolean): Boolean {
        return getPreferences().getBoolean(key, defaultValue)
    }

    operator fun set(key: String?, value: Int) {
        getEditor().putInt(key, value).apply()
    }

    operator fun set(key: String?, value: Long) {
        getEditor().putLong(key, value).apply()
    }

    operator fun set(key: String?, value: Float) {
        getEditor().putFloat(key, value).apply()
    }

    operator fun set(key: String?, value: Double) {
        set(key, value.toString())
    }

    operator fun set(key: String?, value: String?) {
        getEditor().putString(key, value).apply()
    }

    fun setList(
        key: String?,
        stringList: ArrayList<String>
    ) {
        val myStringList = stringList
            .toTypedArray()
        getEditor().putString(key, TextUtils.join("‚‗‚", myStringList)).apply()
    }

    operator fun set(key: String?, value: Boolean) {
        getEditor().putBoolean(key, value).apply()
    }

    operator fun contains(key: String?): Boolean {
        return getPreferences().contains(key)
    }

    fun remove(key: String?) {
        getEditor().remove(key).apply()
    }

    fun clear() {
        getEditor().clear().apply()
    }

    fun getAll(): Map<String?, *>? {
        return getPreferences().all
    }

    fun registerOnSharedPreferenceChangeListener(
        listener: OnSharedPreferenceChangeListener?
    ) {
        getPreferences().registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(
        listener: OnSharedPreferenceChangeListener?
    ) {
        getPreferences().unregisterOnSharedPreferenceChangeListener(listener)
    }




}