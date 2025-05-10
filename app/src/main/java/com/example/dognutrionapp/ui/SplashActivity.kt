package com.example.dognutrionapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.dognutrionapp.MainActivity
import com.example.dognutrionapp.R
import com.example.dognutrionapp.ui.auth.LoginFragment

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay for 2 seconds before navigating
        Handler(Looper.getMainLooper()).postDelayed({
            // Retrieve login status from SharedPreferences
            val sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

            // Navigate to MainActivity or LoginActivity based on login status
            val intent = if (isLoggedIn) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginFragment::class.java)
            }

            startActivity(intent)
            finish() // Close SplashActivity
        }, 2000) // 2-second delay
    }
}
