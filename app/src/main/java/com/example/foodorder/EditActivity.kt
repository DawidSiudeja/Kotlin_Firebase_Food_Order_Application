package com.example.foodorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodorder.databinding.ActivityEditBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditActivity : AppCompatActivity() {

    lateinit var editBinding: ActivityEditBinding
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {

        editBinding = ActivityEditBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        var documentID = intent.getStringExtra("id").toString()
        var documentName = intent.getStringExtra("name").toString()
        var documentDesc = intent.getStringExtra("desc").toString()
        var documentPrice = intent.getStringExtra("price").toString()

        editBinding.editProductName.setText(documentName)
        editBinding.editProductDesc.setText(documentDesc)
        editBinding.editProductPrice.setText(documentPrice)


        editBinding.editProductSaveBtn.setOnClickListener {

            var productName = editBinding.editProductName.text.toString()
            var productDesc = editBinding.editProductDesc.text.toString()
            var productPrice = editBinding.editProductPrice.text.toString()

            updateData(productName, productDesc, productPrice, documentID)
        }


        setContentView(editBinding.root)
    }

    private fun updateData(productName: String, productDesc: String, productPrice: String, productID: String,) {

        val updates = hashMapOf<String, Any>(
            "name" to productName,
            "desc" to productDesc,
            "price" to productPrice
        )

        db.collection("products").document(productID).set(updates).addOnSuccessListener {
            Toast.makeText(applicationContext,
                "Produkt zaktualizowany",
                Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(applicationContext,
                "Dane nie poprawnie zapisane.",
                Toast.LENGTH_SHORT).show()
        }

    }


}