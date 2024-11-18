package Data

import Entities.FinanceCategory
import Entities.FinanceTransaction
import Interfaces.IDBManager

object MemoryManager : IDBManager {
    private val categories = mutableListOf<FinanceCategory>()
    private val transactions = mutableListOf<FinanceTransaction>()
    private var initialBudget: Double = 0.0

    override fun addCategory(category: FinanceCategory) {
        categories.add(category)
    }

    override fun getCategories(): List<FinanceCategory> {
        return categories.toList()
    }

    override fun getCategoryById(id: Int): FinanceCategory? {
        return categories.find { it.id == id }
    }

    override fun updateCategory(category: FinanceCategory) {
        val index = categories.indexOfFirst { it.id == category.id }
        if (index != -1) {
            categories[index] = category
        }
    }

    override fun removeCategory(id: Int) {
        categories.removeIf { it.id == id }
    }

    override fun addTransaction(transaction: FinanceTransaction) {
        transactions.add(transaction)
    }

    override fun getTransactions(): List<FinanceTransaction> {
        return transactions.toList()
    }

    override fun getTransactionById(id: Int): FinanceTransaction? {
        return transactions.find { it.id == id }
    }

    override fun updateTransaction(transaction: FinanceTransaction) {
        val index = transactions.indexOfFirst { it.id == transaction.id }
        if (index != -1) {
            transactions[index] = transaction
        }
    }

    override fun removeTransaction(id: Int) {
        transactions.removeIf { it.id == id }
    }

    override fun setInitialBudget(amount: Double) {
        initialBudget = amount
    }

    override fun getBudget(): Double {
        return initialBudget
    }
}
