package com.example.dognutrionapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dognutrionapp.R
import com.example.dognutrionapp.databinding.FragmentOrderManagementBinding

class OrderManagementFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.update_button).setOnClickListener {
            Toast.makeText(requireContext(), "Order updated!", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.delete_button).setOnClickListener {
            Toast.makeText(requireContext(), "Order deleted!", Toast.LENGTH_SHORT).show()
        }
    }
}
