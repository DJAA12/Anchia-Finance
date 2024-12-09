package com.example.anchiafinance

import Model.FinanceModel
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SetBudgetActivity : AppCompatActivity() {
    private lateinit var txtBudget: EditText
    private lateinit var btnSaveBudget: Button
    private val financeModel = FinanceModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_budget)

        txtBudget = findViewById(R.id.txtBudget)
        btnSaveBudget = findViewById(R.id.btnSaveBudget)

        loadCurrentBudget()

        btnSaveBudget.setOnClickListener {
            val budgetInput = txtBudget.text.toString().toDoubleOrNull() ?: -1.0

            if (budgetInput <= 0) {
                Toast.makeText(this, getString(R.string.invalid_budget_message), Toast.LENGTH_SHORT).show()
            } else {
                saveBudgetToDatabase(budgetInput)
            }
        }
    }

    private fun loadCurrentBudget() {
        try {
            val currentBudget = financeModel.getBudget()
            if (currentBudget != null && currentBudget > 0) {
                txtBudget.setText(currentBudget.toString())
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_loading_budget), Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveBudgetToDatabase(newBudget: Double) {
        try {
            financeModel.setBudget(newBudget)
            Toast.makeText(this, getString(R.string.budget_saved_message, newBudget), Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_saving_budget), Toast.LENGTH_SHORT).show()
        }
    }
}
