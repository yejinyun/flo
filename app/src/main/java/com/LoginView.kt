package com

interface LoginView {
    //액티비티에서 작업을 처리할 수 있는 뷰인터페이스 만들기
    fun onLoginLoading()
    fun onLoginSuccess(auth: Auth)
    fun onLoginFailure(code: Int, message: String)
}