package com.example.dognutrionapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dognutrionapp.R
import androidx.navigation.fragment.findNavController
import com.example.dognutrionapp.databinding.AddProductFragmentBinding
import com.example.dognutrionapp.ui.database.UserDatabaseHelper
import com.example.dognutrionapp.ui.models.Product

class AddProductFragment : Fragment() {

    private lateinit var dbHelper: UserDatabaseHelper
    private var _binding: AddProductFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = AddProductFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = UserDatabaseHelper(requireContext())

        // Handle Add Product button click
        binding.btnSaveProduct.setOnClickListener {
            val name = binding.etProductName.text.toString().trim()
            val description = binding.etProductDescription.text.toString().trim()
            val price = binding.etProductPrice.text.toString().trim().toDoubleOrNull()
            val rating = binding.etProductRating.text.toString().trim().toDoubleOrNull()

            if (name.isEmpty() || description.isEmpty() || price == null || rating == null) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newProduct = Product(0, name, description, price, rating,  "")
            val isProductAdded = dbHelper.addProduct(newProduct)
            if (isProductAdded) {
                Toast.makeText(requireContext(), "Product Added", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addProductFragment_to_adminDashboardFragment)
            } else {
                Toast.makeText(requireContext(), "Failed to Add Product", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
