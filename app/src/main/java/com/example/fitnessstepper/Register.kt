package com.example.fitnessstepper

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val inputEmail = findViewById<EditText>(R.id.inputEmail)
        val inputPassword = findViewById<EditText>(R.id.inputPassword)
        val repeatPassword = findViewById<EditText>(R.id.inputPasswordRepeat)
        val textHaveAccount = findViewById<TextView>(R.id.forgotPassword)
        val createBtn = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.createAccountButton)

        textHaveAccount.setOnClickListener {
            finish()
        }

        createBtn.setOnClickListener {
            val emailAddress = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            val repeatedPassword = repeatPassword.text.toString()

            if(emailAddress.isNotEmpty() && password.isNotEmpty() && repeatedPassword.isNotEmpty() && (password == repeatedPassword)) {
                val DBConnect = dbConnect(this)

                val newUser = Users(emailAddress, password)
                val exist = DBConnect.searchUserExists(emailAddress)

                if(exist) {
                    Toast.makeText(this, "User with this email already exists.", Toast.LENGTH_SHORT).show()
                } else {
                    DBConnect.addUser(newUser)
                    Toast.makeText(this, "User Registered.", Toast.LENGTH_SHORT).show()
                    val Intent = Intent(this, MainActivity::class.java)
                    startActivity(Intent)
                }

            } else if(password != repeatedPassword) {
                Toast.makeText(this, "Passwords must be the same.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}