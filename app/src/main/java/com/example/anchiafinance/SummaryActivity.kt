package com.example.anchiafinance

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import Model.FinanceModel

class SummaryActivity : AppCompatActivity() {
    private lateinit var lblTotalIncome: TextView
    private lateinit var lblTotalExpense: TextView
    private lateinit var lblBalance: TextView
    private val financeModel = FinanceModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        lblTotalIncome = findViewById(R.id.lblTotalIncome)
        lblTotalExpense = findViewById(R.id.lblTotalExpense)
        lblBalance = findViewById(R.id.lblBalance)

        updateSummary()
    }

    private fun updateSummary() {
        try {
            val transactions = financeModel.getTransactions()
            val budget = financeModel.getBudget() ?: 0.0

            if (transactions.isEmpty()) {
                lblTotalIncome.text = getString(R.string.no_transactions)
                lblTotalExpense.text = getString(R.string.no_transactions)
                lblBalance.text = getString(R.string.no_transactions)
                return
            }

            val incomeType = getString(R.string.transaction_type_income)
            val expenseType = getString(R.string.transaction_type_expense)

            val totalIncome = transactions.filter { it.type.equals(incomeType, ignoreCase = true) }.sumOf { it.amount } + budget
            val totalExpense = transactions.filter { it.type.equals(expenseType, ignoreCase = true) }.sumOf { it.amount }
            val balance = totalIncome - totalExpense

            lblTotalIncome.text = getString(R.string.total_income2, totalIncome)
            lblTotalExpense.text = getString(R.string.total_expense2, totalExpense)
            lblBalance.text = getString(R.string.current_balance2, balance)

            Log.d("SummaryActivity", "Total Income: $totalIncome")
            Log.d("SummaryActivity", "Total Expense: $totalExpense")
            Log.d("SummaryActivity", "Balance: $balance")

        } catch (e: Exception) {
            Log.e("SummaryActivity", "Error loading summary: ${e.message}")
            lblTotalIncome.text = getString(R.string.error_loading_summary)
            lblTotalExpense.text = getString(R.string.error_loading_summary)
            lblBalance.text = getString(R.string.error_loading_summary)
        }
    }

    override fun onResume() {
        super.onResume()
        updateSummary()
    }
}
