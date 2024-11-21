package com.example.anchiafinance

import Adapters.CategoryListAdapter
import Entities.FinanceCategory
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import Model.FinanceModel

class CategoryListActivity : AppCompatActivity() {
    private lateinit var lstCategories: ListView
    private val financeModel = FinanceModel(this)
    private lateinit var adapter: CategoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        lstCategories = findViewById(R.id.lstCategories)

        updateCategoryList()

        lstCategories.setOnItemClickListener { _, _, position, _ ->
            val selectedCategory = financeModel.getCategories()[position]
            openCategoryDetails(selectedCategory)
        }
    }

    private fun updateCategoryList() {
        try {
            val categories = financeModel.getCategories()
            if (categories.isNotEmpty()) {
                adapter = CategoryListAdapter(this, R.layout.custom_category_item, categories)
                lstCategories.adapter = adapter
            } else {
                Toast.makeText(this, getString(R.string.no_categories_available), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_fetch_categories), Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCategoryDetails(category: FinanceCategory) {
        val intent = Intent(this, CategoryDetailActivity::class.java)
        intent.putExtra("CATEGORY_ID", category.id)
        startActivity(intent)
    }
}
