package com.example.foodorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorder.databinding.ActivityAdminListProductsBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import kotlin.collections.ArrayList

class AdminListProducts : AppCompatActivity() {

    lateinit var adminListProductsBinding: ActivityAdminListProductsBinding

    private lateinit var productArrayList: ArrayList<Products>
    private lateinit var productsAdapter: ProductsAdapter

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adminListProductsBinding = ActivityAdminListProductsBinding.inflate(layoutInflater)

        adminListProductsBinding.addProd.setOnClickListener {

            val intent = Intent(this, AddProduct::class.java)
            startActivity(intent)

        }
        adminListProductsBinding.recyclerViewEdit.layoutManager = LinearLayoutManager(this)
        adminListProductsBinding.recyclerViewEdit.setHasFixedSize(true)

        productArrayList = arrayListOf()

        productsAdapter = ProductsAdapter(productArrayList, this)

        adminListProductsBinding.recyclerViewEdit.adapter = productsAdapter

        EventChangeListener()

        adminListProductsBinding.recyclerViewEdit.adapter

        setContentView(adminListProductsBinding.root)
    }

    private fun EventChangeListener() {

        db.collection("products")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null) {
                        Log.e("Firestore error: ", error.message.toString())
                        return
                    }

                    for(dc: DocumentChange in value?.documentChanges!!) {
                        if(dc.type == DocumentChange.Type.ADDED){
                            productArrayList.add(dc.document.toObject(Products::class.java))
                        }
                    }

                    productsAdapter.notifyDataSetChanged()

                }
            })

    }
}