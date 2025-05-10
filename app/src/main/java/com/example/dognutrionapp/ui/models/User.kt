package com.example.dognutrionapp.ui.models

data class User(
    val id: Int? = null,
    val email: String,
    val password: String,
    val name: String,
    val address: String,
    val isAdmin: Boolean = false // Default is false for regular users
)

