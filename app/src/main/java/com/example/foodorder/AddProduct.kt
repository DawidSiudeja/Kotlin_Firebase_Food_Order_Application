package com.example.foodorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodorder.databinding.ActivityAddProductBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddProduct : AppCompatActivity() {

    lateinit var addProductBinding: ActivityAddProductBinding
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addProductBinding = ActivityAddProductBinding.inflate(layoutInflater)

        addProductBinding.editProductAddBtn.setOnClickListener {

            val productName = addProductBinding.editProductName.text.toString()
            val productDesc = addProductBinding.editProductDesc.text.toString()
            val productPrice = addProductBinding.editProductPrice.text.toString()

            addingProductsToDatabase(productName, productDesc, productPrice)

        }

        setContentView(addProductBinding.root)
    }


    private fun addingProductsToDatabase(productName: String, productDesc: String, productPrice: String) {

        val productData = hashMapOf(
            "name" to productName,
            "desc" to productDesc,
            "price" to productPrice,
        )

        db.collection("products").add(productData).addOnSuccessListener {
            Toast.makeText(applicationContext,
                "Produkt został dodany poprawnie.",
                Toast.LENGTH_SHORT).show()

            addProductBinding.editProductName.setText("")
            addProductBinding.editProductDesc.setText("")
            addProductBinding.editProductPrice.setText("")

        }.addOnFailureListener {
            Toast.makeText(applicationContext,
                "Dane nie poprawnie zapisane.",
                Toast.LENGTH_SHORT).show()
        }
    }

}