package com.example.unsripay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class confirmyudisium : AppCompatActivity() {

    private lateinit var nimText: TextView
    private lateinit var namaText: TextView
    private lateinit var gelarText: TextView
    private lateinit var tanggalText: TextView
    private lateinit var tempatText: TextView
    private lateinit var nominalText: TextView
    private lateinit var btnKonfirmasi: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirmyudisium)

        // Inisialisasi View
        nimText = findViewById(R.id.nimText)
        namaText = findViewById(R.id.namaText)
        gelarText = findViewById(R.id.gelarText)
        tanggalText = findViewById(R.id.tanggalText)
        tempatText = findViewById(R.id.tempatText)
        nominalText = findViewById(R.id.nominalText)
        btnKonfirmasi = findViewById(R.id.btn_konfirmasi)

        // Ambil data dari Intent
        val nim = intent.getStringExtra("nim") ?: ""
        val nama = intent.getStringExtra("nama") ?: ""
        val gelar = intent.getStringExtra("gelar") ?: ""
        val tanggal = intent.getStringExtra("tanggal") ?: ""
        val tempat = intent.getStringExtra("tempat") ?: ""
        val nominal = intent.getIntExtra("nominal", 0)
        val selectedBank = intent.getStringExtra("selectedBank") ?: "Yudisium"

        // Validasi dan tampilkan data
        if (nim.isNotEmpty() && nama.isNotEmpty() && gelar.isNotEmpty() && tanggal.isNotEmpty() && tempat.isNotEmpty() && nominal > 0) {
            nimText.text = nim
            namaText.text = nama
            gelarText.text = gelar
            tanggalText.text = tanggal
            tempatText.text = tempat
            nominalText.text = "Rp${nominal}"
        } else {
            Toast.makeText(this, "Data tidak valid. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Tombol Konfirmasi Bayar
        btnKonfirmasi.setOnClickListener {
            if (nim.isNotEmpty() && nominal > 0) {
                // Navigasi ke halaman PIN
                val intent = Intent(this, pinbank::class.java)
                intent.putExtra("destinationNumber", nim) // Kirim NIM sebagai nomor tujuan
                intent.putExtra("amount", nominal) // Kirim nominal pembayaran
                intent.putExtra("selectedBank", selectedBank)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Data tidak valid untuk proses pembayaran.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
