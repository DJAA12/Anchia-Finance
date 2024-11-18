package com.example.anchiafinance

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import Model.FinanceModel
import com.example.anchiafinance.SetBudgetActivity.Companion.budget

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
        val transactions = financeModel.getTransactions()

        if (transactions.isEmpty()) {
            lblTotalIncome.text = getString(R.string.no_transactions)
            lblTotalExpense.text = getString(R.string.no_transactions)
            lblBalance.text = getString(R.string.no_transactions)
            return
        }

        val incomeType = getString(R.string.transaction_type_income)
        val expenseType = getString(R.string.transaction_type_expense)

        val totalIncome = budget + transactions.filter { it.type == incomeType }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type == expenseType }.sumOf { it.amount }
        val balance = totalIncome - totalExpense

        lblTotalIncome.text = getString(R.string.total_income2, totalIncome)
        lblTotalExpense.text = getString(R.string.total_expense2, totalExpense)
        lblBalance.text = getString(R.string.current_balance2, balance)
    }
}
