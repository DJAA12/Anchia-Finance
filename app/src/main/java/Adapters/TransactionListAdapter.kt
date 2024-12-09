package Adapters

import Entities.FinanceTransaction
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.anchiafinance.R

class TransactionListAdapter(
    context: Context,
    private val resource: Int,
    private val items: List<FinanceTransaction>
) : ArrayAdapter<FinanceTransaction>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(resource, parent, false)

        val lblType = view.findViewById<TextView>(R.id.lblType)
        val lblCategory = view.findViewById<TextView>(R.id.lblCategory)
        val lblAmount = view.findViewById<TextView>(R.id.lblAmount)
        val lblDescription = view.findViewById<TextView>(R.id.lblDescription)
        val imgPhoto = view.findViewById<ImageView>(R.id.imgTransactionPhoto)

        val transaction = items[position]

        lblType.text = transaction.type
        lblCategory.text = transaction.category
        lblAmount.text = "%.2f".format(transaction.amount)
        lblDescription.text = transaction.description

        if (transaction.photo != null) {
            imgPhoto.setImageBitmap(transaction.photo)
        } else {
            imgPhoto.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        return view
    }
}
