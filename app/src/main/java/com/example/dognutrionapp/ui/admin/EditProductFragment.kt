package com.example.dognutrionapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dognutrionapp.R
import com.example.dognutrionapp.databinding.EditProductFragmentBinding
import com.example.dognutrionapp.ui.database.UserDatabaseHelper
import com.example.dognutrionapp.ui.models.Product

class EditProductFragment : Fragment() {

    private lateinit var dbHelper: UserDatabaseHelper
    private var _binding: EditProductFragmentBinding? = null
    private val binding get() = _binding!!
    private var productId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = EditProductFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = UserDatabaseHelper(requireContext())

        // Get the product ID passed from the Admin Dashboard
        val productId = arguments?.getInt("product_id") ?: -1
        val product = dbHelper.getProductById(productId)

        if (product != null) {
            // Populate the fields with current product data
            binding.etProductName.setText(product.name)
            binding.etProductDescription.setText(product.description)
            binding.etProductPrice.setText(product.price.toString())
            binding.etProductRating.setText(product.rating.toString())
        }

        binding.btnSaveChanges.setOnClickListener {
            val name = binding.etProductName.text.toString().trim()
            val description = binding.etProductDescription.text.toString().trim()
            val price = binding.etProductPrice.text.toString().trim().toDoubleOrNull()
            val rating = binding.etProductRating.text.toString().trim().toDoubleOrNull()

            if (name.isEmpty() || description.isEmpty() || price == null || rating == null) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedProduct = Product(productId, name, description, price, rating, "")
            val isUpdated = dbHelper.updateProduct(updatedProduct)
            if (isUpdated) {
                Toast.makeText(requireContext(), "Product Updated", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_editProductFragment_to_adminDashboardFragment)
            } else {
                Toast.makeText(requireContext(), "Failed to Update Product", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
