package com.example.unsripay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class Topup : AppCompatActivity() { // Ganti dengan nama Activity Anda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topup) // Sesuaikan dengan nama layout Anda

        // Fungsi untuk tombol kembali
        val backButton = findViewById<Button>(R.id.backbutton)
        backButton.setOnClickListener {
            finish() // Menutup activity ini
        }

        // Temukan tombol Indomaret berdasarkan ID
        val btnIndomaret: LinearLayout = findViewById(R.id.btn_indomaret)

        // Set klik listener untuk membuka TopUpActivity
        btnIndomaret.setOnClickListener {
            val intent = Intent(this, TopUpActivity::class.java)
            startActivity(intent)
        }
    }
}
