package com.example.anchiafinance

import Entities.FinanceCategory
import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import Model.FinanceModel
import android.view.Menu
import android.view.MenuItem
import java.io.File

class CategoryActivity : AppCompatActivity() {
    private lateinit var txtCategoryName: EditText
    private lateinit var btnSaveCategory: Button
    private lateinit var btnAddPhoto: Button
    private lateinit var imgCategoryPhoto: ImageView
    private val financeModel = FinanceModel(this)
    private var categoryPhoto: Bitmap? = null
    private var cameraPhotoUri: Uri? = null

    companion object {
        const val REQUEST_CAMERA = 1
        const val REQUEST_GALLERY = 2
        const val REQUEST_CAMERA_PERMISSION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        txtCategoryName = findViewById(R.id.txtCategoryName)
        btnSaveCategory = findViewById(R.id.btnSaveCategory)
        btnAddPhoto = findViewById(R.id.btnAddPhoto)
        imgCategoryPhoto = findViewById(R.id.imgCategoryPhoto)

        btnAddPhoto.setOnClickListener {
            showPhotoOptions()
        }

        btnSaveCategory.setOnClickListener {
            saveCategory()
        }
    }

    private fun showPhotoOptions() {
        val options = arrayOf(
            getString(R.string.take_photo),
            getString(R.string.choose_gallery)
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.select_photo))
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> checkCameraPermissionAndOpenCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                showPermissionExplanationDialog()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        } else {
            openCamera()
        }
    }

    private fun showPermissionExplanationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.camera_permission_title))
        builder.setMessage(getString(R.string.camera_permission_message))
        builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
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

    private fun saveCategory() {
        val name = txtCategoryName.text.toString().trim()

        if (name.isEmpty()) {
            txtCategoryName.error = getString(R.string.error_name_empty)
            Toast.makeText(this, getString(R.string.error_name_required), Toast.LENGTH_SHORT).show()
            return
        }
        if (name.length < 3) {
            txtCategoryName.error = getString(R.string.error_name_length)
            Toast.makeText(this, getString(R.string.error_name_too_short), Toast.LENGTH_SHORT).show()
            return
        }

        if (categoryPhoto == null) {
            Toast.makeText(this, getString(R.string.error_photo_required), Toast.LENGTH_SHORT).show()
            return
        }

        val category = FinanceCategory(0, name, categoryPhoto)
        financeModel.addCategory(category)

        txtCategoryName.text.clear()
        imgCategoryPhoto.setImageResource(android.R.color.transparent)
        categoryPhoto = null
        Toast.makeText(this, getString(R.string.category_saved), Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_category -> {
                startActivity(Intent(this, CategoryListActivity::class.java))
                true
            }
            R.id.mnu_transactions -> {
                startActivity(Intent(this, TransactionActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
