package com.example.sqlite

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var mydb: MyHelper
    private lateinit var book_id: MutableList<String>
    private lateinit var book_title: MutableList<String>
    private lateinit var author_name: MutableList<String>
    private lateinit var page_number: MutableList<String>
    private lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mydb = MyHelper(this)
        book_id = mutableListOf()
        book_title = mutableListOf()
        author_name = mutableListOf()
        page_number = mutableListOf()

        // Set ActionBar title
        val ab = supportActionBar
        ab?.title = "Book Library"

        // Fetch and store data
        storeData()

        // Initialize RecyclerView adapter
        adapter = CustomAdapter(this, book_id, book_title, author_name, page_number, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // FloatingActionButton click listener to navigate to AddActivity
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivityForResult(intent, 1) // Pass requestCode = 1
        }
        binding.deleteAll.setOnClickListener {
            showDeleteAllDialog()
        }
    }

    private fun showDeleteAllDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Delete All")
        builder.setMessage("Are you sure you want to delete all data?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            deleteAllData()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun deleteAllData() {
        mydb.deleteAllData()
        storeData()
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "All data deleted", Toast.LENGTH_SHORT).show()

    }

    private fun storeData() {
        val cursor: Cursor = mydb.readAllData()
        if (cursor.count == 0) {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show()
            book_id.clear()
            book_title.clear()
            author_name.clear()
            page_number.clear()
        } else {
            book_id.clear()
            book_title.clear()
            author_name.clear()
            page_number.clear()
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0))
                book_title.add(cursor.getString(1))
                author_name.add(cursor.getString(2))
                page_number.add(cursor.getString(3))
            }
        }
        cursor.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Refresh data after adding or updating
            storeData()
            adapter.notifyDataSetChanged()
        }
    }
}
