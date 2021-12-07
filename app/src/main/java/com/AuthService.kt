package com

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthService {
    private lateinit var loginView: LoginView
    private lateinit var  signUpView: SignUpView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    fun signUp(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        signUpView.onSignUpLoading()

        authService.signUp(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/API-RESPONSE", response.toString())

                val resp = response.body()!!

                Log.d("SIGNUPACT/API-RESPONSE-FLO", resp.toString())

                when(resp.code){
                    1000 -> signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure(resp.code, resp.message)
                    }
                }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                //네트워크 자체가 실패했을 때
                Log.d("LOGINACT/API-RESPONSE", t.toString())
                signUpView.onSignUpFailure(400, "네트워크 오류 발생")
            }
        })
    }

    fun login(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        loginView.onLoginLoading()

        authService.login(user).enqueue(object : Callback<AuthResponse>{
            override fun onResponse(cal: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGINACT/API-RESPONSE", response.toString())

                val resp = response.body()!!

                Log.d("LOGIN/API-RESPONSE-FLO", resp.toString())

                when(resp.code) {
                    1000 -> loginView.onLoginSuccess(resp.result!!)
                    else -> loginView.onLoginFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t:Throwable){
                Log.d("LOGINACT/API-RESPONSE", t.toString())
                loginView.onLoginFailure(400, t.toString())
            }
        })
    }

    fun getRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder().baseUrl("http://13.125.121.202").addConverterFactory(
        GsonConverterFactory.create()).build()
        return retrofit
    }
}