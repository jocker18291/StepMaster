package com.example.fitnessstepper

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Calendar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calendar)
        val bottomPanel = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomPanel)
        bottomPanel.selectedItemId = R.id.navigationToday

        bottomPanel.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigationWalk -> {
                    val intent = Intent(this, dashboard::class.java)
                    val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
                    startActivity(intent, options.toBundle())
                    finish()
                    true
                }
                R.id.navigationToday -> {
                    true
                }
                R.id.navigationSettings -> {
                    val intent = Intent(this, Settings::class.java)
                    val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
                    startActivity(intent, options.toBundle())
                    finish()
                    true
                }
                else -> false
            }

        }
    }
}