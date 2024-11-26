package com.example.sqlite

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlite.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {

    private val binding: ActivityAddBinding by lazy {
        ActivityAddBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
val mydb = MyHelper(this)
        binding.saveButton.setOnClickListener {
            val title = binding.bookTitle.text.toString()
            val author = binding.bookAuthor.text.toString()
            val pages = binding.pageNumber.text.toString()

            if (title.isNotEmpty() && author.isNotEmpty() && pages.isNotEmpty()) {
                val isInserted = mydb.addBook(title, author, pages)
                if (isInserted) {
                    Toast.makeText(this, "Book added successfully!", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK) // Notify MainActivity
                    finish() // Close AddActivity
                } else {
                    Toast.makeText(this, "Failed to add book!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
