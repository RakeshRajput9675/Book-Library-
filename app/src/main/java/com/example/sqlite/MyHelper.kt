package com.example.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "my_database.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "book_Library" // Table name
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = """
            CREATE TABLE $TABLE_NAME (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                book_title TEXT,
                author_name TEXT,
                page_number INTEGER
            )
        """
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Add a new book
    fun addBook(title: String, author: String, page: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("book_title", title)
            put("author_name", author)
            put("page_number", page)
        }
        val result: Long = db.insert(TABLE_NAME, null, contentValues)
        if (result == -1L) {
            Toast.makeText(context, "Failed to add book", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Book added successfully", Toast.LENGTH_SHORT).show()
        }
        db.close()
        return result != -1L
    }

    // Read all books
    fun readAllData(): Cursor {
        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        return db.rawQuery(query, null)
    }

    // Update an existing book
    fun updateData(id: String, title: String, author: String, pages: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("book_title", title)
            put("author_name", author)
            put("page_number", pages)
        }
        val result = db.update(TABLE_NAME, contentValues, "id=?", arrayOf(id))
        db.close()
        return result > 0 // True if update succeeded
    }

    // Delete a specific book
    fun deleteData(id: String): Boolean {
        val db = this.writableDatabase

        // Delete the specific row
        val result: Int = db.delete(TABLE_NAME, "id=?", arrayOf(id))
        if (result > 0) {
            Toast.makeText(context, "Book deleted successfully", Toast.LENGTH_SHORT).show()

            // Reset IDs sequentially
            db.execSQL("UPDATE $TABLE_NAME SET id = (SELECT COUNT(*) FROM $TABLE_NAME t2 WHERE t2.rowid <= $TABLE_NAME.rowid)")
            db.execSQL("UPDATE sqlite_sequence SET seq = (SELECT MAX(id) FROM $TABLE_NAME) WHERE name = '$TABLE_NAME'")
        } else {
            Toast.makeText(context, "Failed to delete book", Toast.LENGTH_SHORT).show()
        }

        db.close()
        return result > 0
    }


    // Delete all books and reset ID sequence
    fun deleteAllData() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME") // Clear all rows
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = '$TABLE_NAME'") // Reset sequence
        db.close()
        Toast.makeText(context, "All books deleted successfully", Toast.LENGTH_SHORT).show()
    }
}
