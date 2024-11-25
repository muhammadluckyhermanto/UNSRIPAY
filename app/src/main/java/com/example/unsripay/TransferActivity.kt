package com.example.unsripay

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TransferActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.opsi_transfer)

        // Handling window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.TransferActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Animasi scale down (mengecil)
        val scaleDown = ScaleAnimation(
            1.0f, 0.9f,  // Start and end scale for X axis
            1.0f, 0.9f,  // Start and end scale for Y axis
            Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point X axis
            Animation.RELATIVE_TO_SELF, 0.5f   // Pivot point Y axis
        ).apply {
            duration = 100 // Durasi animasi dalam milidetik
        }

        // Animasi scale up (mengembalikan ukuran normal)
        val scaleUp = ScaleAnimation(
            0.9f, 1.0f,
            0.9f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 100
        }

        // Function to set the animation and onTouchListener for buttons
        fun setButtonAnimation(button: View, targetActivity: Class<*>) {
            button.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Bersihkan animasi sebelumnya, lalu apply scale down
                        v.clearAnimation()
                        v.startAnimation(scaleDown)
                    }
                    MotionEvent.ACTION_UP -> {
                        // Apply scale up
                        v.clearAnimation()
                        v.startAnimation(scaleUp)

                        // Menghapus animasi setelah selesai untuk mengembalikan ke keadaan semula
                        v.postDelayed({
                            v.setAnimation(null)  // Menghapus animasi dari view
                            // Navigate to the target activity
                            val intent = Intent(this, targetActivity)
                            startActivity(intent)
                        }, 100) // 100ms untuk durasi animasi
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        // Bersihkan animasi sebelumnya, apply scale up, dan hapus animasi
                        v.clearAnimation()
                        v.startAnimation(scaleUp)
                        v.setAnimation(null)  // Menghapus animasi dari view setelah dibatalkan
                    }
                }
                true
            }
        }


// Apply animation and navigation to all buttons
        setButtonAnimation(findViewById(R.id.bttn_bank), transferbank::class.java)
        setButtonAnimation(findViewById(R.id.bttn_sesama), transferunsripay::class.java)

        val btnBank = findViewById<LinearLayout>(R.id.bttn_bank)
        val btnSesama = findViewById<LinearLayout>(R.id.bttn_sesama)

        btnBank.setOnClickListener {
            startActivity(Intent(this, transferbank::class.java))
        }

        btnSesama.setOnClickListener {
            val intent = Intent(this, transferunsripay::class.java)
            intent.putExtra("selectedBank", "UnsriPAY")
            startActivity(intent)
        }




    }
}
