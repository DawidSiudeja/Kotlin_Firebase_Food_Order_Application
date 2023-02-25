package com.example.foodorder

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.foodorder.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        turnOnRestaurantAccount()

        mainBinding.navLogout.setOnClickListener {
            logout()
        }

        mainBinding.navEdit.setOnClickListener {
            val intent = Intent(this, AdminListProducts::class.java)
            startActivity(intent)
        }


        mainBinding.navHome.setTextColor(Color.parseColor("#FF0000"));
        setContentView(mainBinding.root)
    }


    private fun logout() {
        auth.signOut()
        val intent = Intent(this, LoginRegisterUser::class.java)
        startActivity(intent)
        finish()
    }


    private fun turnOnRestaurantAccount() {
        val userId = auth.currentUser?.uid.toString()
        var resturant = true

        db.collection("users").whereEqualTo("id", userId).get().addOnSuccessListener { result ->
            for (document in result) {
                resturant = document.get("restaurant") as Boolean

                if (resturant == true) {
                    mainBinding.navEdit.visibility = View.VISIBLE
                    mainBinding.navBasket.visibility = View.GONE
                }

            }
        }.addOnFailureListener { exception ->
            Log.d(ContentValues.TAG, "Error getting documents: ", exception)
        }
    }
}