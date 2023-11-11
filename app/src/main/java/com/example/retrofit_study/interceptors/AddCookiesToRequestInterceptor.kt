package com.example.retrofit_study.interceptors

import android.content.Context
// 인터셉터는 okhttp3에서 가져온다
// 레트로핏이 okhttp3에서 가져온것
import okhttp3.Interceptor
import okhttp3.Response

// 프리퍼런스에 저장된 쿠키 값을 읽어서 모두 요청 메시지에 넣어주는 인터셉터
// 인터셉터는 미들웨어와 비슷
// 요청을 보내기 전에 필요한 무언가(쿠키를 집어넣는 것)를 해준다
class AddCookiesToRequestInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        // Preference에 저장된 쿠키 값 Set 가져오기
        val cookies = PreferenceHelper.getCookies(context)
        // 요청 메시지에 넣을 쿠키 값을 모두 전달하도록 개별 쿠키 헤더 추가
        for (cookie in cookies) builder.addHeader("Cookie", cookie)
        // 요청 메시지 보낼 수 있도록 계속 진행 요청
        return chain.proceed(builder.build())
    }
}