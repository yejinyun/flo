package com

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity(), SignUpView {
    lateinit var binding : ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupSignUpBtn.setOnClickListener {
            signUp()
            finish()
        }
    }

    private fun getUser(): User {
        val email: String = binding.signupAddressEt.text.toString() + "@" + binding.signupEmailEt.text.toString()
        val pwd: String = binding.signupPsEt.text.toString()
        val username: String = binding.signupUsernameEt.text.toString()

        return User(email, pwd, username)
    }

//    private fun signUp(){
//        if(binding.signupAddressEt.text.toString().isEmpty() || binding.signupAddressEt.text.toString().isEmpty()){
//            Toast.makeText(this,"이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if (binding.signupUsernameEt.text.toString().isEmpty()) {
//            Toast.makeText(this, "사용자 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//            if (binding.signupPsEt.text.toString() != binding.signupPsCheckEt.text.toString()){
//            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
    //RoomDB를 이용한 회원가입
//        val userDB = SongDatabase.getInstance(this)!!
//        userDB.UserDao().insert(getUser())
//
//        val users = userDB.UserDao().getUsers()
//
//        Log.d("signup Act", users.toString())
//    }

    private fun signUp(){
        if(binding.signupAddressEt.text.toString().isEmpty() || binding.signupAddressEt.text.toString().isEmpty()){
            Toast.makeText(this,"이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signupUsernameEt.text.toString().isEmpty()) {
            Toast.makeText(this, "사용자 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signupPsEt.text.toString() != binding.signupPsCheckEt.text.toString()){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this)

        //우리가 정한 authservice의 signup class를 호출
        authService.signUp((getUser()))

//        //retrofit 객체 만들기
//        val retrofit = Retrofit.Builder().baseUrl("http://13.125.121.202").build()
//        //서비스 인터페잇 넣기
//        val signUpService = retrofit.create(SignUpService::class.java)

        //Retrofit 객체 생성 방법 2
//       val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
//        fun getRetrofit(): Retrofit {
//            val retrofit = Retrofit.Builder().baseUrl("http://13.125.121.202").addConverterFactory(
//                GsonConverterFactory.create()).build()
//            return retrofit
//        }

        //클래스 안에 사인업이 정의가 되어서 호출가능
        //enqueue라는 매소드 사용 가능
        Log.d("SIGNUP/ASYNC","hello")
    }

    override fun onSignUpLoading() {
        binding.signupLoadingPb.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess() {
        binding.signupLoadingPb.visibility = View.GONE

        Toast.makeText(this, "회원가입을 축하합니다!", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSignUpFailure(code: Int, message: String) {
        binding.signupLoadingPb.visibility = View.GONE

        when(code){
            2000, 2016, 2017 -> {
                binding.signupEmailErrorTv.visibility = View.VISIBLE
                binding.signupEmailErrorTv.text = message
            }
        }
    }
    //회원가입 눌렀을떄>레트로핏 객체 생성 > 서비스객체 생성
    // > hello > API 생성 > 서버 응답 > 인큐 매소드 둘 중 하나로
}