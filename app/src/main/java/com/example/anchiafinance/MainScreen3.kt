package com.example.anchiafinance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainScreen3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_screen3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnllamarManageCategories: Button = findViewById<Button>(R.id.btnManageCategories)
        btnllamarManageCategories.setOnClickListener(View.OnClickListener { view ->
            val intentManageCategories3 = Intent(this, ManageCategories::class.java)
            startActivity(intentManageCategories3)
        })
        val btnllamarAddTransactions: Button = findViewById<Button>(R.id.btnAddTransaction)
        btnllamarAddTransactions.setOnClickListener(View.OnClickListener { view ->
            val intentAddTransactions = Intent(this, AddTransactions::class.java)
            startActivity(intentAddTransactions)
        })
        val btnllamarEditIncome: Button = findViewById<Button>(R.id.btnEditIncome)
        btnllamarEditIncome.setOnClickListener(View.OnClickListener { view ->
            val intentEditIncome = Intent(this, EditIncome::class.java)
            startActivity(intentEditIncome)
        })
        val btnllamarTransactionList: Button = findViewById<Button>(R.id.btnManageTransactions)
        btnllamarTransactionList.setOnClickListener(View.OnClickListener { view ->
            val intentTransactionList = Intent(this, TransactionList::class.java)
            startActivity(intentTransactionList)
        })
        val btnllamarFinancialSummary: Button = findViewById<Button>(R.id.btnViewFinancial)
        btnllamarFinancialSummary.setOnClickListener(View.OnClickListener { view ->
            val intentFinancialSummary = Intent(this, FinancialSummary::class.java)
            startActivity(intentFinancialSummary)
        })
    }
}