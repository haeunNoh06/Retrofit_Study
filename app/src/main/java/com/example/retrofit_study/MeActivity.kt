package com.example.retrofit_study

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.retrofit_study.api.MeResponse
import com.example.retrofit_study.api.StringResponse
import com.example.retrofit_study.api.UserAPIService
import com.example.retrofit_study.interceptors.AddCookiesToRequestInterceptor
import com.example.retrofit_study.interceptors.PreferenceHelper
import com.example.retrofit_study.interceptors.SaveCookiesFromResponseInterceptor
import com.example.retrofit_study.interceptors.SessionExpirationDetectInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_me)

        val client = OkHttpClient.Builder()
            .addInterceptor(AddCookiesToRequestInterceptor(this))
            .addInterceptor(SaveCookiesFromResponseInterceptor(this))
            .addInterceptor(SessionExpirationDetectInterceptor(this))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val apiService = retrofit.create(UserAPIService::class.java)

        apiService.me().enqueue(object : Callback<MeResponse> {
            override fun onResponse(call: Call<MeResponse>, response: Response<MeResponse>) {
                val data : MeResponse? = response.body()
                data?.let {
                    Log.d("mytag", data.toString())
                    findViewById<TextView>(R.id.me).text = data.toString()// 정보 추가
                }
            }
            override fun onFailure(call: Call<MeResponse>, t: Throwable) {
                Log.d("mytag", t.toString())
            }
        })

        findViewById<Button>(R.id.logout_btn).setOnClickListener {
            apiService.logout().enqueue(object: Callback<StringResponse> {
                override fun onResponse(call: Call<StringResponse>, response: Response<StringResponse>) {
                    if(response.isSuccessful) {
                        PreferenceHelper.removeCookies(this@MeActivity)// 쿠키 다 지우기
                        Toast.makeText(this@MeActivity, "로그아웃 완료", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MeActivity, LoginActivity::class.java)// 로그인 액티비티로 보내기
                        startActivity(intent)
                        finish()
                    }
                }
                override fun onFailure(call: Call<StringResponse>, t: Throwable) {
                }
            })
        }
    }
}