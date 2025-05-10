package com.example.dognutrionapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dognutrionapp.R
import com.example.dognutrionapp.databinding.AdminDashboardFragmentBinding
import com.example.dognutrionapp.ui.database.UserDatabaseHelper
import com.example.dognutrionapp.ui.catalog.ProductAdapter
import com.example.dognutrionapp.ui.models.Product

class AdminDashboardFragment : Fragment() {

    private lateinit var dbHelper: UserDatabaseHelper
    private var _binding: AdminDashboardFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = AdminDashboardFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = UserDatabaseHelper(requireContext())

        // Handle Add Product button click
        binding.btnAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_adminDashboardFragment_to_addProductFragment)
        }

        // Handle Manage Users button click
        binding.btnManageUsers.setOnClickListener {
            findNavController().navigate(R.id.action_adminDashboardFragment_to_manageUsersFragment)
        }

        // Display product list (assuming you already have a RecyclerView or ListView)
        val productList = dbHelper.getProducts()
        val adapter = ProductAdapter(productList, requireContext()) { product ->
            showProductActions(product)
        }
        binding.rvProductList.adapter = adapter
    }

    private fun showProductActions(product: Product) {
        Toast.makeText(requireContext(), "Selected Product: ${product.name}", Toast.LENGTH_SHORT).show()

        // Navigate to edit product screen when product is clicked
        binding.btnEditProduct.setOnClickListener {
            findNavController().navigate(
                R.id.action_adminDashboardFragment_to_editProductFragment,
                Bundle().apply {
                    putInt("product_id", product.id)
                }
            )
        }

        // Delete product when clicked
        binding.btnDeleteProduct.setOnClickListener {
            val isDeleted = dbHelper.deleteProduct(product.id)
            if (isDeleted) {
                Toast.makeText(requireContext(), "Product Deleted", Toast.LENGTH_SHORT).show()
                refreshProductList()
            } else {
                Toast.makeText(requireContext(), "Failed to Delete Product", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun refreshProductList() {
        val productList = dbHelper.getProducts()
        val adapter = ProductAdapter(productList, requireContext()) { product ->
            showProductActions(product)
        }
        binding.rvProductList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
