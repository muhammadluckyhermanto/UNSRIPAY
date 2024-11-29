package com.example.unsripay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class transferunsripay : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transferunsripay)

        val etDestinationNumber = findViewById<EditText>(R.id.etDestinationNumber)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val btnConfirmTransfer = findViewById<Button>(R.id.btnConfirmTransfer)

        // Fungsi untuk tombol kembali
        val backButton = findViewById<Button>(R.id.backbutton)
        backButton.setOnClickListener {
            finish() // Menutup activity ini
        }

        // Mengambil data dari Intent
        val selectedBank = intent.getStringExtra("selectedBank") ?: "UnsriPAY"

        // Gunakan value selectedBank
        Log.d("transferunsripay", "Selected Bank: $selectedBank")
        btnConfirmTransfer.setOnClickListener {
            val destinationNumber = etDestinationNumber.text.toString()
            val amount = etAmount.text.toString().toIntOrNull()

            if (amount != null && amount > 0) {
                val intent = Intent(this, pinbank::class.java)
                intent.putExtra("destinationNumber", destinationNumber)
                intent.putExtra("amount", amount)
                intent.putExtra("selectedBank", selectedBank)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Nominal tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
