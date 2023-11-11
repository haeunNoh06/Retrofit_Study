package com.example.retrofit_study.interceptors

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.retrofit_study.LoginActivity
import okhttp3.Interceptor
import okhttp3.Response

class SessionExpirationDetectInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if(response.code() == 440) {
            PreferenceHelper.removeCookies(context)
            Handler(Looper.getMainLooper()).post(Runnable {
                Toast.makeText(context, "세션이 만료되었습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
                // https://stackoverflow.com/questions/12947916/android-remove-all-the-previous-activities-from-the-back-stack
                val intent = Intent(context, LoginActivity::class.java)
                // Activity스택을 싹 삭제해서 back버튼으로 뒤로 가는 걸 막는다!!
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
                // (context as Activity).finish()
            })
        }
        return response
    }
}