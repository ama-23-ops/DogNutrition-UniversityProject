package com.example.dognutrionapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dognutrionapp.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.go_to_catalog).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_catalogFragment)
        }

        view.findViewById<View>(R.id.go_to_cart).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
        }

        view.findViewById<View>(R.id.go_to_profile).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }
}
