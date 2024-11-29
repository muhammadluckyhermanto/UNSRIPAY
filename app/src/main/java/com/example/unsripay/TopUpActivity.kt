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

        // Fungsi untuk tombol kembali
        val backButton = findViewById<Button>(R.id.backbutton)
        backButton.setOnClickListener {
            finish() // Menutup activity ini
        }

        // Inisialisasi Views
        nominalInput = findViewById(R.id.input_saldo)
        btnLanjut = findViewById(R.id.btn_lanjut)

        // Listener Tombol Lanjut
        btnLanjut.setOnClickListener {
            val nominalStr = nominalInput.text.toString()

            if (nominalStr.isNotEmpty() && nominalStr.matches(Regex("\\d+"))) {
                val nominal = nominalStr.toIntOrNull()

                if (nominal != null && nominal > 0) {
                    val intent = Intent(this, ConfirmTopUpActivity::class.java)
                    intent.putExtra("nominal", nominal)
                    intent.putExtra("metode", "Top Up Saldo")
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Masukkan jumlah yang valid!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Kolom jumlah tidak boleh kosong atau tidak valid!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
