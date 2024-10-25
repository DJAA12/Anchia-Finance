package com.example.anchiafinance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddBudget : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_budget)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnllamarMainScreen2: Button = findViewById<Button>(R.id.btnSaveBudget)
        btnllamarMainScreen2.setOnClickListener(View.OnClickListener { view ->
            val intentMainScreen2 = Intent(this, MainScreen2::class.java)
            startActivity(intentMainScreen2)
        })

        val btnllamarMainActivity: Button = findViewById<Button>(R.id.btnCancelBudget)
        btnllamarMainActivity.setOnClickListener(View.OnClickListener { view ->
            val intentMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentMainActivity)
        })
    }
}