package com.example.uts

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list)

        val listView: ListView = findViewById(R.id.myListView)

        val carNames = arrayOf("Toyota", "Honda", "Ford", "Chevrolet", "BMW", "Mercedes", "Audi", "Volkswagen", "Nissan", "Hyundai")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, carNames)
        listView.adapter = adapter


    }
}