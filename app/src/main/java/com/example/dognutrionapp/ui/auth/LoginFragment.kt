package com.example.dognutrionapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dognutrionapp.R
import com.example.dognutrionapp.databinding.FragmentLoginBinding
import com.example.dognutrionapp.ui.database.UserDatabaseHelper

class LoginFragment : Fragment() {

    private lateinit var dbHelper: UserDatabaseHelper
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = UserDatabaseHelper(requireContext())

        // Handle login button click
        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim()
            val password = binding.etLoginPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val (isUserValid, isAdmin) = dbHelper.checkUser(email, password)

            if (isUserValid) {
                if (isAdmin) {
                    findNavController().navigate(R.id.action_loginFragment_to_adminDashboardFragment)
                } else {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            } else {
                Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGoToSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
