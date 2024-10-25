package com.example.anchiafinance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainScreen2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_screen2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnllamarManageCategories: Button = findViewById<Button>(R.id.btnmanagercategories)
        btnllamarManageCategories.setOnClickListener(View.OnClickListener { view ->
            val intentManageCategories = Intent(this, ManageCategories::class.java)
            startActivity(intentManageCategories)
        })

        val btnllamarAddTransactions: Button = findViewById<Button>(R.id.btnaddtransaction)
        btnllamarAddTransactions.setOnClickListener(View.OnClickListener { view ->
            val intentAddTransactions = Intent(this, AddTransactions::class.java)
            startActivity(intentAddTransactions)
        })

        val btnllamarAddBudget: Button = findViewById<Button>(R.id.btneditincome)
        btnllamarAddBudget.setOnClickListener(View.OnClickListener { view ->
            val intentAddBudget = Intent(this, AddBudget::class.java)
            startActivity(intentAddBudget)
        })


    }
}