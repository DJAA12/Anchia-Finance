package Interfaces

import Entities.Budget
import Entities.Category
import Entities.Transaction

interface IDBManager {
    fun addTransaction(transaction: Transaction)
    fun updateTransaction(transaction: Transaction)
    fun removeTransaction(id: String)
    fun getAllTransactions(): List<Transaction>
    fun getTransactionById(id: String): Transaction?
    fun addCategory(category: Category)
    fun getAllCategories(): List<Category>
    fun setInitialBudget(amount: Double)
    fun getBudget(): Budget
}
