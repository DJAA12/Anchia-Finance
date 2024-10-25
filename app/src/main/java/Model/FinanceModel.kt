package Model

import Data.MemoryManager
import Entities.Budget
import Entities.Category
import Entities.Transaction
import Interfaces.IDBManager

class FinanceModel {
    private var dbManager: IDBManager = MemoryManager

    fun addTransaction(transaction: Transaction) {
        dbManager.addTransaction(transaction)
    }

    fun getAllTransactions(): List<Transaction> {
        return dbManager.getAllTransactions()
    }

    fun getTransactionById(id: String): Transaction? {
        return dbManager.getTransactionById(id)
    }

    fun addCategory(category: Category) {
        dbManager.addCategory(category)
    }

    fun getAllCategories(): List<Category> {
        return dbManager.getAllCategories()
    }

    fun setInitialBudget(amount: Double) {
        dbManager.setInitialBudget(amount)
    }

    fun getBudget(): Budget {
        return dbManager.getBudget()
    }
}
