package com.example.foodorder

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodorder.databinding.ActivityEditBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.util.*

class EditActivity : AppCompatActivity() {

    lateinit var editBinding: ActivityEditBinding
    val db = Firebase.firestore
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null
    var storageReference = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {

        editBinding = ActivityEditBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        var documentID = intent.getStringExtra("id").toString()
        var documentName = intent.getStringExtra("name").toString()
        var documentDesc = intent.getStringExtra("desc").toString()
        var documentPrice = intent.getStringExtra("price").toString()
        var documentImage = intent.getStringExtra("image").toString()

        editBinding.editProductName.setText(documentName)
        editBinding.editProductDesc.setText(documentDesc)
        editBinding.editProductPrice.setText(documentPrice)

        if (documentImage != null) {
            Picasso.get().load(documentImage).into(editBinding.editProductImage)
        }

        registerActivityForResult()


        editBinding.editProductSaveBtn.setOnClickListener {

            var productName = editBinding.editProductName.text.toString()
            var productDesc = editBinding.editProductDesc.text.toString()
            var productPrice = editBinding.editProductPrice.text.toString()

            uploadPhoto(productName, productDesc, productPrice, documentID, documentImage)
        }

        editBinding.editProductImage.setOnClickListener {
            chooseImage()
        }


        setContentView(editBinding.root)
    }

    private fun updateData(productName: String, productDesc: String, productPrice: String, productID: String, productImage: String) {

        val updates = hashMapOf<String, Any>(
            "name" to productName,
            "desc" to productDesc,
            "price" to productPrice,
            "image" to productImage
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

                        Picasso.get().load(it).into(editBinding.editProductImage)

                    }

                }

            })

    }


    private fun uploadPhoto(productName: String, productDesc: String, productPrice: String, productID: String, documentImage: String) {

        val imageName = UUID.randomUUID().toString()

        val imageReference = storageReference.child("images").child(imageName)

        val storageProd = storage.getReferenceFromUrl(documentImage!!)
        storageProd.delete()

        imageUri?.let { uri ->
            imageReference.putFile(uri).addOnSuccessListener {
                val myUploaddedImageReference = storageReference.child("images").child(imageName)
                myUploaddedImageReference.downloadUrl.addOnSuccessListener { url ->

                    var imageUrl = url.toString()


                    updateData(productName, productDesc, productPrice, productID, imageUrl)
                }
                myUploaddedImageReference.downloadUrl.addOnFailureListener {
                    Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

}