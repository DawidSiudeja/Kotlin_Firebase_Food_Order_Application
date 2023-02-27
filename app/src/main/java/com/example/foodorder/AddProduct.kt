package com.example.foodorder

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodorder.databinding.ActivityAddProductBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*
import com.google.firebase.storage.ktx.storage

class AddProduct : AppCompatActivity() {

    lateinit var addProductBinding: ActivityAddProductBinding
    val db = Firebase.firestore
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null
    var storageReference = Firebase.storage.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addProductBinding = ActivityAddProductBinding.inflate(layoutInflater)

        registerActivityForResult()

        addProductBinding.editProductAddBtn.setOnClickListener {

            val productName = addProductBinding.editProductName.text.toString()
            val productDesc = addProductBinding.editProductDesc.text.toString()
            val productPrice = addProductBinding.editProductPrice.text.toString()

            uploadPhoto(productName, productDesc, productPrice)

        }

        addProductBinding.editProductImage.setOnClickListener {
            chooseImage()
        }


        setContentView(addProductBinding.root)
    }

    private fun chooseImage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activityResultLauncher.launch(intent)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            chooseImage()

        }

    }

    private fun registerActivityForResult() {

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ActivityResultCallback { result ->

                val resultCode = result.resultCode
                val imageData = result.data

                if (resultCode == RESULT_OK && imageData != null) {

                    imageUri = imageData.data

                    // Picasso

                    imageUri?.let {

                        Picasso.get().load(it).into(addProductBinding.editProductImage)

                    }

                }

            })

    }


    private fun addingProductsToDatabase(productName: String, productDesc: String, productPrice: String, imageUrl: String) {
        val productData = hashMapOf(
            "name" to productName,
            "desc" to productDesc,
            "price" to productPrice,
            "image" to imageUrl
        )

        db.collection("products").add(productData).addOnSuccessListener {
            Toast.makeText(
                applicationContext,
                "Produkt został dodany poprawnie.",
                Toast.LENGTH_SHORT
            ).show()

        }.addOnFailureListener {
            Toast.makeText(
                applicationContext,
                "Dane nie poprawnie zapisane.",
                Toast.LENGTH_SHORT
            ).show()
        }

        addProductBinding.editProductName.setText("")
        addProductBinding.editProductDesc.setText("")
        addProductBinding.editProductPrice.setText("")
    }


    private fun uploadPhoto(productName: String, productDesc: String, productPrice: String) {

        val imageName = UUID.randomUUID().toString()

        val imageReference = storageReference.child("images").child(imageName)

        imageUri?.let { uri ->
            imageReference.putFile(uri).addOnSuccessListener {
                val myUploaddedImageReference = storageReference.child("images").child(imageName)
                myUploaddedImageReference.downloadUrl.addOnSuccessListener { url ->

                    var imageUrl = url.toString()

                    addingProductsToDatabase(productName, productDesc, productPrice, imageUrl)
                }
                myUploaddedImageReference.downloadUrl.addOnFailureListener {
                    Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }
}