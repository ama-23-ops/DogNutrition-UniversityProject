package com.example.dognutrionapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dognutrionapp.R
import com.example.dognutrionapp.databinding.FragmentSignupBinding
import com.example.dognutrionapp.ui.database.UserDatabaseHelper

class SignupFragment : Fragment() {

    private lateinit var dbHelper: UserDatabaseHelper
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = UserDatabaseHelper(requireContext())

        // Handle signup button click
        binding.btnSignup.setOnClickListener {
            val name = binding.etSignupName.text.toString().trim()
            val email = binding.etSignupEmail.text.toString().trim()
            val password = binding.etSignupPassword.text.toString().trim()
            val isAdmin: Boolean = true

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if user already exists
            val userExists = dbHelper.getUserByEmail(email)
            if (userExists) {
                Toast.makeText(requireContext(), "User already registered with this email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Add user to the database as a regular user (not admin)
            val isUserAdded = dbHelper.addUser(email, name, password, false);
            if (isUserAdded) {
                Toast.makeText(requireContext(), "Signup Successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            } else {
                Toast.makeText(requireContext(), "Signup Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
