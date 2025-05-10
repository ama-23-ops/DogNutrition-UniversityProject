package com.example.dognutrionapp.ui.catalog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dognutrionapp.R
import com.example.dognutrionapp.databinding.ItemProductBinding
import com.example.dognutrionapp.ui.database.UserDatabaseHelper
import com.example.dognutrionapp.ui.models.Product

class ProductAdapter(
    private var productList: List<Product>,
    private val context: Context
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private lateinit var dbHelper: UserDatabaseHelper

    init {
        dbHelper = UserDatabaseHelper(context)
    }

    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        with(holder.binding) {
            productName.text = product.name
            productDescription.text = product.description
            productPrice.text = "Price: $${product.price}"
            productRating.text = "Rating: ${product.rating}"

            Glide.with(holder.itemView.context)
                .load(product.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(productImage)


            btnAddToCart.setOnClickListener {
                val isAdded = dbHelper.addToCart(product.id, 1)
                val toastMessage = if (isAdded) {
                    "${product.name} added to cart"
                } else {
                    "Failed to add to cart"
                }
                Toast.makeText(holder.itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
            }

            holder.itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("selected_product", product)
                holder.itemView.findNavController().navigate(R.id.action_catalogFragment_to_productDetailFragment, bundle)
            }
        }
    }

    override fun getItemCount(): Int = productList.size

    fun updateList(updatedList: List<Product>) {
        productList = updatedList
        notifyDataSetChanged()
    }
}

