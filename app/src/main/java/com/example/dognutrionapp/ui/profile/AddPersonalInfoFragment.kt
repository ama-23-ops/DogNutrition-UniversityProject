package com.example.dognutrionapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dognutrionapp.R
import com.example.dognutrionapp.ui.database.UserDatabaseHelper

class AddPersonalInfoFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_personal_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEditText = view.findViewById(R.id.edit_text_name)
        emailEditText = view.findViewById(R.id.edit_text_email)
        addressEditText = view.findViewById(R.id.edit_text_address)
        dbHelper = UserDatabaseHelper(requireContext())

        view.findViewById<Button>(R.id.save_button).setOnClickListener {
            savePersonalInfo()
        }
    }

    private fun savePersonalInfo() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val address = addressEditText.text.toString()

        if (name.isEmpty() || email.isEmpty() || address.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User(id = null, name = name, email = email, password = "", address = address, isAdmin = isAdmin)
        val success = dbHelper.insertUserProfile(user)
        if (success) {
            Toast.makeText(requireContext(), "Personal Information Added", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        } else {
            Toast.makeText(requireContext(), "Error adding information", Toast.LENGTH_SHORT).show()
        }
    }
}
