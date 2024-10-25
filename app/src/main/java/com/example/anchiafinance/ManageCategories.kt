package com.example.anchiafinance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ManageCategories : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manage_categories)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnllamarAddNewCategory: Button = findViewById<Button>(R.id.btncreatecategories)
        btnllamarAddNewCategory.setOnClickListener(View.OnClickListener { view ->
            val intentAddNewCategory = Intent(this, AddNewCategory::class.java)
            startActivity(intentAddNewCategory)
        })

        val btnllamarCategoryList: Button = findViewById<Button>(R.id.btnviewcategories)
        btnllamarCategoryList.setOnClickListener(View.OnClickListener { view ->
            val intentCategoryList = Intent(this, CategoryList::class.java)
            startActivity(intentCategoryList)
        })
    }
}