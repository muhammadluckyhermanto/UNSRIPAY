package com.example.unsripay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TopUpActivity : AppCompatActivity() {

    private lateinit var nominalInput: EditText
    private lateinit var btnLanjut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        // Inisialisasi Views
        nominalInput = findViewById(R.id.input_saldo)
        btnLanjut = findViewById(R.id.btn_lanjut)

        // Tombol Kembali
        val backButton = findViewById<Button>(R.id.backbutton)
        backButton.setOnClickListener {
            finish() // Menutup activity dan kembali ke activity sebelumnya
        }

        // Tombol Lanjut
        btnLanjut.setOnClickListener {
            handleTopUp()
        }
    }

    /**
     * Fungsi untuk menangani proses top up.
     */
    private fun handleTopUp() {
        val nominalStr = nominalInput.text.toString().trim() // Menghapus spasi di awal/akhir

        // Validasi input nominal
        if (nominalStr.isEmpty()) {
            showToast("Kolom jumlah tidak boleh kosong!")
            return
        }

        if (!nominalStr.matches(Regex("\\d+"))) {
            showToast("Masukkan jumlah yang valid!")
            return
        }

        val nominal = nominalStr.toIntOrNull()

        if (nominal == null || nominal <= 0) {
            showToast("Masukkan jumlah yang valid dan lebih besar dari nol!")
            return
        }

        // Melanjutkan ke activity berikutnya jika validasi lolos
        val intent = Intent(this, ConfirmTopUpActivity::class.java).apply {
            putExtra("nominal", nominal)
            putExtra("metode", "Top Up Saldo")
        }
        startActivity(intent)
    }

    /**
     * Menampilkan pesan toast dengan teks tertentu.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
