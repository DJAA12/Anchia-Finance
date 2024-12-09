package Data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import Entities.FinanceCategory
import Entities.FinanceTransaction
import java.io.ByteArrayOutputStream
import java.util.Date

class FinanceDatabaseManager(context: Context) {
    private val dbHelper = FinanceDatabaseHelper(context)


    private fun bitmapToByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) return null
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        return if (byteArray != null) BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size) else null
    }


    fun addCategory(category: FinanceCategory) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(FinanceDatabaseHelper.COLUMN_CATEGORY_NAME, category.name)
            put(FinanceDatabaseHelper.COLUMN_CATEGORY_PHOTO, bitmapToByteArray(category.photo) ?: ByteArray(0))
        }
        db.insert(FinanceDatabaseHelper.TABLE_CATEGORIES, null, values)
        db.close()
    }

    fun getCategories(): List<FinanceCategory> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            FinanceDatabaseHelper.TABLE_CATEGORIES,
            null, null, null, null, null, null
        )

        val categories = mutableListOf<FinanceCategory>()
        with(cursor) {
            while (moveToNext()) {
                categories.add(
                    FinanceCategory(
                        id = getInt(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_CATEGORY_ID)),
                        name = getString(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_CATEGORY_NAME)),
                        photo = byteArrayToBitmap(getBlob(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_CATEGORY_PHOTO)))
                    )
                )
            }
        }
        cursor.close()
        db.close()
        return categories
    }

    fun getCategoryById(id: Int): FinanceCategory? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            FinanceDatabaseHelper.TABLE_CATEGORIES,
            null,
            "${FinanceDatabaseHelper.COLUMN_CATEGORY_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var category: FinanceCategory? = null
        with(cursor) {
            if (moveToFirst()) {
                category = FinanceCategory(
                    id = getInt(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_CATEGORY_ID)),
                    name = getString(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_CATEGORY_NAME)),
                    photo = byteArrayToBitmap(getBlob(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_CATEGORY_PHOTO)))
                )
            }
        }
        cursor.close()
        db.close()
        return category
    }

    fun updateCategory(category: FinanceCategory) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(FinanceDatabaseHelper.COLUMN_CATEGORY_NAME, category.name)
            put(FinanceDatabaseHelper.COLUMN_CATEGORY_PHOTO, bitmapToByteArray(category.photo) ?: ByteArray(0))
        }
        db.update(
            FinanceDatabaseHelper.TABLE_CATEGORIES,
            values,
            "${FinanceDatabaseHelper.COLUMN_CATEGORY_ID} = ?",
            arrayOf(category.id.toString())
        )
        db.close()
    }

    fun removeCategory(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete(
            FinanceDatabaseHelper.TABLE_CATEGORIES,
            "${FinanceDatabaseHelper.COLUMN_CATEGORY_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
    }


    fun addTransaction(transaction: FinanceTransaction) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(FinanceDatabaseHelper.COLUMN_TRANSACTION_TYPE, transaction.type)
            put(FinanceDatabaseHelper.COLUMN_TRANSACTION_CATEGORY, transaction.category)
            put(FinanceDatabaseHelper.COLUMN_TRANSACTION_AMOUNT, transaction.amount)
            put(FinanceDatabaseHelper.COLUMN_TRANSACTION_DESCRIPTION, transaction.description)
            put(FinanceDatabaseHelper.COLUMN_TRANSACTION_DATE, transaction.date.time)
            put(FinanceDatabaseHelper.COLUMN_TRANSACTION_PHOTO, bitmapToByteArray(transaction.photo) ?: ByteArray(0))
        }
        db.insert(FinanceDatabaseHelper.TABLE_TRANSACTIONS, null, values)
        db.close()
    }

    fun getTransactions(): List<FinanceTransaction> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            FinanceDatabaseHelper.TABLE_TRANSACTIONS,
            null, null, null, null, null, null
        )

        val transactions = mutableListOf<FinanceTransaction>()
        with(cursor) {
            while (moveToNext()) {
                transactions.add(
                    FinanceTransaction(
                        id = getInt(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_ID)),
                        type = getString(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_TYPE)),
                        category = getString(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_CATEGORY)),
                        amount = getDouble(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_AMOUNT)),
                        description = getString(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_DESCRIPTION)),
                        date = Date(getLong(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_DATE))),
                        photo = byteArrayToBitmap(getBlob(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_PHOTO)))
                    )
                )
            }
        }
        cursor.close()
        db.close()
        return transactions

    }

    fun getTransactionById(id: Int): FinanceTransaction? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            FinanceDatabaseHelper.TABLE_TRANSACTIONS,
            null,
            "${FinanceDatabaseHelper.COLUMN_TRANSACTION_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var transaction: FinanceTransaction? = null
        with(cursor) {
            if (moveToFirst()) {
                transaction = FinanceTransaction(
                    id = getInt(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_ID)),
                    type = getString(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_TYPE)),
                    category = getString(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_CATEGORY)),
                    amount = getDouble(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_AMOUNT)),
                    description = getString(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_DESCRIPTION)),
                    date = Date(getLong(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_DATE))),
                    photo = byteArrayToBitmap(getBlob(getColumnIndexOrThrow(FinanceDatabaseHelper.COLUMN_TRANSACTION_PHOTO)))
                )
            }
        }
        cursor.close()
        db.close()
        return transaction
    }

    fun updateTransaction(transaction: FinanceTransaction) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(FinanceDatabaseHelper.COLUMN_TRANSACTION_TYPE, transaction.type)
            put(FinanceDatabaseHelper.COLUMN_TRANSACTION_CATEGORY, transaction.category)
            put(FinanceDatabaseHelper.COLUMN_TRANSACTION_AMOUNT, transaction.amount)
            put(FinanceDatabaseHelper.COLUMN_TRANSACTION_DESCRIPTION, transaction.description)
            put(FinanceDatabaseHelper.COLUMN_TRANSACTION_DATE, transaction.date.time)
            put(FinanceDatabaseHelper.COLUMN_TRANSACTION_PHOTO, bitmapToByteArray(transaction.photo) ?: ByteArray(0))
        }
        db.update(
            FinanceDatabaseHelper.TABLE_TRANSACTIONS,
            values,
            "${FinanceDatabaseHelper.COLUMN_TRANSACTION_ID} = ?",
            arrayOf(transaction.id.toString())
        )
        db.close()
    }

    fun removeTransaction(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete(
            FinanceDatabaseHelper.TABLE_TRANSACTIONS,
            "${FinanceDatabaseHelper.COLUMN_TRANSACTION_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
    }


    fun getBudget(): Double? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT ${FinanceDatabaseHelper.COLUMN_BUDGET_AMOUNT} FROM ${FinanceDatabaseHelper.TABLE_BUDGET} LIMIT 1", null)
        var budget: Double? = null
        if (cursor.moveToFirst()) {
            budget = cursor.getDouble(0)
        }
        cursor.close()
        db.close()
        return budget
    }

    fun setBudget(newBudget: Double) {
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM ${FinanceDatabaseHelper.TABLE_BUDGET}")
        val values = ContentValues().apply {
            put(FinanceDatabaseHelper.COLUMN_BUDGET_AMOUNT, newBudget)
        }
        db.insert(FinanceDatabaseHelper.TABLE_BUDGET, null, values)
        db.close()
    }
}
