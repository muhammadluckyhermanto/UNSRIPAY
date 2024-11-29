package com.example.unsripay

// SplashActivity.kt
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_screen)

        // Membuat status bar transparan
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true // Teks status bar terang


        // Timer untuk splash screen
        Handler().postDelayed({
            // Pindah ke MainActivity
            val intent = Intent(this, PinActivity::class.java)
            startActivity(intent)
            finish() // Tutup SplashActivity
        }, 3000) // Durasi splash screen dalam milidetik (3 detik)
    }
}