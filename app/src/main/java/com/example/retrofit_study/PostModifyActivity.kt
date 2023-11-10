package com.example.retrofit_study

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.retrofit_study.api.APIService
import com.example.retrofit_study.api.Post
import com.example.retrofit_study.api.StringResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostModifyActivity : AppCompatActivity() {
    lateinit var apiService: APIService
    lateinit var writeBtn: Button
    lateinit var titleEdit: EditText
    lateinit var authorEditText: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_modify)

        val post = intent.getSerializableExtra("post") as Post // 시리얼라이저블을 post로 다운캐스팅
        // Q) post의 title, author, content가 있을테니 해당 내용을 모두 EditText에다가 넣어주기
        findViewById<EditText>(R.id.post_title).setText(post.title)
        findViewById<EditText>(R.id.post_author).setText(post.author)
        findViewById<EditText>(R.id.post_content).setText(post.content)

        val retrofit = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)// 안드에서 지원하는 내 pc(10.0.2.2)에 연결해준다. localhost 사용 안 됨
            .addConverterFactory(GsonConverterFactory.create())// 필수적으로 들어가는 코드. body-parser의 역할과 동일
            .build()

        apiService = retrofit.create(APIService::class.java)// 아까 정의한 APIService 인터페이스를 만듦

        findViewById<Button>(R.id.post_modify_btn).setOnClickListener {
            modifyPost(
                post.id,
                mutableMapOf(
                    "title" to findViewById<EditText>(R.id.post_title).text.toString(),
                    "author" to findViewById<EditText>(R.id.post_author).text.toString(),
                    "content" to findViewById<EditText>(R.id.post_content).text.toString()
                )
            )
        }
    }

    fun modifyPost(id: Int, body: MutableMap<String, Any>) {
        val call = apiService.modifyPost(id, body)
        call.enqueue(object : Callback<StringResponse> {
            // 익명클래스므로 추상메서드 구현 필요
            override fun onResponse(
                call: Call<StringResponse>,
                response: Response<StringResponse>
            ) {

                val data: StringResponse? = response.body()
                data?.let {
                    Toast.makeText(this@PostModifyActivity, "글 수정이 완료되었습니다.", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }

            }

            override fun onFailure(call: Call<StringResponse>, t: Throwable) {

            }
        })
    }
}