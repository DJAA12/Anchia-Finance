package com.example.anchiafinance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddNewCategory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_category)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnllamarManageCategories: Button = findViewById<Button>(R.id.btnSaveCategory)
        btnllamarManageCategories.setOnClickListener(View.OnClickListener { view ->
            val intentManageCategories = Intent(this, ManageCategories::class.java)
            startActivity(intentManageCategories)
        })
        val btnllamarManageCategories2: Button = findViewById<Button>(R.id.btnCancelCategory)
        btnllamarManageCategories2.setOnClickListener(View.OnClickListener { view ->
            val intentManageCategories2 = Intent(this, ManageCategories::class.java)
            startActivity(intentManageCategories2)
        })

    }
}