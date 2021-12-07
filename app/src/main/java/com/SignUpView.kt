package com

interface SignUpView {
    //액티비티랑 서비스 클래스를 연결한 인터페이스
    fun onSignUpLoading()
    fun onSignUpSuccess()
    fun onSignUpFailure(code: Int, message: String)
}