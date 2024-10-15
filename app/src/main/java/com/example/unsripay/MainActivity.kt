package com.example.unsripay

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Handling window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the LinearLayout (acting as a button)
        val buttonPayment: LinearLayout = findViewById(R.id.button_payment)

        // Set up the OnClickListener
        buttonPayment.setOnClickListener {
            // Navigate to the payment page (PaymentActivity)
            val intent = Intent(this, PembayaranActivity::class.java)
            startActivity(intent)
        }


        // Set up OnClickListener for QRIS button
        val buttonQRIS: LinearLayout = findViewById(R.id.button3)
        buttonQRIS.setOnClickListener {
            // Navigate to the QRIS page (QRISActivity)
            val intent = Intent(this, ScanQris::class.java)
            startActivity(intent)
        }
    }
}
