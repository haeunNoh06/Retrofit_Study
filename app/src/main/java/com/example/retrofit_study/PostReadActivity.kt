package com.example.retrofit_study

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit_study.api.APIService
import com.example.retrofit_study.api.AllPostResponse
import com.example.retrofit_study.api.Post
import com.example.retrofit_study.api.PostResponse
import com.example.retrofit_study.api.StringResponse
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostReadActivity : AppCompatActivity() {
    lateinit var apiService : APIService
    lateinit var post : Post
    lateinit var deleteBtn : Button
    lateinit var modifyBtn : Button
    var id : Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_read)
        
        // id 받아서 content 출력하기
        id = intent.getIntExtra("id", -1)!!
        Log.d("mytag", id.toString())

        val retrofit = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)// 안드에서 지원하는 내 pc(10.0.2.2)에 연결해준다. localhost 사용 안 됨
            .addConverterFactory(GsonConverterFactory.create())// 필수적으로 들어가는 코드. body-parser의 역할과 동일
            .build()

        apiService = retrofit.create(APIService::class.java)// 아까 정의한 APIService 인터페이스를 만듦
        getPost(id)

        deleteBtn = findViewById<Button>(R.id.post_delete_btn)
        deleteBtn.setOnClickListener {
            deletePost(id)
            finish()
            // 왜 this만 사용하면 안 되는가?
            // A. this가 액티비티가 아닌 익명클래스를 가리키기 때문이다. this가 콜백이므로 바깥의 this임을 알려주기 위해서
            Toast.makeText(this@PostReadActivity,"글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
        }


        modifyBtn = findViewById<Button>(R.id.post_modify_btn)
        modifyBtn.setOnClickListener {
            val intent = Intent(this, PostModifyActivity::class.java)
            intent.putExtra("post", post)
            finish()
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        //showPosts()
    }

    fun getPost( id: Int) {
        val call = apiService.getPost(id)
        call.enqueue(object : Callback<PostResponse> {
            @SuppressLint("WrongViewCast")
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if ( response.isSuccessful) {
                    val data : PostResponse? = response.body()
                    data?.let {
                        post = it.result
                        findViewById<TextView>(R.id.post_title).text = "제목: "+it.result.title
                        findViewById<TextView>(R.id.post_author).text = "글쓴이: "+it.result.author
                        findViewById<TextView>(R.id.post_content).text = "내용: "+it.result.content
                    }
                } else {
                    // 없습니다
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.w("mytag", "failed to call API : "+t.message)
            }

        })
    }

    fun showPosts() {
        val call = apiService.getPosts()
        call.enqueue(object : Callback<AllPostResponse> {
            @SuppressLint("WrongViewCast")
            override fun onResponse(call: Call<AllPostResponse>, response: Response<AllPostResponse>) {
                if ( response.isSuccessful) {
                    val data : AllPostResponse? = response.body()
                    data?.let {
                        Log.d("mytag", it.result.toString())
                        val layoutManager = LinearLayoutManager(this@PostReadActivity)
                        val adapter = PostListAdapter(it.result)
                        val recyclerView = findViewById<RecyclerView>(R.id.post_list)
                        recyclerView.setHasFixedSize(false)
                        recyclerView.layoutManager = layoutManager
                        recyclerView.adapter = adapter
                    }

                } else {
                    // 없습니다
                }
            }

            override fun onFailure(call: Call<AllPostResponse>, t: Throwable) {

            }
        })
    }
    fun deletePost(id: Int) {
        val call = apiService.deletePost(id)
        call.enqueue(object : Callback<StringResponse> {// 익명클래스므로 추상메서드 구현 필요
        override fun onResponse(call: Call<StringResponse>, response: Response<StringResponse>) {
            if ( response.isSuccessful) {
                val data : StringResponse? = response.body()
                data?.let {
                    Log.d("mydelete", it.result.toString())
                }
            } else {

            }
        }

            override fun onFailure(call: Call<StringResponse>, t: Throwable) {

            }
        })
    }
}