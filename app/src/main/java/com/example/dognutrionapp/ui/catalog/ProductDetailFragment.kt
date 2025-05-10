package com.example.dognutrionapp.ui.catalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.dognutrionapp.R
import com.example.dognutrionapp.databinding.FragmentProductDetailBinding
import com.example.dognutrionapp.ui.database.UserDatabaseHelper
import com.example.dognutrionapp.ui.models.Product

class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = UserDatabaseHelper(requireContext())

        val product = arguments?.getParcelable<Product>("selected_product")

        if (product != null) {
            binding.apply {
                productName.text = product.name
                productDescription.text = product.description
                productPrice.text = "Price: $${product.price}"
                productRating.text = "Rating: ${product.rating}"

                Glide.with(requireContext())
                    .load(product.imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(productImage)

                btnAddToCart.setOnClickListener {
                    val isAdded = dbHelper.addToCart(product.id, 1)
                    if (isAdded) {
                        Toast.makeText(requireContext(), "${product.name} added to cart", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Log.e("ProductDetailFragment", "Product is null!")
            Toast.makeText(requireContext(), "Product not found", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}