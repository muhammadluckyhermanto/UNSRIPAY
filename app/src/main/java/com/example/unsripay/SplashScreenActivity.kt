package com.example.unsripay

// SplashActivity.kt
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.unsripaypin.PinActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_screen)

        // Timer untuk splash screen
        Handler().postDelayed({
            // Pindah ke MainActivity
            val intent = Intent(this, PinActivity::class.java)
            startActivity(intent)
            finish() // Tutup SplashActivity
        }, 3000) // Durasi splash screen dalam milidetik (3 detik)
    }
}