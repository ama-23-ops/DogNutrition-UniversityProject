package com.example.dognutrionapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dognutrionapp.MainActivity
import com.example.dognutrionapp.R
import com.example.dognutrionapp.ui.database.UserDatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.dognutrionapp.ui.models.User

class ProfileFragment : Fragment() {

    private lateinit var textName: TextView
    private lateinit var textEmail: TextView
    private lateinit var textAddress: TextView
    private lateinit var buttonAddPersonalInfo: Button
    private lateinit var buttonUpdateInfo: Button
    private lateinit var buttonOrderManagement: Button
    private lateinit var buttonLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textName = view.findViewById(R.id.text_name)
        textEmail = view.findViewById(R.id.text_email)
        textAddress = view.findViewById(R.id.text_address)
        buttonAddPersonalInfo = view.findViewById(R.id.button_add_personal_info)
        buttonUpdateInfo = view.findViewById(R.id.button_update_info)
        buttonOrderManagement = view.findViewById(R.id.button_order_management)
        buttonLogout = view.findViewById(R.id.button_logout)

        // Load user personal info
        loadPersonalInfo()

        // Navigate to Add Personal Info screen
        buttonAddPersonalInfo.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addPersonalInfoFragment)
        }

        // Navigate to Update Personal Info screen
        buttonUpdateInfo.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_updateProfileFragment)
        }

        // Navigate to Order Management screen
        buttonOrderManagement.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_orderManagementFragment)
        }

        // Logout button click listener
        buttonLogout.setOnClickListener {
            logout()
        }
    }

    private fun loadPersonalInfo() {
        val userId = requireActivity()
            .getSharedPreferences("loginPrefs", 0)
            .getInt("userId", -1)

        if (userId == -1) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val dbHelper = UserDatabaseHelper(requireContext())
            val user = dbHelper.getUserProfile()
            withContext(Dispatchers.Main) {
                if (user != null) {   // Check if user is null
                    textName.text = user.name ?: "Not Available"
                    textEmail.text = user.email ?: "Not Available"  // Handle null
                    textAddress.text = user.address ?: "Not Available" // Handle null
                } else {
                    // Handle case where user profile is not found in the database
                    Toast.makeText(requireContext(), "User profile not found", Toast.LENGTH_SHORT).show()
                    // Maybe navigate to the add personal info screen?
                }
            }
        }
    }

    private fun logout() {
        // Clear user session
        val sharedPreferences = requireActivity().getSharedPreferences("loginPrefs", 0)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }

        // Navigate to Home or Login Screen
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Redirect to MainActivity (assuming it's the Home or Login activity)
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
