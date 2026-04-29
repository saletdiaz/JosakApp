package edu.josakapp.proyectoJosakapp

import android.content.Intent
import android.os.Bundle
import android.app.Activity

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Pager)
        super.onCreate(savedInstanceState)

        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}