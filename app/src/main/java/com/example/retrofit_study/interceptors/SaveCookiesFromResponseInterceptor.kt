package com.example.retrofit_study.interceptors

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

// Set-Cookie로 전달된 쿠키 정보를 모두 저장하는 인터셉터
class SaveCookiesFromResponseInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        // Set-Cookie로 전달받은 저장할 쿠키가 있으면
        if(response.headers("Set-Cookie").isNotEmpty()) {
            // 기존 쿠키에
            val cookies = PreferenceHelper.getCookies(context)
            // Set-Cookie로 전달받은 새 쿠키를 모두 저장하여
            for(header in response.headers("Set-Cookie")) cookies.add(header)
            // 다시 프리퍼런스에 저장
            PreferenceHelper.setCookies(context, cookies)
        }
        return response
    }
}