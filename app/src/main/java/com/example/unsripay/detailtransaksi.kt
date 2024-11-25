package com.example.unsripay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class detailtransaksi : AppCompatActivity() {

    private lateinit var transaksiRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailtransaksi)

        // Ambil userId dari SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)

        if (userId == -1) {
            Toast.makeText(this, "Data pengguna tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Inisialisasi Firebase Database
        transaksiRef = FirebaseDatabase.getInstance().getReference("unsripay/transaksi")

        // Mendapatkan transaksiId yang diteruskan dari aktivitas sebelumnya
        val transaksiId = intent.getStringExtra("transaksiId")

        // Ambil referensi tampilan
        val tvTransaksiId = findViewById<TextView>(R.id.tv_transaksiId)
        val tvMetode = findViewById<TextView>(R.id.tv_metode)
        val tvNomorTujuan = findViewById<TextView>(R.id.tv_nomortujuan)
        val tvNominal = findViewById<TextView>(R.id.tv_nominal)
        val tvTimestamp = findViewById<TextView>(R.id.tv_timestamp)
        val btnKembali = findViewById<Button>(R.id.btn_kembali)

        if (transaksiId != null) {
            // Ambil detail transaksi dari Firebase
            transaksiRef.child(transaksiId).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val metode = snapshot.child("metode").value.toString()
                    val nomorTujuan = snapshot.child("nomortujuan").value.toString()
                    val nominal = snapshot.child("nominal").value.toString()
                    val timestamp = snapshot.child("timestamp").value.toString()

                    // Menampilkan detail transaksi
                    tvTransaksiId.text = "ID Transaksi: $transaksiId"
                    tvMetode.text = "Pembayaran: $metode"
                    tvNomorTujuan.text = "Nomor Tujuan: $nomorTujuan"
                    tvNominal.text = "Nominal: Rp$nominal"
                    tvTimestamp.text = "Tanggal Transaksi: $timestamp"
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal mengambil data transaksi", Toast.LENGTH_SHORT).show()
            }
        }

        btnKembali.setOnClickListener {
            handleBackButton(userId)
        }
    }

    override fun onBackPressed() {
        // Ambil userId dari SharedPreferences untuk memastikan data pengguna
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)

        if (userId != -1) {
            handleBackButton(userId)
        } else {
            super.onBackPressed()
        }
    }

    private fun handleBackButton(userId: Int) {
        val userRef = FirebaseDatabase.getInstance().getReference("unsripay/user")
        userRef.child(userId.toString()).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val userName = snapshot.child("nama").value.toString()
                val newBalance = snapshot.child("saldo").value.toString().toInt()

                // Kirim saldo terbaru dan nama pengguna ke MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("userSaldo", newBalance)
                intent.putExtra("userName", userName)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish() // Menutup halaman DetailTransaksi
            } else {
                Toast.makeText(this, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Terjadi kesalahan saat mengakses data pengguna", Toast.LENGTH_SHORT).show()
        }
    }

}

