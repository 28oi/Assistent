package com.example.ts3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailInput)
        passwordEditText = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        progressBar = findViewById(R.id.progressBar)

        val registerLink: Button = findViewById(R.id.registerLink)
        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && isValidEmail(email)) {
                sendLoginDataToBackend(email, password)
            } else {
                Toast.makeText(this, "Введите корректный email и пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailRegex)
    }

    private fun sendLoginDataToBackend(email: String, password: String) {
        progressBar.visibility = ProgressBar.VISIBLE

        val request = LoginRequest(email, password)

        RetrofitClient.apiService.login(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                progressBar.visibility = ProgressBar.INVISIBLE

                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Успешный вход", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, TranscribeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Неверный email или пароль", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, TranscribeActivity::class.java))
                    finish()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                progressBar.visibility = ProgressBar.INVISIBLE
                Toast.makeText(this@LoginActivity, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
