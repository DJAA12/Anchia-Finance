package Data

import Entities.Budget
import Entities.Category
import Entities.Transaction
import Interfaces.IDBManager

object MemoryManager: IDBManager {
    private var transactionList = mutableListOf<Transaction>()
    private var categoryList = mutableListOf<Category>()
    private var budget: Budget = Budget()

    override fun addTransaction(transaction: Transaction) {
        transactionList.add(transaction)
        updateBudget(transaction)
    }

    override fun updateTransaction(transaction: Transaction) {
        removeTransaction(transaction.id)
        transactionList.add(transaction)
    }

    override fun removeTransaction(id: String) {
        transactionList.removeIf { it.id == id }
    }

    override fun getAllTransactions(): List<Transaction> = transactionList.toList()

    override fun getTransactionById(id: String): Transaction? {
        return transactionList.find { it.id == id }
    }

    override fun addCategory(category: Category) {
        categoryList.add(category)
    }

    override fun getAllCategories(): List<Category> = categoryList.toList()

    override fun setInitialBudget(amount: Double) {
        budget = Budget(amount, amount)
    }

    override fun getBudget(): Budget = budget

    private fun updateBudget(transaction: Transaction) {
        if (transaction.type == "Income") {
            budget.currentAmount += transaction.amount
        } else {
            budget.currentAmount -= transaction.amount
        }
    }
}
