package com.example.foodorder

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


val db = Firebase.firestore


class ProductsAdapter(private val productList: ArrayList<Products>, private val context: Context): RecyclerView.Adapter<ProductsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_edit,
        parent, false)

        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ProductsAdapter.MyViewHolder, position: Int) {

        val product: Products = productList[position]
        holder.productName.text = product.name
        holder.productDesc.text = product.desc
        holder.productPrice.text = product.price


        holder.removeProduct.setOnClickListener {
            deleteItem(product.name.toString())
        }

        holder.editProduct.setOnClickListener {
            pushWithData(product.name.toString(), product.desc.toString(), product.price.toString())
        }

    }


    override fun getItemCount(): Int {

        return productList.size

    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productName: TextView = itemView.findViewById(R.id.productName)
        val productDesc: TextView = itemView.findViewById(R.id.productDesc)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)

        val editProduct: TextView = itemView.findViewById(R.id.editFromList)
        val removeProduct: TextView = itemView.findViewById(R.id.removeFromList)

    }

    private fun deleteItem(productName: String) {

        val collectionRef = db.collection("products")
        val query = collectionRef.whereEqualTo("name", productName)

        query.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val documentId = document.id
                db.collection("products").document(documentId).delete()
                Log.d(TAG,"Udało się usunąć element")
                notifyDataSetChanged()
                (context as Activity).recreate()
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Wystąpił błąd podczas pobierania dokumentów: ", exception)
        }

    }


    private fun pushWithData(productName: String, productDesc: String, productPrice: String) {

        val collectionRef = db.collection("products")
        val query = collectionRef.whereEqualTo("name", productName)

        query.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val documentId = document.id

                val intent = Intent(context, EditActivity::class.java)
                intent.putExtra("id", documentId)
                intent.putExtra("name", productName)
                intent.putExtra("desc", productDesc)
                intent.putExtra("price", productPrice)
                context.startActivity(intent)

                notifyDataSetChanged()
                (context as Activity).recreate()
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Wystąpił błąd podczas pobierania dokumentów: ", exception)
        }


    }




}

