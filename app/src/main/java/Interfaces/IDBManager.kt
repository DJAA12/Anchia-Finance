package Interfaces

import Entities.FinanceCategory
import Entities.FinanceTransaction

interface IDBManager {

    fun addTransaction(transaction: FinanceTransaction)
    fun updateTransaction(transaction: FinanceTransaction)
    fun removeTransaction(id: Int)
    fun getTransactions(): List<FinanceTransaction>
    fun getTransactionById(id: Int): FinanceTransaction?

    fun addCategory(category: FinanceCategory)
    fun updateCategory(category: FinanceCategory)
    fun removeCategory(id: Int)
    fun getCategories(): List<FinanceCategory>
    fun getCategoryById(id: Int): FinanceCategory?

    fun setInitialBudget(amount: Double)
    fun getBudget(): Double
}
