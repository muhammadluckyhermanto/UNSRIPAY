package com.example.unsripay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class formtransferbank : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.formtransferbank)

        val etDestinationNumber = findViewById<EditText>(R.id.etDestinationNumber)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val btnConfirmTransfer = findViewById<Button>(R.id.btnConfirmTransfer)

        val selectedBank = intent.getStringExtra("selectedBank")

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
