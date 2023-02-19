package com.example.foodorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodorder.databinding.ActivityLoginRegisterUserBinding
import com.google.firebase.auth.FirebaseAuth

class LoginRegisterUser : AppCompatActivity() {

    lateinit var loginRegisterUserBinding: ActivityLoginRegisterUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        var auth: FirebaseAuth = FirebaseAuth.getInstance()

        loginRegisterUserBinding = ActivityLoginRegisterUserBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)


        if (auth.getCurrentUser() != null ) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        loginRegisterUserBinding.LoginUserBtn.setOnClickListener {

            val userLogin = loginRegisterUserBinding.loginUser.text.toString()
            val userPassword = loginRegisterUserBinding.passwordUser.text.toString()

            auth.signInWithEmailAndPassword(userLogin, userPassword).addOnCompleteListener { task ->
                if(task.isSuccessful) {

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }

            }

        }


        loginRegisterUserBinding.registerUser.setOnClickListener {

            val intent = Intent(this, RegisterUserActivity::class.java)
            startActivity(intent)

        }


        setContentView(loginRegisterUserBinding.root)
    }
}