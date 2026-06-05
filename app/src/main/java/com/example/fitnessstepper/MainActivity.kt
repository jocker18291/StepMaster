package com.example.fitnessstepper

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.media.audiofx.Virtualizer
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var PERMISSION_PHYSICAL_ACTIVITY = Manifest.permission.ACTIVITY_RECOGNITION
    private val REQUEST_CODE_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val inputEmail = findViewById<EditText>(R.id.inputEmail)
        val inputPassword = findViewById<EditText>(R.id.inputPassword)
        val loginBtn = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.loginButton)
        val createBtn = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.buttonCreate)
        val DBConnect = dbConnect(this)

        createBtn.setOnClickListener {
            val Intent = Intent(this, Register::class.java)
            startActivity(Intent)
        }

        loginBtn.setOnClickListener {
            val emailAddress = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            val exist = DBConnect.searchUserExists(emailAddress)
            val passwordCorrect = DBConnect.checkPassword(emailAddress, password)

            if(emailAddress.isNotEmpty() && password.isNotEmpty() && exist && passwordCorrect) {

                if(ActivityCompat.checkSelfPermission(this, PERMISSION_PHYSICAL_ACTIVITY) != PackageManager.PERMISSION_GRANTED){
                    requestRuntimePermission()
                } else {
                    val intent = Intent(this, dashboard::class.java)
                    intent.putExtra("USER_EMAIL", emailAddress)
                    startActivity(intent)
                    finish()
                }

            }else if(!exist){
                Toast.makeText(this, "User does not exist.", Toast.LENGTH_SHORT).show()
            }
            else if (!passwordCorrect) {
                Toast.makeText(this, "Password not correct.", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "All fields required.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun requestRuntimePermission() {
        if(ActivityCompat.checkSelfPermission(this, PERMISSION_PHYSICAL_ACTIVITY) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted for Physical Activity.", Toast.LENGTH_SHORT).show()
        } else if(ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_PHYSICAL_ACTIVITY)) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("This app requires Physical Activity permission.")
                .setTitle("Permission Required")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, which ->
                    ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_PHYSICAL_ACTIVITY), 100)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
            builder.show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_PHYSICAL_ACTIVITY), 100)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CODE_PERMISSION) {
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted for Physical Activity.", Toast.LENGTH_SHORT).show()
            } else if(!ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_PHYSICAL_ACTIVITY)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("This feature is unavailable because this feature requires permission that you have denied.")
                    .setTitle("Permission Required")
                    .setCancelable(false)
                    .setNegativeButton("Cancel", {dialog, which -> dialog.dismiss()})
                    .setPositiveButton("Settings") { dialog, which ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.setData(uri)
                        startActivity(intent)

                        dialog.dismiss()
                    }
                builder.show()
            } else {
                requestRuntimePermission()
            }
        }
    }
}