package com.example.anchiafinance

import Model.FinanceModel
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FinanceMainActivity : AppCompatActivity() {
    private lateinit var lblBalance: TextView
    private lateinit var btnManageCategories: Button
    private lateinit var btnAddTransaction: Button
    private lateinit var btnViewSummary: Button
    private lateinit var btnViewCategoryList: Button
    private lateinit var btnViewTransactionList: Button
    private val financeModel = FinanceModel(this)
    private var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finance_main)

        lblBalance = findViewById(R.id.lblBalance)
        btnManageCategories = findViewById(R.id.btnManageCategories)
        btnAddTransaction = findViewById(R.id.btnAddTransaction)
        btnViewSummary = findViewById(R.id.btnViewSummary)
        btnViewCategoryList = findViewById(R.id.btnViewCategoryList)
        btnViewTransactionList = findViewById(R.id.btnViewTransactionList)

        if (isFirstTime) {
            checkAndRequestBudget()
        } else {
            updateBalance()
        }

        btnManageCategories.setOnClickListener {
            startActivitySafe(Intent(this, CategoryActivity::class.java))
        }

        btnAddTransaction.setOnClickListener {
            startActivityForResult(Intent(this, TransactionActivity::class.java), REQUEST_TRANSACTION)
        }

        btnViewSummary.setOnClickListener {
            startActivitySafe(Intent(this, SummaryActivity::class.java))
        }

        btnViewCategoryList.setOnClickListener {
            startActivitySafe(Intent(this, CategoryListActivity::class.java))
        }

        btnViewTransactionList.setOnClickListener {
            startActivitySafe(Intent(this, TransactionListActivity::class.java))
        }
    }

    private fun checkAndRequestBudget() {
        val budget = financeModel.getBudget() ?: 0.0
        if (budget <= 0.0) {
            Toast.makeText(this, getString(R.string.no_balance_warning), Toast.LENGTH_SHORT).show()
            startActivityForResult(Intent(this, SetBudgetActivity::class.java), REQUEST_BUDGET)
        } else {
            isFirstTime = false
            updateBalance()
        }
    }

    private fun updateBalance() {
        try {
            val totalBalance = calculateTotalBalance()
            lblBalance.text = getString(R.string.available_balance2, totalBalance)
        } catch (e: Exception) {
            val errorMessage = getString(R.string.error_loading_balance2, e.message ?: "Unknown error")
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateTotalBalance(): Double {
        val budget = financeModel.getBudget() ?: 0.0
        val transactions = financeModel.getTransactions()

        val incomeType = getString(R.string.transaction_type_income)
        val expenseType = getString(R.string.transaction_type_expense)

        val totalIncome = transactions.filter { it.type == incomeType }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type == expenseType }.sumOf { it.amount }
        return budget + totalIncome - totalExpense
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_BUDGET -> {
                isFirstTime = false
                updateBalance()
            }
            REQUEST_TRANSACTION -> {
                updateBalance()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_category -> {
                startActivitySafe(Intent(this, CategoryActivity::class.java))
                true
            }
            R.id.mnu_transactions -> {
                startActivityForResult(Intent(this, TransactionActivity::class.java), REQUEST_TRANSACTION)
                true
            }
            R.id.mnu_summary -> {
                startActivitySafe(Intent(this, SummaryActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startActivitySafe(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: Exception) {
            val errorMessage = getString(R.string.error_navigation2, e.message ?: "Unknown error")
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        updateBalance()
    }

    companion object {
        private const val REQUEST_BUDGET = 101
        private const val REQUEST_TRANSACTION = 102
    }
}
