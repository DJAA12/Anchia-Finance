package com.example.anchiafinance

import Adapters.TransactionListAdapter
import Entities.FinanceTransaction
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import Model.FinanceModel

class TransactionListActivity : AppCompatActivity() {
    private lateinit var lstTransactions: ListView
    private val financeModel = FinanceModel(this)
    private lateinit var adapter: TransactionListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_list)

        lstTransactions = findViewById(R.id.lstTransactions)

        updateTransactionList()

        lstTransactions.setOnItemClickListener { _, _, position, _ ->
            val transactions = financeModel.getTransactions()
            if (position in transactions.indices) {
                val selectedTransaction = transactions[position]
                openTransactionDetails(selectedTransaction)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.invalid_transaction_selection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateTransactionList() {
        try {
            val transactions = financeModel.getTransactions()
            if (transactions.isNotEmpty()) {
                adapter = TransactionListAdapter(this, R.layout.custom_transaction_item, transactions)
                lstTransactions.adapter = adapter
            } else {
                Toast.makeText(this, getString(R.string.no_transactions_available), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_loading_transactions, e.message), Toast.LENGTH_SHORT).show()
        }
    }

    private fun openTransactionDetails(transaction: FinanceTransaction) {
        val intent = Intent(this, TransactionDetailActivity::class.java)
        intent.putExtra("TRANSACTION_ID", transaction.id)
        startActivity(intent)
    }
}
