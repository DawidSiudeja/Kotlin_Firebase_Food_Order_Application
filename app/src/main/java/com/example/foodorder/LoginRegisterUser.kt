package com.example.foodorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodorder.databinding.ActivityLoginRegisterUserBinding

class LoginRegisterUser : AppCompatActivity() {

    lateinit var loginRegisterUserBinding: ActivityLoginRegisterUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        loginRegisterUserBinding = ActivityLoginRegisterUserBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)



        setContentView(loginRegisterUserBinding.root)
    }
}