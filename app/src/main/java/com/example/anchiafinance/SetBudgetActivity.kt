package com.example.anchiafinance

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SetBudgetActivity : AppCompatActivity() {
    private lateinit var txtBudget: EditText
    private lateinit var btnSaveBudget: Button

    companion object {
        var budget: Double = 0.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_budget)

        txtBudget = findViewById(R.id.txtBudget)
        btnSaveBudget = findViewById(R.id.btnSaveBudget)

        btnSaveBudget.setOnClickListener {
            val budgetInput = txtBudget.text.toString().toDoubleOrNull() ?: -1.0

            if (budgetInput <= 0) {
                Toast.makeText(this, getString(R.string.invalid_budget_message), Toast.LENGTH_SHORT).show()
            } else {
                budget = budgetInput
                Toast.makeText(this, getString(R.string.budget_saved_message, budget), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
