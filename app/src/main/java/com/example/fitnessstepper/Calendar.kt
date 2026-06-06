package com.example.fitnessstepper

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Calendar : AppCompatActivity() {

    private lateinit var DBConnect: dbConnect

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calendar)
        val email = intent.getStringExtra("USER_EMAIL")
        val bottomPanel = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomPanel)
        bottomPanel.selectedItemId = R.id.navigationToday

        val check_activity = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.check_activity)
        val inputDate = findViewById<EditText>(R.id.date)
        DBConnect = dbConnect(this)

        check_activity.setOnClickListener {
            val stepsOnDate = DBConnect.getSteps(email, inputDate.text.toString().trim())

            if(stepsOnDate > 0) {
                findViewById<TextView>(R.id.stepsTaken).text = "${stepsOnDate.toString()}/6000"
            } else {
                findViewById<TextView>(R.id.stepsTaken).text = "0/6000"
                Toast.makeText(this, "No data for this date.", Toast.LENGTH_SHORT).show()
            }


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
                    true
                }
                R.id.navigationSettings -> {
                    val intent = Intent(this, Settings::class.java)
                    intent.putExtra("USER_EMAIL", email)
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