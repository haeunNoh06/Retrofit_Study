package com.example.retrofit_study

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.retrofit_study.Config.Companion.BASE_URL
import com.example.retrofit_study.api.APIService
import com.example.retrofit_study.api.PostCreateRequest
import com.example.retrofit_study.api.StringResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostWriteActivity : AppCompatActivity() {

    lateinit var apiService: APIService
    lateinit var writeBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_write)
        
        // Q) "작성완료"버튼을 누르면
        // EditText에서 title author, content 3개 문자열 추출하기
        // 글 작성 API를 호출하기 그리고 성공 이후 액티비티 종료
        val retrofit = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)// 안드에서 지원하는 내 pc(10.0.2.2)에 연결해준다. localhost 사용 안 됨
            .addConverterFactory(GsonConverterFactory.create())// 필수적으로 들어가는 코드. body-parser의 역할과 동일
            .build()

        apiService = retrofit.create(APIService::class.java)


        writeBtn = findViewById(R.id.post_write_btn)
        writeBtn.setOnClickListener {
            createPost(
                findViewById<EditText>(R.id.post_title).text.toString(),
                findViewById<EditText>(R.id.post_author).text.toString(),
                findViewById<EditText>(R.id.post_content).text.toString()
            )
        }
    }

    fun createPost(title: String, author: String, content: String) {
        val call = apiService.createPost(PostCreateRequest(title, author, content))
        call.enqueue(object : Callback<StringResponse> {
            override fun onResponse(
                call: Call<StringResponse>,
                response: Response<StringResponse>
            ) {
                Toast.makeText(this@PostWriteActivity, "글 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onFailure(call: Call<StringResponse>, t: Throwable) {

            }

        })
    }
}