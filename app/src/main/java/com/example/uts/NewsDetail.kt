package com.example.uts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class NewsDetail : AppCompatActivity() {

    lateinit var newsTitle: TextView
    lateinit var newsSubtitle: TextView
    lateinit var newsImage: ImageView
    lateinit var edit: Button
    lateinit var hapus: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

// Initialize UI components
        newsTitle = findViewById(R.id.newsTitle)
        newsSubtitle = findViewById(R.id.newsSubtitle)
        newsImage = findViewById(R.id.newsImage)
        edit = findViewById(R.id.editButton)
        hapus = findViewById(R.id.deleteButton)
        db = FirebaseFirestore.getInstance()

// Get data from Intent
        val intent = intent
        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val subtitle = intent.getStringExtra("desc")
        val imageUrl = intent.getStringExtra("imageUrl")
        // Set data to UI components
        newsTitle.text = title
        newsSubtitle.text = subtitle
        Glide.with(this).load(imageUrl).into(newsImage)

        edit.setOnClickListener {
            val editIntent = Intent(this@NewsDetail, NewsAdd::class.java).apply {
                putExtra("id", id)
                putExtra("title", title)
                putExtra("desc", subtitle)
                putExtra("imageUrl", imageUrl)
            }
            startActivity(editIntent)
        }

        hapus.setOnClickListener {
            id?.let { documentId ->
                db.collection("news").document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@NewsDetail, "News deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        val mainIntent = Intent(this@NewsDetail, Dashboard::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        startActivity(mainIntent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this@NewsDetail, "Error deleting news: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.w( "NewsDetail", "Error deleting document", e)
                    }
                // Close the activity and go back to the previous screen
            }
        }

    }

}

