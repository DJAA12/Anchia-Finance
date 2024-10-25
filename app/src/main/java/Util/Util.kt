package Util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

const val EXTRA_TRANSACTION_ID = "com.example.anchiafinance.transactionId"

class Util {
    companion object {
        fun openActivity(context: Context, objClass: Class<*>, keyName: String = "", value: String? = "") {
            val intent = Intent(context, objClass).apply { putExtra(keyName, value) }
            startActivity(context, intent, null)
        }

        fun formatCurrency(amount: Double): String {
            return "$ ${"%,.2f".format(amount)}"
        }
    }
}
