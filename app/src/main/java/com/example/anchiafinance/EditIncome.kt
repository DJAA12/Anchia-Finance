package com.example.anchiafinance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditIncome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_income)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnllamarMainScreen3: Button = findViewById<Button>(R.id.btnSaveEditIncome)
        btnllamarMainScreen3.setOnClickListener(View.OnClickListener { view ->
            val intentManageMainScreen3 = Intent(this, MainScreen3::class.java)
            startActivity(intentManageMainScreen3)
        })
        val btnllamarMainScreen32: Button = findViewById<Button>(R.id.btnCancelEditIncome)
        btnllamarMainScreen32.setOnClickListener(View.OnClickListener { view ->
            val intentManageMainScreen32 = Intent(this, MainScreen3::class.java)
            startActivity(intentManageMainScreen32)
        })
    }
}