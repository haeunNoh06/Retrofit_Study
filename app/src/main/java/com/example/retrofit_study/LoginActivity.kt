package com.example.retrofit_study

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.retrofit_study.api.LoginData
import com.example.retrofit_study.api.StringResponse
import com.example.retrofit_study.api.UserAPIService
import com.example.retrofit_study.interceptors.AddCookiesToRequestInterceptor
import com.example.retrofit_study.interceptors.PreferenceHelper
import com.example.retrofit_study.interceptors.SaveCookiesFromResponseInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 세션 아이디가 저장된 쿠키가 있는 경우 바로 Me 화면으로 이동(이미 로그인이 되어있는 경우)ㅁ
        if(PreferenceHelper.getCookies(this).size > 0) {
            val intent = Intent(this, MeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 인터셉터 등록(2개 등록하고 있음)
        val client = OkHttpClient.Builder()
            .addInterceptor(AddCookiesToRequestInterceptor(this))
            .addInterceptor(SaveCookiesFromResponseInterceptor(this))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val apiService = retrofit.create(UserAPIService::class.java)

        findViewById<Button>(R.id.login_btn).setOnClickListener {
            val email = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            apiService.login(LoginData(email, password)).enqueue(object : Callback<StringResponse> {
                override fun onResponse(call: Call<StringResponse>, response: Response<StringResponse>) {
                    val data : StringResponse? = response.body()
                    if(response.isSuccessful) {
                        data?.let {
                            Log.d("mytag", it.toString())
                            val intent = Intent(this@LoginActivity, MeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                }
                override fun onFailure(call: Call<StringResponse>, t: Throwable) {
                }
            })
        }
    }
}