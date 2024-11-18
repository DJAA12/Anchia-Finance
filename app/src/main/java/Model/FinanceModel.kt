package Model

import android.content.Context
import Data.MemoryManager
import Entities.FinanceCategory
import Entities.FinanceTransaction
import com.example.anchiafinance.R

class FinanceModel(private val context: Context) {

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


    fun addCategory(category: FinanceCategory) {
        try {
            MemoryManager.addCategory(category)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_add_category, e.message))
        }
    }

    fun getCategories(): List<FinanceCategory> {
        return try {
            MemoryManager.getCategories()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_fetch_categories, e.message))
        }
    }

    fun removeCategory(id: Int) {
        try {
            MemoryManager.removeCategory(id)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_remove_category, e.message))
        }
    }

    fun updateCategory(category: FinanceCategory) {
        try {
            MemoryManager.updateCategory(category)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_update_category2, e.message))
        }
    }

    fun getCategoryById(id: Int): FinanceCategory? {
        return try {
            MemoryManager.getCategoryById(id)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_fetch_category_id, e.message))
        }
    }


    fun addTransaction(transaction: FinanceTransaction) {
        try {
            MemoryManager.addTransaction(transaction)
            updateCachedBalance()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_add_transaction, e.message))
        }
    }

    fun getTransactions(): List<FinanceTransaction> {
        return try {
            MemoryManager.getTransactions()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_fetch_transactions, e.message))
        }
    }

    fun removeTransaction(id: Int) {
        try {
            MemoryManager.removeTransaction(id)
            updateCachedBalance()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_remove_transaction, e.message))
        }
    }

    fun updateTransaction(transaction: FinanceTransaction) {
        try {
            MemoryManager.updateTransaction(transaction)
            updateCachedBalance()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_update_transaction, e.message))
        }
    }

    fun getTransactionById(id: Int): FinanceTransaction? {
        return try {
            MemoryManager.getTransactionById(id)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_fetch_transaction_id, e.message))
        }
    }

    fun calculateBalance(): Double {
        return try {
            val transactions = getTransactions()
            transactions.sumOf {
                if (it.type.equals("Income", ignoreCase = true)) it.amount else -it.amount
            }
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_calculate_balance, e.message))
        }
    }
}
