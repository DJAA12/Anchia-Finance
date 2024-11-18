package com.example.anchiafinance

import Entities.FinanceCategory
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import Model.FinanceModel
import java.io.File

class CategoryDetailActivity : AppCompatActivity() {
    private lateinit var lblCategoryId: TextView
    private lateinit var txtCategoryName: EditText
    private lateinit var imgCategoryPhoto: ImageView
    private lateinit var btnEditPhoto: Button
    private lateinit var btnEdit: Button

    private val financeModel = FinanceModel(this)
    private var categoryId: Int = 0
    private var categoryPhoto: Bitmap? = null
    private var cameraPhotoUri: Uri? = null

    companion object {
        const val REQUEST_CAMERA = 1
        const val REQUEST_GALLERY = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_detail)

        lblCategoryId = findViewById(R.id.lblCategoryId)
        txtCategoryName = findViewById(R.id.txtCategoryName)
        imgCategoryPhoto = findViewById(R.id.imgCategoryPhoto)
        btnEditPhoto = findViewById(R.id.btnEditPhoto)
        btnEdit = findViewById(R.id.btnEdit)

        categoryId = intent.getIntExtra("CATEGORY_ID", 0)
        val category = financeModel.getCategoryById(categoryId)

        if (category != null) {
            loadCategoryDetails(category)
        } else {
            Toast.makeText(this, getString(R.string.category_not_found), Toast.LENGTH_SHORT).show()
            finish()
        }

        btnEditPhoto.setOnClickListener {
            showPhotoOptions()
        }

        btnEdit.setOnClickListener {
            saveCategoryChanges()
        }
    }

    private fun loadCategoryDetails(category: FinanceCategory) {
        lblCategoryId.text = getString(R.string.category_id_placeholder, category.id)
        txtCategoryName.setText(category.name)
        categoryPhoto = category.photo

        if (categoryPhoto != null) {
            imgCategoryPhoto.setImageBitmap(categoryPhoto)
        } else {
            imgCategoryPhoto.setImageResource(android.R.drawable.ic_menu_report_image)
        }
    }

    private fun showPhotoOptions() {
        val options = arrayOf(
            getString(R.string.take_photo),
            getString(R.string.choose_gallery)
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.edit_photo))
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val photoFile = createImageFile()
        if (photoFile != null) {
            cameraPhotoUri = FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.fileprovider",
                photoFile
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri)
            }
            startActivityForResult(intent, REQUEST_CAMERA)
        } else {
            Toast.makeText(this, getString(R.string.error_camera_file), Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(): File? {
        return try {
            val storageDir = cacheDir
            File.createTempFile("photo_", ".jpg", storageDir)
        } catch (ex: Exception) {
            null
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    cameraPhotoUri?.let { uri ->
                        val photo = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        categoryPhoto = photo
                        imgCategoryPhoto.setImageBitmap(photo)
                    }
                }
                REQUEST_GALLERY -> {
                    val uri = data?.data
                    val photo = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    categoryPhoto = photo
                    imgCategoryPhoto.setImageBitmap(photo)
                }
            }
        }
    }

    private fun saveCategoryChanges() {
        val updatedName = txtCategoryName.text.toString().trim()

        if (updatedName.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_name_required), Toast.LENGTH_SHORT).show()
            return
        }
        if (updatedName.length < 3) {
            Toast.makeText(this, getString(R.string.error_name_length), Toast.LENGTH_SHORT).show()
            return
        }

        val updatedCategory = FinanceCategory(
            id = categoryId,
            name = updatedName,
            photo = categoryPhoto
        )

        try {
            financeModel.updateCategory(updatedCategory)
            Toast.makeText(this, getString(R.string.category_updated), Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_update_category), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                saveCategoryChanges()
                true
            }
            R.id.mnu_delete -> {
                confirmDeleteCategory()
                true
            }
            R.id.mnu_cancel -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmDeleteCategory() {
        try {
            financeModel.removeCategory(categoryId)
            Toast.makeText(this, getString(R.string.category_deleted), Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_delete_category), Toast.LENGTH_SHORT).show()
        }
    }
}
