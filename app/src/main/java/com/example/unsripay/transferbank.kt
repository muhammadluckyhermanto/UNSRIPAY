package com.example.unsripay

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class transferbank : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transferbank)

        // Fungsi untuk tombol kembali
        val backButton = findViewById<Button>(R.id.backbutton)
        backButton.setOnClickListener {
            finish() // Menutup activity ini
        }

        // Inisialisasi Spinner untuk memilih bank
        val spinnerBank = findViewById<Spinner>(R.id.spinnerBank)
        val bankList = listOf(
            getString(R.string.bank_bca),
            getString(R.string.bank_mandiri),
            getString(R.string.bank_bri),
            getString(R.string.bank_bni),
            getString(R.string.bank_cimb)
        )

        // Menyusun adapter untuk Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bankList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBank.adapter = adapter

        // Menangani tombol Lanjut untuk melanjutkan ke form transfer
        val btnLanjut = findViewById<Button>(R.id.btnLanjut)
        btnLanjut.setOnClickListener {
            val selectedBank = spinnerBank.selectedItem?.toString() ?: ""
            if (selectedBank.isNotEmpty()) {
                // Mengirimkan data bank yang dipilih ke activity berikutnya
                val intent = Intent(this, formtransferbank::class.java)
                intent.putExtra("selectedBank", selectedBank)
                startActivity(intent)
            } else {
                // Menampilkan pesan jika bank belum dipilih
                Toast.makeText(this, getString(R.string.choose_bank_message), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
