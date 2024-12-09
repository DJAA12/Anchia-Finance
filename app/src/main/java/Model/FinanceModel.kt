package Model

import android.content.Context
import Data.FinanceDatabaseManager
import Entities.FinanceCategory
import Entities.FinanceTransaction
import com.example.anchiafinance.R

class FinanceModel(private val context: Context) {

    private val dbManager = FinanceDatabaseManager(context)
    private var cachedBalance: Double? = null

    private fun updateCachedBalance() {
        cachedBalance = try {
            calculateBalance()
        } catch (e: Exception) {
            null
        }
    }

    fun getBalance(): Double {
        if (cachedBalance == null) {
            updateCachedBalance()
        }
        return cachedBalance ?: 0.0
    }


    fun getBudget(): Double {
        return try {
            dbManager.getBudget() ?: 0.0
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_fetch_budget, e.message))
        }
    }

    fun setBudget(newBudget: Double) {
        try {
            dbManager.setBudget(newBudget)
            updateCachedBalance()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_set_budget))
        }
    }


    fun addCategory(category: FinanceCategory) {
        try {
            dbManager.addCategory(category)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_add_category, e.message))
        }
    }

    fun getCategories(): List<FinanceCategory> {
        return try {
            dbManager.getCategories()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_fetch_categories, e.message))
        }
    }

    fun removeCategory(id: Int) {
        try {
            dbManager.removeCategory(id)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_remove_category, e.message))
        }
    }

    fun getCategoryById(id: Int): FinanceCategory? {
        return try {
            dbManager.getCategoryById(id)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_fetch_category_id, e.message))
        }
    }

    fun updateCategory(category: FinanceCategory) {
        try {
            dbManager.updateCategory(category)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_update_category))
        }
    }

    fun addTransaction(transaction: FinanceTransaction) {
        try {
            dbManager.addTransaction(transaction)
            updateCachedBalance()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_add_transaction, e.message))
        }
    }

    fun getTransactions(): List<FinanceTransaction> {
        return try {
            dbManager.getTransactions()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_fetch_transactions, e.message))
        }
    }

    fun removeTransaction(id: Int) {
        try {
            dbManager.removeTransaction(id)
            updateCachedBalance()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_remove_transaction, e.message))
        }
    }

    fun getTransactionById(id: Int): FinanceTransaction? {
        return try {
            dbManager.getTransactionById(id)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_fetch_transaction_id, e.message))
        }
    }

    fun updateTransaction(transaction: FinanceTransaction) {
        try {
            dbManager.updateTransaction(transaction)
            updateCachedBalance()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_update_transaction, e.message))
        }
    }


    fun calculateBalance(): Double {
        return try {
            val budget = getBudget()
            val transactions = getTransactions()
            val totalIncome = transactions.filter { it.type.equals("Income", ignoreCase = true) }.sumOf { it.amount }
            val totalExpense = transactions.filter { it.type.equals("Expense", ignoreCase = true) }.sumOf { it.amount }

            val balance = budget + totalIncome - totalExpense
            balance
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_calculate_balance, e.message))
        }
    }
}
