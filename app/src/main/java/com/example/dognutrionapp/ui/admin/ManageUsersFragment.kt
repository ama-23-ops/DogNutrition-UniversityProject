package com.example.dognutrionapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dognutrionapp.R
import com.example.dognutrionapp.databinding.FragmentManageUsersBinding 
import com.example.dognutrionapp.ui.database.UserDatabaseHelper
import com.example.dognutrionapp.ui.models.User

class ManageUsersFragment : Fragment() {

    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var usersAdapter: UsersAdapter
    private var _binding: FragmentManageUsersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = UserDatabaseHelper(requireContext())
        recyclerView = binding.rvUsersList  // Initialize RecyclerView

        binding.btnAddUser.setOnClickListener {
            findNavController().navigate(R.id.action_manageUsersFragment_to_addUserFragment)
        }

        loadUsers()
    }

    private fun loadUsers() {
        val users = dbHelper.getAllUsers()
        usersAdapter = UsersAdapter(users) 
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = usersAdapter

        if (users.isEmpty()) {
            Toast.makeText(requireContext(), "No users found", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}