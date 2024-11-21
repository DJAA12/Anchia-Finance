package Data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FinanceDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "finance.db"
        private const val DATABASE_VERSION = 1


        const val TABLE_CATEGORIES = "categories"
        const val TABLE_TRANSACTIONS = "transactions"
        const val TABLE_BUDGET = "budget"


        const val COLUMN_CATEGORY_ID = "id"
        const val COLUMN_CATEGORY_NAME = "name"
        const val COLUMN_CATEGORY_PHOTO = "photo"


        const val COLUMN_TRANSACTION_ID = "id"
        const val COLUMN_TRANSACTION_TYPE = "type"
        const val COLUMN_TRANSACTION_CATEGORY = "category"
        const val COLUMN_TRANSACTION_AMOUNT = "amount"
        const val COLUMN_TRANSACTION_DESCRIPTION = "description"
        const val COLUMN_TRANSACTION_DATE = "date"
        const val COLUMN_TRANSACTION_PHOTO = "photo"


        const val COLUMN_BUDGET_ID = "id"
        const val COLUMN_BUDGET_AMOUNT = "amount"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createCategoriesTable = """
            CREATE TABLE $TABLE_CATEGORIES (
                $COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY_NAME TEXT NOT NULL,
                $COLUMN_CATEGORY_PHOTO BLOB
            )
        """


        val createTransactionsTable = """
            CREATE TABLE $TABLE_TRANSACTIONS (
                $COLUMN_TRANSACTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TRANSACTION_TYPE TEXT NOT NULL,
                $COLUMN_TRANSACTION_CATEGORY TEXT NOT NULL,
                $COLUMN_TRANSACTION_AMOUNT REAL NOT NULL,
                $COLUMN_TRANSACTION_DESCRIPTION TEXT,
                $COLUMN_TRANSACTION_DATE INTEGER NOT NULL,
                $COLUMN_TRANSACTION_PHOTO BLOB
            )
        """


        val createBudgetTable = """
            CREATE TABLE $TABLE_BUDGET (
                $COLUMN_BUDGET_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BUDGET_AMOUNT REAL NOT NULL
            )
        """


        db?.execSQL(createCategoriesTable)
        db?.execSQL(createTransactionsTable)
        db?.execSQL(createBudgetTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BUDGET")
        onCreate(db)
    }
}
