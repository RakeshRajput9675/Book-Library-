package com.example.sqlite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlite.databinding.RawItemBinding

class CustomAdapter(
    private val context: Context,
    private val bookId: List<String>,
    private val bookTitles: List<String>,
    private val authorNames: List<String>,
    private val pageNumbers: List<String>,
    private val activity:Activity
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // Define ViewHolder class with binding
    class ViewHolder(private val binding: RawItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            bookId: String,
            bookTitle: String,
            authorName: String,
            pageNumber: String,
            onItemClicked: (String) -> Unit
        ) {
            binding.bookId.text = bookId
            binding.bookTitle.text = bookTitle
            binding.bookAuthor.text = authorName
            binding.pageNumber.text = pageNumber

            // Set click listener on mainLayout
            binding.mainLayout.setOnClickListener {
                onItemClicked(bookId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate item layout using binding
        val binding = RawItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            bookId[position],
            bookTitles[position],
            authorNames[position],
            pageNumbers[position]
        ) {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("BOOK_ID", bookId[position])
            intent.putExtra("BOOK_TITLE", bookTitles[position])
            intent.putExtra("AUTHOR_NAME", authorNames[position])
            intent.putExtra("PAGE_NUMBER", pageNumbers[position])

            if (context is MainActivity) {
                // Use startActivityForResult to trigger onActivityResult
                context.startActivityForResult(intent, 1)
            }
        }
    }



    override fun getItemCount(): Int {
        // Return the size of the list
        return bookTitles.size
    }
}
