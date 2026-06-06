package com.example.fitnessstepper

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.jvm.java

class dashboard : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null

    private var running = false
    private var totalSteps = 0f
    private lateinit var DBConnect: dbConnect



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        DBConnect = dbConnect(this)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val bottomPanel = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomPanel)

        bottomPanel.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigationWalk -> {
                    true
                }
                R.id.navigationToday -> {
                    val intent = Intent(this, Calendar::class.java)
                    val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
                    startActivity(intent, options.toBundle())
                    finish()
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

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(stepSensor == null) {
            Toast.makeText(this, "No sensor detected.", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    private var firstValueSteps: Float? = null
    private var currentSteps = 0

    override fun onSensorChanged(event: SensorEvent?) {
        val email = intent.getStringExtra("USER_EMAIL")
        if(running && event != null) {
            totalSteps = event.values[0]

            if(firstValueSteps == null) {
                firstValueSteps = totalSteps
            }

            currentSteps = (totalSteps - (firstValueSteps ?: 0f)).toInt()

            val caloriesBurned = currentSteps * 0.045
            val formattedCalories = String.format(java.util.Locale.US, "%.1f", caloriesBurned)
            val distanceTraveled = currentSteps * 0.725
            val formattedDistance = String.format(java.util.Locale.US, "%.1f", distanceTraveled)
            findViewById<TextView>(R.id.stepsTaken).text = "${currentSteps}/6000"
            findViewById<TextView>(R.id.calories).text = "$formattedCalories kcal"
            findViewById<TextView>(R.id.distance).text = "$formattedDistance m"
        }
    }

    override fun onPause() {
        super.onPause()
        running = false

        val email = intent.getStringExtra("USER_EMAIL")
        DBConnect.setSteps(email, currentSteps)
    }
}