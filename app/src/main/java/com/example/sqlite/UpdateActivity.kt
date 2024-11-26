package com.example.sqlite

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlite.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {
    private val binding: ActivityUpdateBinding by lazy {
        ActivityUpdateBinding.inflate(layoutInflater)
    }
    private lateinit var mydb: MyHelper
    private var bookId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize the database helper
        mydb = MyHelper(this)

        // Retrieve and populate fields with data
        getIntentData()

        // Set click listener for the update button

        binding.updateButton.setOnClickListener {
            val updatedTitle = binding.updateBookTitle.text.toString()
            val updatedAuthor = binding.updateBookAuthor.text.toString()
            val updatedPage = binding.updatePageNumber.text.toString()

            if (bookId != null && updatedTitle.isNotEmpty() && updatedAuthor.isNotEmpty() && updatedPage.isNotEmpty()) {
                val isUpdated = mydb.updateData(bookId!!, updatedTitle, updatedAuthor, updatedPage)
                if (isUpdated) {
                    Toast.makeText(this, "Book updated successfully!", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK) // Notify MainActivity of successful update
                    finish()
                } else {
                    Toast.makeText(this, "Failed to update book!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.deleteButton.setOnClickListener {
            bookId?.let { id ->
                AlertDialog.Builder(this).apply {
                    setTitle("Delete Confirmation")
                    setMessage("Are you sure you want to delete this book?")
                    setPositiveButton("Yes") { _, _ ->
                        val isDeleted = mydb.deleteData(id)
                        if (isDeleted) {
                            Toast.makeText(this@UpdateActivity, "Book deleted successfully!", Toast.LENGTH_SHORT).show()
                            // Set RESULT_OK to notify MainActivity
                            setResult(RESULT_OK)
                            finish() // Close the activity
                        } else {
                            Toast.makeText(this@UpdateActivity, "Failed to delete book!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                }.create().show()
            } ?: run {
                Toast.makeText(this, "Book ID is null!", Toast.LENGTH_SHORT).show()
            }
        }



    }

    private fun getIntentData() {
        // Get data passed via Intent
        bookId = intent.getStringExtra("BOOK_ID")
        val title = intent.getStringExtra("BOOK_TITLE")
        val author = intent.getStringExtra("AUTHOR_NAME")
        val pages = intent.getStringExtra("PAGE_NUMBER")

        // Populate the fields
        binding.updateBookTitle.setText(title)
        binding.updateBookAuthor.setText(author)
        binding.updatePageNumber.setText(pages)
    }


}
