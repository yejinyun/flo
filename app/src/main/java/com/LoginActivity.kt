package com

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding

class LoginActivity :AppCompatActivity(), LoginView {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginCloseIv.setOnClickListener {
            finish()
        }
        binding.loginSignUpTv.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        binding.loginLoginBtn.setOnClickListener {
            login()
        }
    }

/*    private fun login(){
        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginEmailEt.text.toString().isEmpty()){
            Toast.makeText(this,"이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.loginPsEt.text.toString().isEmpty()){
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email: String = binding.loginIdEt.text.toString() + "@" + binding.loginEmailEt.text.toString()
        val pwd: String = binding.loginPsEt.text.toString()

        val songDB = SongDatabase.getInstance(this)!!

        val user = songDB.UserDao().getUserinfo(email,pwd)

        user?.let{
            Log.d("LOGINACT/GET_USER","userId: ${user.id},$user")

            //발급받은 jwt를 저장해주는 함수
            saveJwt(user.id, user.username)
            startMainActivity()

            "Login"
            return
        }

        Toast.makeText(this,"회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }*/

    private fun login(){
//        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginEmailEt.text.toString().isEmpty()){
//            Toast.makeText(this,"이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if (binding.loginPsEt.text.toString().isEmpty()){
//            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
        val email: String = binding.loginIdEt.text.toString() + "@" + binding.loginEmailEt.text.toString()
        val pwd: String = binding.loginPsEt.text.toString()
        val user = User(email, pwd, "")

        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(user)
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginLoading() {
        binding.loginLoadingPb.visibility = View.VISIBLE
    }

    override fun onLoginSuccess(auth: Auth) {
        binding.loginLoadingPb.visibility = View.GONE

        saveJwt(this, auth.jwt)
        saveUserIdx(this, auth.userIdx)

        startMainActivity()
        Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onLoginFailure(code: Int, message: String) {
        binding.loginLoadingPb.visibility = View.GONE

        when(code) {
            2015, 2019, 3014 -> {
                binding.loginErrorTv.visibility = View.VISIBLE
                binding.loginErrorTv.text = message
                Toast.makeText(this, "Login Error.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveJwt(context: Context, jwt: String){
        val spf = context.getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("id", jwt)
        editor.apply()
    }

    private fun saveUserIdx(context: Context, jwtIdx: Int){
        val spf = context.getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("id", jwtIdx)
        editor.apply()
    }

}