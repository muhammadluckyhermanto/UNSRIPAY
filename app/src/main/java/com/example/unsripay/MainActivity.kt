package com.example.unsripay

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Menangani window insets untuk tampilan edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mendapatkan referensi TextView untuk nama dan saldo
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val saldoTextView: TextView = findViewById(R.id.saldoTextView)

        // Mengamati perubahan data di ViewModel
        mainViewModel.userName.observe(this) { userName ->
            nameTextView.text = userName
        }

        mainViewModel.userSaldo.observe(this) { userSaldo ->
            saldoTextView.text = "Rp $userSaldo"
        }


        // Mengambil data dari Intent atau sumber lain
        val userName = intent.getStringExtra("userName") ?: "Nama tidak ditemukan"
        val userSaldo = intent.getIntExtra("userSaldo", 0)

        // Memperbarui data pada ViewModel
        mainViewModel.updateUserData(userName, userSaldo)

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

        // Fungsi untuk set animasi dan onTouchListener pada tombol
        fun setButtonAnimation(button: View, targetActivity: Class<*>) {
            button.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.clearAnimation()
                        v.startAnimation(scaleDown)
                    }
                    MotionEvent.ACTION_UP -> {
                        v.clearAnimation()
                        v.startAnimation(scaleUp)
                        v.postDelayed({
                            v.setAnimation(null)
                            val intent = Intent(this, targetActivity)
                            startActivity(intent)
                        }, 100)
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        v.clearAnimation()
                        v.startAnimation(scaleUp)
                        v.setAnimation(null)
                    }
                }
                true
            }
        }

        // Mengaplikasikan animasi dan navigasi ke aktivitas lainnya
        setButtonAnimation(findViewById(R.id.button1), TransferActivity::class.java)
        setButtonAnimation(findViewById(R.id.button2), IsiSaldoActivity::class.java)
        setButtonAnimation(findViewById(R.id.button3), ScanQris::class.java)
        setButtonAnimation(findViewById(R.id.button4), RiwayatActivity::class.java)
        setButtonAnimation(findViewById(R.id.button_payment), PembayaranActivity::class.java)
    }

    // Menangani pembaruan data ketika kembali ke aktivitas utama
    override fun onResume() {
        super.onResume()

        // Mendapatkan data terbaru dari Intent atau sumber lain
        val userName = intent.getStringExtra("userName") ?: "Nama tidak ditemukan"
        val userSaldo = intent.getIntExtra("userSaldo", 0)

        // Memperbarui data pada ViewModel
        mainViewModel.updateUserData(userName, userSaldo)
    }
}
