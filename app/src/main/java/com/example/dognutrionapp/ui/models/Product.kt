package com.example.dognutrionapp.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val imageUrl: String // This will store drawable resource id or a placeholder for images
) : Parcelable
