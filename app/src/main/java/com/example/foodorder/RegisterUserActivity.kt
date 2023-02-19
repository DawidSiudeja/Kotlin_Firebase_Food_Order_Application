package com.example.foodorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodorder.databinding.ActivityRegisterUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterUserActivity : AppCompatActivity() {

    lateinit var registerUserBinding: ActivityRegisterUserBinding
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {

        registerUserBinding = ActivityRegisterUserBinding.inflate(layoutInflater)


        super.onCreate(savedInstanceState)

        registerUserBinding.RegisterUserBtn.setOnClickListener {

            val userName = registerUserBinding.nameUserRegister.text.toString()
            val userSurname = registerUserBinding.surnameUserRegister.text.toString()
            val userPhone = registerUserBinding.phoneNumberUserRegister.text.toString().toInt()
            val userEmail = registerUserBinding.mailUserRegister.text.toString()
            val userPassword = registerUserBinding.passwordUserRegister.text.toString()
            val userPassword2 = registerUserBinding.passwordUserRegister2.text.toString()

            registerWithFirebase(userName, userSurname, userPhone, userEmail, userPassword, userPassword2)

        }

        setContentView(registerUserBinding.root)
    }


    fun registerWithFirebase(userName: String,
                             userSurname: String,
                             userPhone: Int,
                             userEmail: String,
                             userPassword: String,
                             userPassword2: String ) {

        if ( userPassword == userPassword2 ) {

            auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->

                if(task.isSuccessful) {

                    val userId = auth.currentUser?.uid.toString()

                    val userData = hashMapOf(
                        "name" to userName,
                        "surname" to userSurname,
                        "phone" to userPhone,
                        "email" to userEmail,
                        "id" to userId,
                        "phone verification" to false,
                        "restaurant" to false
                    )

                    db.collection("users").document(userId).set(userData).addOnSuccessListener {
                        Toast.makeText(applicationContext,
                            "Konto założone poprawnie, prosimy o weryfikacje numeru telefonu w profilu.",
                            Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext,
                            "Dane nie poprawnie zapisane.",
                            Toast.LENGTH_SHORT).show()
                    }

                    val Intent = Intent(this, MainActivity::class.java)
                    startActivity(Intent)
                    finish()

                } else {
                    Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            Toast.makeText(applicationContext, "Hasła się nie zgadzają", Toast.LENGTH_SHORT).show()
        }


    }

}