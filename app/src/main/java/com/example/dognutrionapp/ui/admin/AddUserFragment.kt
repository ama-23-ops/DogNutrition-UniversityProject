package com.example.dognutrionapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dognutrionapp.databinding.FragmentAddUserBinding
import com.example.dognutrionapp.ui.database.UserDatabaseHelper

class AddUserFragment : Fragment() {
    private lateinit var userDatabaseHelper: UserDatabaseHelper
    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDatabaseHelper = UserDatabaseHelper(requireContext())

        binding.btnAddUser.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val name = binding.etName.text.toString()
            val password = binding.etPassword.text.toString()
            val isAdmin = binding.checkboxIsAdmin.isChecked

            if (email.isNotBlank() && name.isNotBlank() && password.isNotBlank()) {
                val isUserAdded = userDatabaseHelper.addUser(email, name, password, isAdmin)

                if (isUserAdded) {
                    Toast.makeText(requireContext(), "User added successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp() // Navigate back after adding user
                } else {
                    Toast.makeText(requireContext(), "Error adding user", Toast.LENGTH_SHORT).show()
                }


            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}