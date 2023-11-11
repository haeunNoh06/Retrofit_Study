package com.example.retrofit_study.interceptors

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper {
    companion object {
        val PREF_NAME = "PREF_COOKIES"

        // 쿠키 가져오기
        fun getCookies(context: Context): MutableSet<String> {
            // getSharedPreference: 내장db같은 것 xml파일에 저장(logalstorage느낌)
            val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            // val set : java.util.Set<String> = java.util.HashSet<String>() as Set<String>
            return pref.getStringSet("cookies", mutableSetOf()) as MutableSet<String>
        }

        // 쿠키 저장
        fun setCookies(context: Context, cookies: MutableSet<String>) {
            val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putStringSet("cookies", cookies)
            editor.apply()
        }

        // 쿠키 날리기
        fun removeCookies(context: Context) {
            val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.clear()
            editor.apply()
        }
    }
}