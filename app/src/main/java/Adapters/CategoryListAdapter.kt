package Adapters

import Entities.FinanceCategory
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.anchiafinance.R

class CategoryListAdapter(
    private val context: Context,
    private val resource: Int,
    private val datasource: List<FinanceCategory>
) : ArrayAdapter<FinanceCategory>(context, resource, datasource) {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return datasource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = convertView ?: inflater.inflate(resource, parent, false)

        val lblCategoryName = rowView.findViewById<TextView>(R.id.lblCategoryName)
        val imgCategoryPhoto = rowView.findViewById<ImageView>(R.id.imgCategoryPhoto)
        val category = datasource[position]

        lblCategoryName.text = category.name
        if (category.photo != null) {
            imgCategoryPhoto.setImageBitmap(category.photo)
        } else {
            imgCategoryPhoto.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        return rowView
    }

}
