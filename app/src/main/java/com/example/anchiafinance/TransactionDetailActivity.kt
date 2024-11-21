package com.example.anchiafinance

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
import Entities.FinanceTransaction
import Model.FinanceModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TransactionDetailActivity : AppCompatActivity() {
    private lateinit var lblTransactionId: TextView
    private lateinit var spnTransactionType: Spinner
    private lateinit var spnTransactionCategory: Spinner
    private lateinit var txtTransactionAmount: EditText
    private lateinit var txtTransactionDescription: EditText
    private lateinit var lblTransactionDate: TextView
    private lateinit var imgTransactionPhoto: ImageView
    private lateinit var btnEditPhoto: Button

    private val financeModel = FinanceModel(this)
    private var transactionId: Int = 0
    private var transactionPhoto: Bitmap? = null
    private var cameraPhotoUri: Uri? = null

    companion object {
        const val REQUEST_CAMERA = 1
        const val REQUEST_GALLERY = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)

        lblTransactionId = findViewById(R.id.lblTransactionId)
        spnTransactionType = findViewById(R.id.spnTransactionType)
        spnTransactionCategory = findViewById(R.id.spnTransactionCategory)
        txtTransactionAmount = findViewById(R.id.txtTransactionAmount)
        txtTransactionDescription = findViewById(R.id.txtTransactionDescription)
        lblTransactionDate = findViewById(R.id.lblTransactionDate)
        imgTransactionPhoto = findViewById(R.id.imgTransactionPhoto)
        btnEditPhoto = findViewById(R.id.btnEditPhoto)

        val btnEdit: Button = findViewById(R.id.btnEdit)

        transactionId = intent.getIntExtra("TRANSACTION_ID", 0)
        val transaction = financeModel.getTransactionById(transactionId)

        if (transaction != null) {
            loadTransactionDetails(transaction)
        } else {
            Toast.makeText(this, getString(R.string.transaction_not_found), Toast.LENGTH_SHORT).show()
            finish()
        }

        btnEditPhoto.setOnClickListener {
            showPhotoOptions()
        }

        btnEdit.setOnClickListener {
            saveTransactionChanges()
        }
    }

    private fun loadTransactionDetails(transaction: FinanceTransaction) {
        lblTransactionId.text = getString(R.string.transaction_id_placeholder, transaction.id)
        txtTransactionAmount.setText(transaction.amount.toString())
        txtTransactionDescription.setText(transaction.description)

        val dateFormatted = SimpleDateFormat(getString(R.string.date_format), Locale.getDefault())
            .format(transaction.date)
        lblTransactionDate.text = getString(R.string.transaction_date, dateFormatted)

        val transactionTypes = listOf(
            getString(R.string.transaction_type_income),
            getString(R.string.transaction_type_expense)
        )
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, transactionTypes)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnTransactionType.adapter = typeAdapter
        spnTransactionType.setSelection(transactionTypes.indexOf(transaction.type))

        val categories = financeModel.getCategories().map { it.name }
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnTransactionCategory.adapter = categoryAdapter
        spnTransactionCategory.setSelection(categories.indexOf(transaction.category))

        transactionPhoto = transaction.photo
        if (transactionPhoto != null) {
            imgTransactionPhoto.setImageBitmap(transactionPhoto)
        } else {
            imgTransactionPhoto.setBackgroundColor(resources.getColor(android.R.color.darker_gray, theme))
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
                        transactionPhoto = photo
                        imgTransactionPhoto.setImageBitmap(photo)
                    }
                }
                REQUEST_GALLERY -> {
                    val uri = data?.data
                    val photo = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    transactionPhoto = photo
                    imgTransactionPhoto.setImageBitmap(photo)
                }
            }
        }
    }

    private fun saveTransactionChanges() {
        val updatedType = spnTransactionType.selectedItem.toString()
        val updatedCategory = spnTransactionCategory.selectedItem.toString()
        val updatedAmount = txtTransactionAmount.text.toString().toDoubleOrNull()
        val updatedDescription = txtTransactionDescription.text.toString()

        if (updatedAmount == null || updatedAmount <= 0) {
            Toast.makeText(this, getString(R.string.invalid_amount), Toast.LENGTH_SHORT).show()
            return
        }

        if (updatedType.isEmpty() || updatedCategory.isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val updatedTransaction = FinanceTransaction(
            id = transactionId,
            type = updatedType,
            category = updatedCategory,
            amount = updatedAmount,
            description = updatedDescription,
            date = Date(),
            photo = transactionPhoto
        )

        try {
            financeModel.updateTransaction(updatedTransaction)
            Toast.makeText(this, getString(R.string.transaction_updated), Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_update_transaction, e.message), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                saveTransactionChanges()
                true
            }
            R.id.mnu_delete -> {
                confirmDeleteTransaction()
                true
            }
            R.id.mnu_cancel -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmDeleteTransaction() {
        try {
            financeModel.removeTransaction(transactionId)
            Toast.makeText(this, getString(R.string.transaction_deleted), Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_delete_transaction, e.message), Toast.LENGTH_SHORT).show()
        }
    }
}
