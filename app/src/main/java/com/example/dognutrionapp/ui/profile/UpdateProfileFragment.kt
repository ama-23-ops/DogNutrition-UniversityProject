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

class UpdateProfileFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEditText = view.findViewById(R.id.edit_text_name)
        emailEditText = view.findViewById(R.id.edit_text_email)
        addressEditText = view.findViewById(R.id.edit_text_address)

        view.findViewById<Button>(R.id.save_button).setOnClickListener {
            // Save profile changes
            Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
        }
    }
}
