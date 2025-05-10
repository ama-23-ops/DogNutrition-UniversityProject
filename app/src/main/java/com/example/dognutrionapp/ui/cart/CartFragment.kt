package com.example.dognutrionapp.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dognutrionapp.R
import com.example.dognutrionapp.ui.database.UserDatabaseHelper
import com.example.dognutrionapp.ui.models.CartItem

class CartFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.cart_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        dbHelper = UserDatabaseHelper(requireContext())

        // Fetch cart items from the database
        val cartItems = getCartItemsFromDatabase()

        if (cartItems.isNotEmpty()) {
            cartAdapter = CartAdapter(cartItems)
            recyclerView.adapter = cartAdapter
        } else {
            Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCartItemsFromDatabase(): List<CartItem> {
        val cartItems = mutableListOf<CartItem>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${UserDatabaseHelper.TABLE_CART}", null)

        if (cursor.moveToFirst()) {
            do {
                val productName = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_PRODUCT_NAME))
                val productQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_CART_QUANTITY))
                val productPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_PRODUCT_PRICE))

                val cartItem = CartItem(
                    productName,
                    productQuantity,
                    productPrice
                )
                cartItems.add(cartItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return cartItems
    }
}
