package com.example.fitnessstepper

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        val bottomPanel = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomPanel)
        bottomPanel.selectedItemId = R.id.navigationSettings
        val logout = findViewById<androidx.cardview.widget.CardView>(R.id.logoutCard)
        val email = intent.getStringExtra("USER_EMAIL")

        logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        bottomPanel.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigationWalk -> {
                    val intent = Intent(this, dashboard::class.java)
                    intent.putExtra("USER_EMAIL", email)
                    val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
                    startActivity(intent, options.toBundle())
                    finish()
                    true
                }
                R.id.navigationToday -> {
                    val intent = Intent(this, Calendar::class.java)
                    intent.putExtra("USER_EMAIL", email)
                    val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
                    startActivity(intent, options.toBundle())
                    finish()
                    true
                }
                R.id.navigationSettings -> {

                    true
                }
                else -> false
            }

        }
    }
}