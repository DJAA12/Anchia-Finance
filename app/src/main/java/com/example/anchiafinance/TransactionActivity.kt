package com.example.anchiafinance

import Entities.FinanceCategory
import Entities.FinanceTransaction
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
import java.io.IOException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class TransactionActivity : AppCompatActivity() {
    private lateinit var lblAvailableBalance: TextView
    private lateinit var txtAmount: EditText
    private lateinit var txtDescription: EditText
    private lateinit var spnType: Spinner
    private lateinit var spnCategory: Spinner
    private lateinit var btnSaveTransaction: Button
    private lateinit var imgTransactionPhoto: ImageView
    private lateinit var btnAddPhoto: Button
    private val financeModel = FinanceModel(this)
    private var transactionPhoto: Bitmap? = null
    private var cameraPhotoUri: Uri? = null

    companion object {
        const val REQUEST_CAMERA = 1
        const val REQUEST_GALLERY = 2
        const val REQUEST_CAMERA_PERMISSION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        lblAvailableBalance = findViewById(R.id.lblAvailableBalance)
        txtAmount = findViewById(R.id.txtAmount)
        txtDescription = findViewById(R.id.txtDescription)
        spnType = findViewById(R.id.spnType)
        spnCategory = findViewById(R.id.spnCategory)
        btnSaveTransaction = findViewById(R.id.btnSaveTransaction)
        imgTransactionPhoto = findViewById(R.id.imgTransactionPhoto)
        btnAddPhoto = findViewById(R.id.btnAddPhoto)

        updateAvailableBalance()
        setupSpinners()

        btnAddPhoto.setOnClickListener {
            showPhotoOptions()
        }

        btnSaveTransaction.setOnClickListener {
            saveTransaction()
        }
    }

    private fun updateAvailableBalance() {
        try {

            val transactions = financeModel.getTransactions()


            val totalIncome = transactions.filter { it.type == getString(R.string.transaction_type_income) }
                .sumOf { it.amount }


            val totalExpense = transactions.filter { it.type == getString(R.string.transaction_type_expense) }
                .sumOf { it.amount }


            val budget = financeModel.getBudget() ?: 0.0


            val balance = budget + totalIncome - totalExpense
            lblAvailableBalance.text = String.format(getString(R.string.available_balance2), balance)

        } catch (e: Exception) {
            lblAvailableBalance.text = getString(R.string.error_loading_balance)
        }
    }

    private fun setupSpinners() {
        val transactionTypes = resources.getStringArray(R.array.transaction_types)
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, transactionTypes)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnType.adapter = typeAdapter

        try {
            val categories = financeModel.getCategories().map { it.name }
            val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spnCategory.adapter = categoryAdapter
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_loading_categories), Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveTransaction() {
        val type = spnType.selectedItem?.toString() ?: ""
        val category = spnCategory.selectedItem?.toString() ?: ""
        val amountString = txtAmount.text.toString()
        val description = txtDescription.text.toString()

        if (type.isEmpty()) {
            Toast.makeText(this, getString(R.string.select_transaction_type), Toast.LENGTH_SHORT).show()
            return
        }

        if (category.isEmpty()) {
            Toast.makeText(this, getString(R.string.select_transaction_category), Toast.LENGTH_SHORT).show()
            return
        }

        if (amountString.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_valid_amount), Toast.LENGTH_SHORT).show()
            return
        }


        val decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault())
        decimalFormatSymbols.groupingSeparator = ','
        decimalFormatSymbols.decimalSeparator = '.'

        val decimalFormat = DecimalFormat("#,##0.00", decimalFormatSymbols)

        val amount: Double
        try {
            amount = decimalFormat.parse(amountString)?.toDouble() ?: 0.0
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_parsing_amount), Toast.LENGTH_SHORT).show()
            return
        }

        if (amount <= 0) {
            Toast.makeText(this, getString(R.string.enter_valid_amount), Toast.LENGTH_SHORT).show()
            return
        }

        val transaction = FinanceTransaction(
            id = 0,
            type = type,
            category = category,
            amount = amount,
            description = description,
            date = Date(),
            photo = transactionPhoto
        )

        try {
            financeModel.addTransaction(transaction)
            Toast.makeText(this, getString(R.string.transaction_saved), Toast.LENGTH_SHORT).show()
            clearFields()
            updateAvailableBalance()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_saving_transaction), Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        txtAmount.text.clear()
        txtDescription.text.clear()
        imgTransactionPhoto.setImageResource(android.R.color.transparent)
        transactionPhoto = null
    }

    private fun showPhotoOptions() {
        val options = arrayOf(getString(R.string.take_photo), getString(R.string.choose_gallery))
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            openCamera()
        }
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
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val storageDir = cacheDir
            File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        } catch (e: IOException) {
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
                startActivity(Intent(this, CategoryActivity::class.java))
                true
            }
            R.id.mnu_transactions -> {
                startActivity(Intent(this, TransactionListActivity::class.java))
                true
            }
            R.id.mnu_summary -> {
                startActivity(Intent(this, SummaryActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
