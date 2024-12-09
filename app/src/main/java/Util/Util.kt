package Util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

const val EXTRA_MESSAGE_TRANSACTION_ID = "com.example.anchia_finance.transactionId"
const val EXTRA_MESSAGE_CATEGORY_ID = "com.example.anchia_finance.categoryId"
const val EXTRA_MESSAGE_BUDGET = "com.example.anchia_finance.budget"

class util {
    companion object {
        fun openActivity(context: Context, objClass: Class<*>, keyName: String = "", value: String? = "") {
            val intent = Intent(context, objClass).apply {
                if (keyName.isNotEmpty() && value != null) {
                    putExtra(keyName, value)
                }
            }
            startActivity(context, intent, null)
        }
    }
}
