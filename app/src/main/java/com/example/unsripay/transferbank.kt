package com.example.unsripay

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class transferbank : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transferbank) // Make sure this matches your XML layout file name

        // Reference to the Spinner
        val spinnerBank: Spinner = findViewById(R.id.spinnerBank)

        // List of bank names
        val bankList = listOf("Bank A", "Bank B", "Bank C", "Bank D", "Bank E")

        // Creating an ArrayAdapter to set the items in the Spinner
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            bankList
        )

        // Set the layout for the dropdown items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Attach the adapter to the Spinner
        spinnerBank.adapter = adapter
    }
}
